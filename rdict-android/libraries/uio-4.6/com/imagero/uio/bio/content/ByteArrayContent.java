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

import java.io.IOException;

/**
 * Date: 05.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class ByteArrayContent extends Content {
    byte[][] data;

    public ByteArrayContent(byte[][] data) {
        this.data = data;
    }

    public int load(long offset, int bpos, byte[] buffer) throws IOException {
        IndexAndStart ias = getIAS(offset);
        if (ias == null) {
            return 0;
        }
        int index = ias.index;
        long start = ias.start;
        int pos = (int) (offset - start);
        byte[] src = data[index];
        int toCopy = Math.min(src.length - pos, buffer.length - bpos);
        if (toCopy > 0) {
            System.arraycopy(src, pos, buffer, bpos, toCopy);
            if ((toCopy < buffer.length - bpos)) {
                return toCopy + load(offset + toCopy, bpos + toCopy, buffer);
            }
        }
        return toCopy;
    }

    public void close() {
    }

    private IndexAndStart getIAS(long offset) {
        long start = 0;
        for (int i = 0; i < data.length; i++) {
            byte[] src = data[i];
            int length = src.length;
            long end = start + length;
            if (offset >= start && offset <= end) {
                return new IndexAndStart(i, start);
            }
            start += length;
        }
        return null;
    }

    public void save(long offset, int spos, byte[] src, int length) throws IOException {
        IndexAndStart ias = getIAS(offset);
        if (ias == null) {
            return;
        }
        int index = ias.index;
        long start = ias.start;
        int dpos = (int) (offset - start);
        byte[] dest = data[index];
        int request = Math.min(length, src.length - spos);
        int toCopy = Math.min(dest.length - dpos, request);
        if (request > 0 && toCopy > 0) {
            System.arraycopy(src, spos, dest, dpos, toCopy);
            if (toCopy < request) {
                save(offset + toCopy, spos + toCopy, src, request - toCopy);
            }
        }
    }

    public boolean canReload() {
        return true;
    }

    public long length() throws IOException {
        long length = 0;
        for (int i = 0; i < data.length; i++) {
            byte[] dest = data[i];
            length += dest.length;
        }
        return length;
    }

    public boolean writable() {
        return true;
    }
}
