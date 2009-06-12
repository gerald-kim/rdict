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

import com.imagero.uio.RandomAccessIO;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class can be used to read from and write to byte array.
 *
 * @author Andrey Kuznetsov
 */
public class FixedSizeByteBuffer {

    protected byte[] buf;
    protected int count;

    boolean changed;
    BufferIndex index;

    protected FixedSizeByteBuffer(byte buf[]) {
        this.buf = buf;
    }

    public int read(BufferPosition position) {
        if (availableForReading(position) > 0) {
            int v = buf[position.pos++] & 0xFF;
            return v;
        }
        return -1;
    }

    public long skip(long n, BufferPosition position) {
        long p = Math.max(0, Math.min(count - position.pos, n));
        position.pos += p;
        return p;
    }

    public BufferPosition createPosition() {
        return new BufferPosition(buf.length);
    }

    public int availableForReading(BufferPosition position) {
        return Math.max(0, Math.min(count, position.bufferSize) - position.pos);
    }

    public int availableForWriting(BufferPosition position) {
        return Math.max(0, buf.length - position.pos);
    }

    public int read(byte[] dest, int offset, int length, BufferPosition position) {
        final int available = availableForReading(position);
        int toCopy = Math.max(0, Math.min(length, available));
        if (toCopy > 0) {
            System.arraycopy(buf, position.pos, dest, offset, toCopy);
            position.pos += toCopy;
        }
        return toCopy;
    }

    /**
     * write given byte to buffer.
     *
     * @param b int to write
     */
    public void write(int b, BufferPosition position) {
        buf[position.pos++] = (byte) b;
        count = Math.max(position.pos, count);
    }

    public int getCount() {
        return count;
    }

    public int getPosition(BufferPosition position) {
        return position.pos;
    }

    public void setCount(int count) {
        this.count = Math.min(Math.max(count, 0), buf.length);
    }

    /**
     * write buffer contents to OutputStream
     * @param wholeBuffer if true then whole buffer is written, otherwise only getCount() bytes are written
     */
    public void writeBuffer(OutputStream out, boolean wholeBuffer) throws IOException {
        if (wholeBuffer) {
            out.write(buf);
        } else {
            out.write(buf, 0, count);
        }
    }

    public void writeBuffer(DataOutput out, boolean wholeBuffer) throws IOException {
        if (wholeBuffer) {
            out.write(buf);
        } else {
            out.write(buf, 0, count);
        }
    }

    /**
     * write whole buffer contents to OutputStream (count is ignored)
     */
    public void writeBuffer(OutputStream out) throws IOException {
        out.write(buf);
    }

    public void writeBuffer(DataOutput out) throws IOException {
        out.write(buf);
    }

    public int write(byte src[], int offset, int length, BufferPosition position) {
        int available = availableForWriting(position);
        int toCopy = Math.max(0, Math.min(length, available));
        if (toCopy > 0) {
            System.arraycopy(src, offset, buf, position.pos, toCopy);
            position.pos += toCopy;
            count = Math.max(count, position.pos);
        }
        return toCopy;
    }

    public RandomAccessIO create() {
        return new FSBRandomAccessIO(this);
    }

    public RandomAccessIO create(int offset, int length) {
        return new FSBRandomAccessIO(this, offset, length);
    }

    public static FixedSizeByteBuffer createBuffer(byte buf[]) {
        return new FixedSizeByteBuffer(buf);
    }
}
