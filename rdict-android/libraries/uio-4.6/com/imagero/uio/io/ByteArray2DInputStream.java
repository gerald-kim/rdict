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

import java.io.ByteArrayInputStream;

/**
 * Like byteArrayInputStream but works with 2d byte array
 * @author Andrey Kuznetsov
 */
public class ByteArray2DInputStream extends ByteArrayInputStream {

    private byte[][] data;

    int current;
    boolean finished;

    int markIndex;

    public ByteArray2DInputStream(byte[][] data) {
        super(data[0]);
        this.data = data;
    }

    public int read() {
        if (finished) {
            return -1;
        }
        if (pos >= count) {
            next();
        }
        return super.read();
    }

    private void next() {
        if ((current + 1) < data.length) {
            buf = data[++current];
            pos = 0;
            count = buf.length;
        }
        else {
            finished = true;
        }
    }

    public int read(byte[] buf, int off, int len) {
        int read = 0;
        while (read < len && !finished) {
            if (pos >= count) {
                next();
            }
            int rd = super.read(buf, off + read, len - read);
            if(rd <= 0) {
                break;
            }
            read += rd;
        }
        return read;
    }

    public long skip(long ns) {
        long skipped = 0;
        while (skipped < ns && !finished) {
            if (pos >= count) {
                next();
            }
            long skp = super.skip(ns - skipped);
            skipped += skp;
            if (skp == 0) {
                break;
            }
        }
        return skipped;
    }

    public void mark(int readAheadLimit) {
        markIndex = current;
        super.mark(readAheadLimit);
    }

    public void reset() {
        buf = data[markIndex];
        super.reset();
        finished = false;
    }
}
