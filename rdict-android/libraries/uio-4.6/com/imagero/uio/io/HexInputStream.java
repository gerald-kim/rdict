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

import com.imagero.java.util.Debug;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andrey Kuznetsov
 */
public class HexInputStream extends FilterInputStream {

    private static final char encodeTable[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static final int decodeTable [] = new int[0x100];

    static {
        for (int i = 0; i < decodeTable.length; i++) {
            decodeTable[i] = -1;
        }
        for (int j = 0; j < encodeTable.length; j++) {
            decodeTable[encodeTable[j]] = j;
        }
    }

    boolean finished;

    byte[] buffer = new byte[80];
    int count;
    int pos;

    public HexInputStream(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        if (pos >= count) {
            if (finished) {
                return -1;
            }
            else {
                fillBuffer();
            }
        }
        if (pos < count) {
            return buffer[pos++] & 0xFF;
        }
        return -1;
    }

    protected void fillBuffer() {
        int k = 0;
        try {
            for (; k < buffer.length; k++) {
                int b0 = in.read();
                if (b0 == 13 || b0 == 10) {
                    k--;
                    continue;
                }
                if (b0 == '>') {
                    k++;
                    finished = true;
                    break;
                }
                int b1 = in.read();
                int d0 = decodeTable[b0];
                int d1 = decodeTable[b1];
                if (d0 == -1 || d1 == -1) {
                    k--;
                    continue;
                }
                buffer[k] = (byte) (d0 * 16 + d1);
            }
        }
        catch (Throwable ex) {
            Debug.print(ex);
//            Debug.printErr(ex.getMessage());
        }
        count = k;
        pos = 0;
    }

    public long skip(long n) throws IOException {
        long i = 0;
        for (; i < n; i++) {
            int a = read();
            if (a == -1) {
                break;
            }
        }
        return i;
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            return (int) skip(len);
        }
        if (len <= 0) {
            return 0;
        }
        int i = 0;
        try {
            for (; i < len; i++) {
                int a = read();
                if (a == -1) {
                    break;
                }
                b[i] = (byte) a;
            }
        }
        catch (IOException ex) {
        }
        return i == 0 ? -1 : i;
    }
}
