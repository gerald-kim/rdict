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

/**
 * This class helps to track which interval was already filed with data.
 * @author Andrey Kuznetsov
 */
public class Range {
    long first;
    long last;

    public Range(long first, long last) {
        if(first == last) {
            throw new IllegalArgumentException("Empty range");
        }
        this.first = Math.min(first, last);
        this.last = Math.max(first, last);
    }

    public boolean contains(Range r) {
        return (r.first >= first && r.last <= last);
    }

    public boolean isOverlap(Range r) {
        return ((r.first >= first && r.first <= last) || (r.last >= first && r.last <= last));
    }

    public boolean isNeighbor(Range r) {
        return ((r.last + 1 == first) || (r.first - 1 == last));
    }

    public boolean canJoin(Range r) {
        return isOverlap(r) || isNeighbor(r);
    }

    public void join(Range r) {
        if(canJoin(r)) {
            this.first = Math.min(first, r.first);
            this.last = Math.max(last, r.last);
        }
    }
}
