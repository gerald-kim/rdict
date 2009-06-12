/*
 * Copyright (c) Andrey Kuznetsov. All Rights Reserved.
 *
 * http://res.imagero.com
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

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream for decoding data from base64 encoded String array
 * @author Andrey Kuznetsov
 */
public class Base64DInputStream extends InputStream {

    boolean finished;

    protected byte[] buffer;
    String[] s;

    int current;

    int p;

    public Base64DInputStream(String[] s) {
        this.s = s;
    }

    /**
     * decode next byte
     * @return int
     * @throws IOException
     */
    public int read() throws IOException {
        if (finished) {
            return -1;
        }
        if (buffer == null || p >= buffer.length) {
            next();
        }
        if (buffer == null) {
            finished = true;
            return -1;
        }
        try {
            return buffer[p++] & 0xFF;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            Debug.println(p + " " + buffer.length, System.err);
            throw ex;
        }
    }

    /**
     * switch to next String
     */
    protected void next() throws IOException {
        if (finished) {
            buffer = null;
            p = 0;
            return;
        }
        if (current < s.length) {
            String s = this.s[current++];
            buffer = Base64.base64Decode(s);
            p = 0;
        }
        else {
            finished = true;
            buffer = null;
            p = 0;
        }
    }
}
