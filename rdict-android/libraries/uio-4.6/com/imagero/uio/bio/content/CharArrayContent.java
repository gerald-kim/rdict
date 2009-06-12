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

import com.imagero.uio.Transformer;

import java.io.IOException;

/**
 * Date: 05.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class CharArrayContent extends Content {
    char[][] data;
    boolean bigEndian;

    static final int SHIFT = 1;

    public CharArrayContent(char[][] data) {
        this(data, true);
    }

    public CharArrayContent(char[][] data, boolean bigEndian) {
        this.data = data;
        this.bigEndian = bigEndian;
    }

    public int load(long offset, int bpos, byte[] dest) throws IOException {
        //to simplify char to byte converting, we require two bytes boundary
        if ((offset & 1) != 0 || ((dest.length - bpos) & 1) != 0) {
            throw new IOException("Illegal offset or length");
        }
        final long off = offset >> SHIFT;
        final int len = (dest.length - bpos) >> SHIFT;

        IndexAndStart ias = getIAS(off);
        if (ias == null) {
            return 0;
        }
        int index = ias.index;
        int pos = (int) (off - ias.start);
        char[] src = data[index];
        int toCopy = Math.min(src.length - pos, len);
        if (toCopy > 0) {
            com.imagero.uio.Transformer.charToByte(src, pos, toCopy, dest, bpos, bigEndian);
            if ((toCopy < len)) {
                int copiedBytes = (toCopy << SHIFT);
                return toCopy + load(offset + copiedBytes, bpos + copiedBytes, dest);
            }
        }
        return toCopy;
    }

    public void close() {
    }

    private IndexAndStart getIAS(long offset) {
        long start = 0;
        for (int i = 0; i < data.length; i++) {
            char[] src = data[i];
            int length = src.length;
            long end = start + length;
            if (offset >= start && offset <= end) {
                return new IndexAndStart(i, start);
            }
            start += length;
        }
        return null;
    }

    /**
     * Save data to current content (char array).
     * All offsets and lengths must be
     * @param offset
     * @param spos
     * @param src
     * @param length
     * @throws IOException
     */
    public void save(long offset, int spos, byte[] src, int length) throws IOException {
        //to simplify char to byte converting we require two byte boundary
        if ((offset & 1) != 0 || ((src.length - spos) & 1) != 0) {
            throw new IOException("Illegal offset or length");
        }
        final long off = offset >> SHIFT;
        final int len = Math.min((src.length - spos), length) >> SHIFT;

        IndexAndStart ias = getIAS(off);
        if (ias == null) {
            return;
        }
        int index = ias.index;
        long start = ias.start;
        int dpos = (int) (off - start);
        char[] dest = data[index];
        int request = len;
        int toCopy = Math.min((dest.length - dpos), request);
        if (request > 0 && toCopy > 0) {
            com.imagero.uio.Transformer.byteToChar(src, spos, toCopy, dest, dpos, true);
            if (toCopy < request) {
                int copiedBytes = (toCopy << SHIFT);
                save(offset + copiedBytes, spos + copiedBytes, src, (request - toCopy) << SHIFT);
            }
        }
    }

    public boolean canReload() {
        return true;
    }

    public long length() throws IOException {
        long length = 0;
        for (int i = 0; i < data.length; i++) {
            char[] dest = data[i];
            length += dest.length;
        }
        return length << SHIFT;
    }

    public boolean writable() {
        return true;
    }
}
