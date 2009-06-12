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

import com.imagero.uio.bio.content.HTTPContent;
import com.imagero.uio.impl.TmpRandomAccessFile;
import com.imagero.java.util.Vector;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Date: 05.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class FileCachedHTTPContent extends HTTPContent {

    File tmp;
    TmpRandomAccessFile tmpRaf;

    Vector ranges = new Vector();

    public FileCachedHTTPContent(URL url, File tmp) throws IOException {
        super(url);
        this.tmp = tmp;
        tmpRaf = new TmpRandomAccessFile(tmp, "rw");
    }

    public int load(long offset, int bpos, byte[] buffer) throws IOException {
        if (contains(offset, buffer.length - bpos)) {
            tmpRaf.seek(offset);
            tmpRaf.readFully(buffer, bpos, buffer.length - bpos);
            return buffer.length - bpos;
        }
        int k = super.load(offset, bpos, buffer);
        if (k > 0) {
            tmpRaf.seek(offset);
            tmpRaf.write(buffer, bpos, k);
            addRange(offset, k);
        }
        return k;
    }

    boolean contains(long offset, int length) {
        Range r0 = new Range(offset, offset + length);
        for (int i = 0; i < ranges.size(); i++) {
            Range r = (Range) ranges.elementAt(i);
            if (r.contains(r0)) {
                return true;
            }
        }
        return false;
    }

    void addRange(long offset, int length) {
        Range r0 = new Range(offset, offset + length);

        for (int i = 0; i < ranges.size(); i++) {
            Range r = (Range) ranges.elementAt(i);
            if (r.canJoin(r0)) {
                r.join(r0);
                for (int j = 0; j < ranges.size(); j++) {
                    Range r1 = (Range) ranges.elementAt(j);
                    if (r != r1 && r.canJoin(r1)) {
                        r.join(r1);
                        j--;
                        ranges.remove(r1);
                    }
                }
                return;
            }
        }
        ranges.add(r0);
    }

    public boolean canReload() {
        return true;
    }
}
