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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Read one or more App13 block(s)
 * @author Andrey Kuznetsov
 */
public class App13InputStream extends FilterInputStream {

    public static final int MARKER = 0xFF;
    public static final int APP_13 = 0xED;

    private static final byte[] ID8 = "8BIM".getBytes();
    private static final byte[] PHOTOSHOP = "Photoshop3.0".getBytes();

    boolean finished;

    /**
     * Create new App13InputStream.
     * Note that <code>in</code> should support <code>mark()</code>
     * @param in InputStream
     * @throws IOException
     */
    public App13InputStream(InputStream in) throws IOException {
        super(in);
    }

    void initBlock() throws IOException {
        in.mark(3);
        int marker = in.read();
        int app13 = in.read();
        if (marker != MARKER || app13 != APP_13) {
            finished = true;
            in.reset();
            return;
        }
        length = (in.read() << 8) | (in.read() & 0xFF) - 2;

        in.mark(4);
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) in.read();
        }
        in.reset();

        boolean photoshop = true;

        //some applications "forget" to write 'Photoshop 3.0' Identifier
        for (int i = 0; i < b.length; i++) {
            if (b[i] != PHOTOSHOP[i]) {
                photoshop = false;
                break;
            }
        }

        if (photoshop) {
            for (int i = 0; i < 14; i++) {
                in.read();
            }
            length -= 14;
        }
        else {
            for (int i = 0; i < b.length; i++) {
                if (b[i] != ID8[i]) {
                    throw new IOException("not App13 stream");
                }
            }
        }
    }

    int length;

    public int read() throws IOException {
        if (finished) {
            return -1;
        }
        if (length == 0) {
            initBlock();
            if (finished) {
                return -1;
            }
        }
        length--;
        return super.read();
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off + len > b.length || off < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int read = 1;
        int a = read();
        if (a == -1) {
            return -1;
        }
        b[off] = (byte) a;
        for (int i = off + 1; i < len; i++) {
            a = read();
            if (a == -1) {
                break;
            }
            read++;
            b[i] = (byte) a;
        }
        return read;
    }

    public long skip(long n) throws IOException {
        long remaining = n;
        while (remaining > 0) {
            int a = read();
            if (a == -1) {
                break;
            }
            remaining--;
        }
        return n - remaining;
    }

    public int available() throws IOException {
        if (finished) {
            return 0;
        }
        if (length == 0) {
            initBlock();
            if (finished) {
                return 0;
            }
        }
        return length;
    }

    public synchronized void mark(int readlimit) {

    }

    public synchronized void reset() throws IOException {

    }

    public boolean markSupported() {
        return false;
    }
}
