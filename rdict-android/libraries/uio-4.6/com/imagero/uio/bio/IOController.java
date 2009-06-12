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
package com.imagero.uio.bio;

import com.imagero.uio.bio.content.SynchronizedContent;
import com.imagero.uio.bio.content.Content;
import com.imagero.uio.UIOStreamBuilder;
import com.imagero.java.util.OpenVector;
import com.imagero.java.util.Debug;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Buffer controller.
 * IOController loads data from Content and maintains buffers (FixedSizeBuffer).
 * @author Andrey Kuznetsov
 */
public class IOController {

    OpenVector bufs = new OpenVector(100);
    int bufferSize = UIOStreamBuilder.DEFAULT_CHUNK_SIZE;
    int arrayLength = 1000;
    Content content;
    Ring rs;
    int maxBufferCount = UIOStreamBuilder.DEFAULT_CHUNK_COUNT;
    long explicitLength;
    int streamCount;

    public IOController(int bufferSize, Content content) {
        this.bufferSize = bufferSize;
        this.content = content;
        this.rs = new Ring(maxBufferCount);
    }

    final void setLength(long newLength) {
        explicitLength = newLength;
    }

    private Enumeration buffers(final boolean allowNullValues) {
        final FixedSizeByteBuffer empty = allowNullValues ? null : FixedSizeByteBuffer.createBuffer(new byte[bufferSize]);
        return new Enumeration() {

            final long length = length();
            final BufferIndex max = getBufferIndex(length);
            final BufferIndex bi = new BufferIndex(0, 0);

            public boolean hasMoreElements() {
                boolean b = bi.arrayIndex < max.arrayIndex;
                boolean b2 = bi.index <= max.index;
                return b || b2;
            }

            public Object nextElement() {
                if (hasMoreElements()) {
                    if (!(bi.index < arrayLength)) {
                        bi.index = 0;
                        bi.arrayIndex++;
                    }
                    FixedSizeByteBuffer fb = getBuffer(bi.arrayIndex, bi.index++);
                    return fb != null || allowNullValues ? fb : empty;
                }
                else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    private Enumeration buffers(final long maxPos, final boolean allowNullValues) {
        return new Enumeration() {

            final FixedSizeByteBuffer empty = FixedSizeByteBuffer.createBuffer(new byte[bufferSize]);
            final long length = Math.min(maxPos, length());
            final BufferIndex max = getBufferIndex(length);
            final BufferIndex bi = new BufferIndex(0, 0);

            public boolean hasMoreElements() {
                boolean b = bi.arrayIndex < max.arrayIndex;
                boolean b2 = bi.index <= max.index;
                return b || b2;
            }

            public Object nextElement() {
                if (hasMoreElements()) {
                    if (!(bi.index < arrayLength)) {
                        bi.index = 0;
                        bi.arrayIndex++;
                    }
                    FixedSizeByteBuffer fb = getBuffer(bi.arrayIndex, bi.index++);
                    return fb != null || allowNullValues ? fb : empty;
                }
                else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    long length() {
        if (explicitLength > 0) {
            return explicitLength;
        }

        long contentLength = 0;
        try {
            contentLength = content.length();
        }
        catch (IOException ex) {
            Debug.print(ex);
        }

        Object[] elements = bufs.getElements();
        int maxI = elements.length - 1;
        for (int i = maxI; i >= 0; i--) {
            BufferArray ba = (BufferArray) elements[i];
            if (ba != null) {
                FixedSizeByteBuffer[] buffers = ba.buffers;
                int maxJ = buffers.length - 1;
                for (int j = maxJ; j >= 0; j--) {
                    FixedSizeByteBuffer buffer = buffers[j];
                    if (buffer != null) {
                        int count = buffer.getCount();
                        long startOffset = getStartOffset(buffer.index, bufferSize);
                        if (count > 0) {
                            return Math.max(contentLength, startOffset + count);
                        }
                    }
                }
            }
        }
        return contentLength;
    }

    void writeTo(OutputStream out) throws IOException {
        Enumeration e = buffers(false);
        while (e.hasMoreElements()) {
            FixedSizeByteBuffer buffer = (FixedSizeByteBuffer) e.nextElement();
            buffer.writeBuffer(out, e.hasMoreElements());
        }
    }

    void writeTo(DataOutput out) throws IOException {
        Enumeration e = buffers(false);
        while (e.hasMoreElements()) {
            FixedSizeByteBuffer buffer = (FixedSizeByteBuffer) e.nextElement();
            buffer.writeBuffer(out, e.hasMoreElements());
        }
    }

    private class BufferArray {

        FixedSizeByteBuffer[] buffers;

        public BufferArray() {
            buffers = new FixedSizeByteBuffer[arrayLength];
        }
    }

    private FixedSizeByteBuffer getBuffer(BufferIndex bi) {
        return getBuffer(bi.arrayIndex, bi.index);
    }

    private FixedSizeByteBuffer getBuffer(int aIndex, int index) {
        Object[] objects = bufs.checkSize(aIndex);
        BufferArray ba = (BufferArray) objects[aIndex];
        if (ba == null) {
            ba = new BufferArray();
            objects[aIndex] = ba;
        }
        return ba.buffers[index];
    }

    protected void setBuffer(BufferIndex index, FixedSizeByteBuffer buffer) {
        Object[] objects = bufs.checkSize(index.arrayIndex);
        BufferArray ba = (BufferArray) objects[index.arrayIndex];
        ba.buffers[index.index] = buffer;
    }

    public long flushBefore(long pos) {
        pos = (pos / bufferSize) * bufferSize;
        Enumeration e = buffers(pos, true);
        while (e.hasMoreElements()) {
            FixedSizeByteBuffer o = (FixedSizeByteBuffer) e.nextElement();
            if (o != null) {
                o.buf = null;
            }
        }
        return pos;
    }

    public BufferIndex getBufferIndex(long pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("Negative stream position");
        }
        long count = pos / bufferSize;
        long aIndex = count / arrayLength;
        int index = (int) (count % arrayLength);
        if (aIndex > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Please increase buffer size");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("Please increase buffer size");
        }
        BufferIndex bi = new BufferIndex((int) aIndex, index);
        return bi;
    }

    public FixedSizeByteBuffer getBuffer(long pos) {
        return getBuffer(getBufferIndex(pos));
    }

    FixedSizeByteBuffer getBuffer(long pos, boolean load) throws IOException {
        BufferIndex bi = getBufferIndex(pos);
        long startOffset = getStartOffset(bi, bufferSize);
        FixedSizeByteBuffer sb = getBuffer(bi);
        if (sb == null) {
            sb = FixedSizeByteBuffer.createBuffer(new byte[bufferSize]);
            setBuffer(bi, sb);
            sb.index = bi;
            if (load || (content.canReload() && content.length() >= startOffset + bufferSize)) {
                long max = content.length();
                if (pos > max) {
                    return null;
                }

                int size = content.load(startOffset, sb.buf);
                sb.count = size;
            }
            if (content.canReload()) {
                checkBuffers(sb);
            }
        }
        else {
            if (sb.buf == null) {
                long max = content.length();
                if (pos > max) {
                    return null;
                }
                sb.buf = new byte[bufferSize];
                int size = content.load(startOffset, sb.buf);
                sb.count = size;
            }
        }
        return sb;
    }

    private void checkBuffers(FixedSizeByteBuffer buffer0) {
        FixedSizeByteBuffer buffer = (FixedSizeByteBuffer) rs.add(buffer0);
        if (buffer != null && content.writable()) {
            if (buffer.changed) {
                try {
                    long offset = getStartOffset(buffer.index, bufferSize);
                    content.save(offset, 0, buffer.buf, buffer.getCount());
                    buffer.changed = false;
                }
                catch (IOException ex) {
                    Debug.print(ex);
                }
            }
            setBuffer(buffer.index, null);
            buffer.buf = null;
        }
    }

    void sync() throws IOException {
        boolean canWrite = content.writable();
        if (!canWrite) {
            return;
        }

        try {
            long length = content.length(); //may be already closed
        }
        catch (IOException ex) {
            //stream was already closed, so just return
            return;
        }
        Enumeration e = buffers(true);
        while (e.hasMoreElements()) {
            FixedSizeByteBuffer buffer = (FixedSizeByteBuffer) e.nextElement();
            if (buffer != null && buffer.changed) {
                long offset = getStartOffset(buffer.index, bufferSize);
                content.save(offset, 0, buffer.buf, buffer.getCount());
                buffer.changed = false;
            }
        }
    }

    public long getStartOffset(BufferIndex bufferIndex, int bufferSize) {
        long arrayIndex = bufferIndex.arrayIndex;
        long y = arrayLength * bufferSize;
        long res = arrayIndex * y;
        res += bufferIndex.index * bufferSize;
        return res;
    }

    /**
     * determine if access to stream content is synchronized
     */
    public boolean isSynchronizedContent() {
        return content instanceof SynchronizedContent;
    }

    /**
     * define if access to content should be syncronized.
     */
    public void setSynchronizedContent(boolean b) {
        if (b) {
            if (!isSynchronizedContent()) {
                content = new SynchronizedContent(content);
            }
        }
        else {
            if (isSynchronizedContent()) {
                SynchronizedContent synchronizedContent = (SynchronizedContent) content;
                content = synchronizedContent.getContent();
            }
        }
    }

    public void close() {
        if (--streamCount <= 0) {
            if (content != null) {
                content.close();
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (content != null) {
            content.close();
        }
        content = null;
        rs = null;
        bufs = null;
    }
}
