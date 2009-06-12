/*
 * Copyright (c) Andrey Kuznetsov. All Rights Reserved.
 *
 * http://uio.imagero.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Andrey Kuznetsov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.imagero.uio.bio.content;

import com.imagero.uio.io.IOutils;
import com.imagero.uio.io.UnexpectedEOFException;
import com.imagero.java.util.Hashtable;

import java.io.InputStream;
import java.io.IOException;
import java.io.EOFException;

/**
 * Date: 05.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class MemoryCachedInputStreamContent extends Content {
    InputStream in;
    int chunkSize;
    boolean finished;

    int overflow = 5;

    Hashtable ht = new Hashtable();

    int lastChunkSize;

    public MemoryCachedInputStreamContent(InputStream in, int chunkSize) {
        this.in = in;
        this.chunkSize = chunkSize;
    }

    public int load(long offset, int destOffset, byte[] dest) throws IOException {
        long index = offset / chunkSize;
        if (finished && index >= readCount) {
            throw new EOFException();
        }
        if (!finished) {
            try {
                for (long i = readCount; i <= index; i++) {
                    byte[] b = new byte[chunkSize];
                    Chunk chunk = addChunk(b, i);   //first add then read
                    int length = b.length;
                    lastChunkSize = chunkSize;
                    try {
                        IOutils.readFully(in, b);
                    } catch (UnexpectedEOFException ex) {
                        length = (int) ex.getCount();
                    }
                    if (chunk.src.length != length) {
                        if (length > 0) {
                            b = new byte[length];
                            System.arraycopy(chunk.src, 0, b, 0, length);
                            chunk.src = b;
                            lastChunkSize = length;
                        } else {
                            ht.remove(new Long(i));
                        }
                        finished = true;
                        break;
                    }
                }
            } catch (IOException ex) {
                finished = true;
            }
        }
        if (index <= readCount) {
            return copyData(dest, destOffset, offset);
        }
        return 0;
    }

    protected void prepare() {
        try {
            for (long i = readCount; !finished; i++) {
                byte[] b = new byte[chunkSize];
                Chunk chunk = addChunk(b, i);   //first add then read
                int length = b.length;
                lastChunkSize = chunkSize;
                try {
                    IOutils.readFully(in, b);
                } catch (UnexpectedEOFException ex) {
                    length = (int) ex.getCount();
                }
                if (chunk.src.length != length) {
                    if (length > 0) {
                        b = new byte[length];
                        System.arraycopy(chunk.src, 0, b, 0, length);
                        chunk.src = b;
                        lastChunkSize = length;
                    } else {
                        ht.remove(new Long(i));
                    }
                    finished = true;
                    break;
                }
            }
        } catch (IOException ex) {
            finished = true;
        }
    }

    private int copyData(byte[] dest, int destOffset, long streamOffset) {
        long index = streamOffset / chunkSize;
        Chunk chunk = (Chunk) ht.get(new Long(index));
        if (chunk != null) {
            return chunk.copyInterval(dest, destOffset, streamOffset);
        }
        return 0;
    }

    public void close() {
    }

    long readCount;

    private Chunk addChunk(byte[] buf, long index) {
        long start = index * chunkSize;
        Chunk helper = new Chunk(buf, index, start);
        readCount++;
        ht.put(new Long(index), helper);
        return helper;
    }

    class Chunk {
        byte[] src;
        long index;

        long start;

        private Chunk parent;

        private Chunk left;
        private Chunk right;

        public Chunk(byte[] buf, long index, long start) {
            this.src = buf;
            this.index = index;
            this.start = start;
        }

        /**
         *
         * @param dest destination array
         * @param destOffset start offset in destination array
         * @param absOffset absolute offset in stream
         * @return how much bytes was copied
         */
        int copyInterval(byte[] dest, int destOffset, long absOffset) {
            if (src != null) {
                if ((start > absOffset) || (absOffset > start + src.length)) {
                    throw new IndexOutOfBoundsException("Given offset is out of chunk bounds");
                }
                if (destOffset < 0 || destOffset > dest.length) {
                    throw new IndexOutOfBoundsException("Illegal destination offset: " + destOffset);
                }
                int srcOffset = (int) (absOffset - start);
                int length = Math.min(dest.length - destOffset, src.length - srcOffset);
                System.arraycopy(src, srcOffset, dest, destOffset, length);
                if (srcOffset == 0 && length == src.length) {
                    free();
                } else {
                    if (srcOffset == 0) {
                        //right part leftover
                        byte[] buf = new byte[src.length - length];
                        System.arraycopy(src, length, buf, 0, buf.length);
                        src = buf;
                    } else if (srcOffset + length == src.length) {
                        //left part leftover
                        byte[] buf = new byte[src.length - length];
                        System.arraycopy(src, 0, buf, 0, buf.length);
                        src = buf;
                    } else {
                        byte[] leftBuf = new byte[srcOffset];
                        System.arraycopy(src, 0, leftBuf, 0, leftBuf.length);
                        left = new Chunk(leftBuf, -1, start);
                        left.parent = this;

                        byte[] rightBuf = new byte[src.length - (srcOffset + length)];
                        System.arraycopy(src, srcOffset + length, rightBuf, 0, rightBuf.length);
                        right = new Chunk(rightBuf, -1, start + srcOffset + length);
                        right.parent = this;

                        src = null;
                    }
                }
                return length;
            } else {
                Chunk chunk = getChild(absOffset);
                if (chunk != null) {
                    return chunk.copyInterval(dest, destOffset, absOffset);
                }
            }
            return 0;
        }

        private Chunk getChild(long absOffset) {
            if (right.start <= absOffset) {
                if (right.src != null) {
                    return right;
                } else {
                    return right.getChild(absOffset);
                }
            } else if (left.start <= absOffset) {
                if (left.src != null) {
                    return left;
                } else {
                    return left.getChild(absOffset);
                }
            }
            return null;
        }

        private void free() {
            if (parent != null) {
                parent.removeChild(this);
            } else {
                ht.remove(new Long(index));
            }
        }

        private void removeChild(Chunk c) {
            if (c == null || c.parent != this) {
                return;
            }
            if (c == left) {
                left = null;
            } else if (c == right) {
                right = null;
            } else {
                return;
            }

            if (left == null && right == null) {
                free();
            } else {
                if (left != null) {
                    connectChild(left);
                } else if (right != null) {
                    connectChild(right);
                }
            }
        }

        private void connectChild(Chunk c) {
            src = c.src;
            left = c.left;
            right = c.right;
        }
    }


    public boolean canReload() {
        return false;
    }

    public void save(long offset, int bpos, byte[] buffer, int length) throws IOException {
    }

    public long length() throws IOException {
        if (!finished) {
            prepare();
        }
        return (readCount - 1) * chunkSize + lastChunkSize;
    }

    public boolean writable() {
        return false;
    }
}
