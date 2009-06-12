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

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class can be used to read from and write to byte array.
 *
 * @author Andrey Kuznetsov
 */
public class VariableSizeByteBuffer {

    protected Buffer buf;
    int count;

    boolean changed;

    public VariableSizeByteBuffer(int size) {
        this(new byte[size]);
    }

    public VariableSizeByteBuffer(byte buf[]) {
        this(new Buffer(buf));
    }

    VariableSizeByteBuffer(Buffer buf) {
        this.buf = buf;
        count = buf.buffer.length;
    }

    public VariableSizeByteBuffer create() {
        return new VariableSizeByteBuffer(buf);
    }

    public void seek(int pos, BufferPosition position) {
        position.pos = pos;
    }

    public int read(BufferPosition position) {
        if (position.pos >= count) {
            return -1;
        }
        return buf.buffer[position.pos++] & 0xFF;
    }

    public long skip(long n, BufferPosition position) {
        long p = Math.max(0L, Math.min(n, Integer.MAX_VALUE));
        position.pos += p;
        return p;
    }

    /**
     * get amount of bytes which may be written without changing buffer size
     */
    public int availableForWriting(BufferPosition position) {
        return buf.buffer.length - position.pos;
    }

    public int availableForReading(BufferPosition position) {
        return Math.max(0, Math.min(count, position.bufferSize) - position.pos);
    }

    public int read(byte[] dest, int offset, int length, BufferPosition position) {
        final int available = availableForReading(position);
        int toRead = Math.max(0, Math.min(length, available));
        if (toRead > 0) {
            System.arraycopy(buf.buffer, position.pos, dest, offset, toRead);
            position.pos += toRead;
        }
        return toRead;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = Math.min(Math.max(count, 0), buf.buffer.length);
    }

    public void writeBuffer(OutputStream out) throws IOException {
        out.write(buf.buffer, 0, count);
    }

    public void writeBuffer(DataOutput out) throws IOException {
        out.write(buf.buffer, 0, count);
    }

    public void write(byte b[], int offset, int length, BufferPosition position) {
        if (length > 0) {
            checkSize(length, position);
            System.arraycopy(b, offset, buf.buffer, position.pos, length);
            position.pos += length;
            count = Math.max(count, position.pos);
        }
    }

    public void write(int b, BufferPosition position) {
        checkSize(1, position);
        buf.buffer[position.pos++] = (byte) b;
        count = Math.max(count, position.pos);
    }

    private synchronized void checkSize(int k, BufferPosition position) {
        if (position.pos + k > buf.buffer.length) {
            byte newbuf[] = new byte[Math.max(buf.buffer.length << 1, position.pos + k)];
            System.arraycopy(buf.buffer, 0, newbuf, 0, count);
            buf.buffer = newbuf;
        }
    }

    public InputStream getInputStream(int offset) {
        return new VSBInputStream(offset, this);
    }

    public InputStream getInputStream(int offset, int length) {
        return new VSBInputStream(offset, this, length);
    }

    public OutputStream getOutputStream(int offset) {
        return new VSBOutputStream(offset, this);
    }
}
