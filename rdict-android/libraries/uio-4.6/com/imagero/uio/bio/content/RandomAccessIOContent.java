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

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.io.IOutils;
import com.imagero.java.util.Debug;

import java.io.IOException;
import java.io.EOFException;

/**
 * Date: 05.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class RandomAccessIOContent extends Content {
    private RandomAccessIO rio;

    public RandomAccessIOContent(RandomAccessIO rio) throws IOException {
        this.rio = rio;
    }

    public int load(long offset, int bpos, byte[] b) throws IOException {
        long max = rio.length() - offset;
        int len = (int) Math.min(max, b.length - bpos);
        if (len > 0) {
            rio.seek(offset);
            rio.readFully(b, bpos, len);
            return len;
        }
        throw new EOFException();
//            return 0;
    }

    public boolean canReload() {
        return true;
    }

    public void close() {
        IOutils.closeStream((RandomAccessInput) rio);
        rio = null;
    }

    public boolean writable() {
        return true;
    }

    public void save(long offset, int bpos, byte[] buffer, int length) throws IOException {
        rio.seek(offset);
        try {
            rio.write(buffer, bpos, length);
        } catch (IndexOutOfBoundsException ex) {
            Debug.print(ex);
        }
    }

    public long length() throws IOException {
        return rio.length();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        rio = null;
    }
}
