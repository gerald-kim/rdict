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

package com.imagero.uio.io;

import java.io.ByteArrayOutputStream;

/**
 * ByteArrayOutputStream which writes to external buffer.
 * Length of this external buffer can't be changed.
 *
 * @author Andrey Kuznetsov
 */
public class ByteArrayOutputStream2 extends ByteArrayOutputStream {
    public ByteArrayOutputStream2(byte[] buffer) {
        super(0);
        buf = buffer;
    }

    /**
     * write given byte to buffer.
     *
     * @param b byte to write
     * @throws ArrayIndexOutOfBoundsException if new byte count would exceed length of buffer after this operation
     */
    public void write(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            throw new ArrayIndexOutOfBoundsException(newcount);
        }
        buf[count] = (byte) b;
        count = newcount;
    }

    /**
     * combine (OR) current value with given byte using supplied mask.
     * resulting value is (b & mask) | (currentValue & ~mask)
     * @param b byte to combine
     * @param mask 8 bit mask
     * @throws ArrayIndexOutOfBoundsException
     */
    public void write(int b, int mask) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            throw new ArrayIndexOutOfBoundsException(newcount);
        }
        buf[count] = (byte) ((b & mask) | (buf[count] & ~mask));
        count = newcount;
    }

    /**
     * Writes bytes from the specified byte array to buffer
     * @param b byte array
     * @param off start offset
     * @param len number of bytes to write
     * @throws ArrayIndexOutOfBoundsException if new byte count would exceed length of buffer after this operation (however the max possible byte count is written first)
     */
    public void write(byte b[], int off, int len) {
        if (len == 0) {
            return;
        }
        if ((off < 0) || (len < 0) || ((off + len) > b.length)) {
            throw new IndexOutOfBoundsException();
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            int max = buf.length - count;
            System.arraycopy(b, off, buf, count, max);
            throw new ArrayIndexOutOfBoundsException(newcount);
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    public int getCount() {
        return count;
    }

    /**
     * Skip some bytes.
     * Negative skip is possible.
     * @param n byte count to skip
     * @return how much bytes were skipped
     */
    public int skip(int n) {
        int p = this.count;
        seek(p + n);
        return this.count - p;
    }

    public void seek(int pos) {
        this.count = Math.min(Math.max(pos, 0), buf.length - 1);
    }
}
