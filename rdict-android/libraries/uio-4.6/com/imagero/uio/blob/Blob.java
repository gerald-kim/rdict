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

package com.imagero.uio.blob;

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.io.UnexpectedEOFException;
import com.imagero.java.util.Hashtable;
import com.imagero.java.util.Debug;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

/**
 * Blob - Object which encapsulates (possible deferred)
 * data which may come from different sources.
 *
 * @author Andrey Kuznetsov
 */
public abstract class Blob {

    long length;

    private Hashtable properties = new Hashtable();

    /**
     * retrieve data from this Blob
     * @param start start offset
     * @param length how much bytes to get
     * @return byte array with data
     * @throws java.io.IOException
     */
    public abstract byte[] get(long start, int length) throws IOException;

    /**
     * retrieve data from this Blob
     * @param start start offset
     * @param dest where to copy data
     * @return how much byte were copied
     * @throws java.io.IOException
     */
    public abstract int get(long start, byte[] dest) throws IOException;

    /**
     * determine if this Blob is writable and method set(long, byte[]) can be used to change content of this Blob
     * @return true if Blob is writable
     */
    public abstract boolean writable();

    /**
     * set data (work only if writable returns true)
     * @param start start in destination
     * @param data new data
     * @throws java.io.IOException
     */
    public abstract void set(long start, byte[] data) throws IOException;

    /**
     * release reloadable resources
     */
    public abstract void clear();

    protected boolean lengthKnown() {
        return length > 0;
    }

    protected abstract long computeLength() throws IOException;

    public long getLength() {
        if (!lengthKnown()) {
            try {
                length = computeLength();
            } catch (IOException ex) {
                Debug.println("Can't compute Blob length. " + ex.getMessage(), System.err);
            }
        }
        return length;
    }

    public InputStream getInputStream() throws IOException {
        return new BlobInputStream(this, getLength());
    }

    public long getChildPosition(InputStream in) {
        if (in instanceof BlobInputStream) {
            return ((BlobInputStream) in).getPosition();
        }
        return -1;
    }

    public void setChildPosition(InputStream in, long pos) {
        if (in instanceof BlobInputStream) {
            ((BlobInputStream) in).setPosition(pos);
        }
    }

    public Object getProperty(Object key) {
        return properties.get(key);
    }

    public void setProperty(Object key, Object property) {
        properties.put(key, property);
    }

    public static class BaBlob extends Blob {
        int offset;

        byte[] blob;

        public BaBlob(byte[] blob) {
            this(blob, 0, blob.length);
        }

        public BaBlob(byte[] blob, int offset, int length) {
            this.offset = offset;
            this.length = length;
            this.blob = blob;
        }

        protected long computeLength() {
            return length;
        }

        static final byte[] empty = new byte[0];

        public byte[] get(long start, int length) {
            int len = (int) Math.max(Math.min(length, this.length - start), 0);
            if (len == 0) {
                return empty;
            }
            byte[] dest = new byte[len];
            System.arraycopy(blob, (int) start + offset, dest, 0, len);
            return dest;
        }

        public int get(long start, byte[] dest) {
            long max = Math.max(Math.min(dest.length, this.length - start), 0);
            if (max > 0) {
                System.arraycopy(blob, (int) start + offset, dest, 0, (int) max);
            }
            return (int) max;
        }

        public boolean writable() {
            return true;
        }

        public void set(long start, byte[] data) {
            System.arraycopy(data, 0, blob, (int) start, data.length);
        }

        public void clear() {
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStreamP(blob, offset, (int) getLength());
        }

        public long getChildPosition(InputStream in) {
            if (in instanceof ByteArrayInputStreamP) {
                return ((ByteArrayInputStreamP) in).getPosition();
            }
            return -1;
        }

        public void setChildPosition(InputStream in, long pos) {
            if (in instanceof ByteArrayInputStreamP) {
                ((ByteArrayInputStreamP) in).setPosition(pos);
            }
        }
    }

    static class ByteArrayInputStreamP extends ByteArrayInputStream {
        public ByteArrayInputStreamP(byte buf[]) {
            super(buf);
        }

        public ByteArrayInputStreamP(byte buf[], int offset, int length) {
            super(buf, offset, length);
        }

        public long getPosition() {
            return pos;
        }

        public void setPosition(long pos) {
            this.pos = (int) (pos & Integer.MAX_VALUE);
        }
    }

    public static class RoBlob extends Blob {
        RandomAccessInput ro;
        long start;

        public RoBlob(RandomAccessInput ro, long start, long length) {
            this.ro = ro;
            this.start = start;
            this.length = length;
        }

        protected long computeLength() {
            return length;
        }

        public byte[] get(long start, int length) throws IOException {
            long pos = ro.getFilePointer();
            try {
                byte[] dest = new byte[length];
                ro.seek(this.start + start);
                ro.readFully(dest);
                return dest;
            } finally {
                ro.seek(pos);
            }
        }

        public int get(long start, byte[] dest) throws IOException {
            long max = Math.min(dest.length, length - start);
            long pos = ro.getFilePointer();
            try {
                ro.seek(this.start + start);
                ro.readFully(dest, 0, (int) max);
            } catch (UnexpectedEOFException ex) {
                return (int) ex.getCount();
            } finally {
                ro.seek(pos);
            }
            return (int) max;
        }

        public boolean writable() {
            return ro instanceof RandomAccessIO;
        }

        public void set(long start, byte[] data) throws IOException {
            if (writable()) {
                RandomAccessIO ra = (RandomAccessIO) ro;
                ra.seek(start);
                ra.write(data);
            }
        }

        public void clear() {
        }

        public InputStream getInputStream() throws IOException {
            return ro.createInputStream(start, (int) getLength());
        }

        public long getChildPosition(InputStream in) {
            return ro.getChildPosition(in);
        }

        public void setChildPosition(InputStream in, long pos) {
            ro.setChildPosition(in, pos);
        }
    }

    public static class SeqBlob extends Blob {

        Blob first;
        Blob second;

        public SeqBlob(Blob first, Blob second) {
            this.first = first;
            this.second = second;
        }

        public byte[] get(long start, int length) throws IOException {
            if(start < first.length) {
                return first.get(start, length);
            }
            else {
                return second.get(start - first.getLength(), length);
            }
        }

        public int get(long start, byte[] dest) throws IOException {
            if(start < first.length) {
                return first.get(start, dest);
            }
            else {
                return second.get(start - first.getLength(), dest);
            }
        }

        public boolean writable() {
            return false;
        }

        public void set(long start, byte[] data) throws IOException {
        }

        public void clear() {
            first.clear();
            second.clear();
        }

        protected long computeLength() throws IOException {
            return first.getLength() + second.getLength();
        }

        public InputStream getInputStream() throws IOException {
            return new SequenceInputStream(first.getInputStream(), second.getInputStream());
        }
    }
}
