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
 package com.imagero.uio;

import com.imagero.uio.impl.AbstractRandomAccessInput;
import com.imagero.uio.io.IOutils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Wrapper for ByteArrayInputStream which gives possibility to use it as AbstractRandomAccessInput.
 * Date: 19.12.2007
 *
 * @author Andrey Kuznetsov
 */
public class BaisWrapper extends AbstractRandomAccessInput {
    long pos;
    long mark;
    long count;

    long offset;

    ByteArrayInputStream in;

    public BaisWrapper(ByteArrayInputStream in) {
        this.in = in;
        in.mark(0);
        count = in.available();
    }

    public BaisWrapper(BaisWrapper in, long offset, int byteOrder) {
        this.in = in.in;
        this.offset = offset;
        count = in.count - (in.offset + (int)offset);
        setByteOrder(byteOrder);
    }

    public BaisWrapper(BaisWrapper in, long offset, long length, int byteOrder) {
        this.in = in.in;
        this.offset = offset;
        count = in.count - (in.offset + (int) offset);
        if(length > 0) {
            count = Math.min(count, length);
        }
        setByteOrder(byteOrder);
    }

    synchronized public int read() {
        seek0(pos);
        int k = in.read();
        pos++;
        return k;
    }

    synchronized public int read(byte b[], int off, int len) {
        seek0(pos);
        int read = in.read(b, off, len);
        pos += read;
        return read;
    }

    synchronized public long skip(long n) {
        seek0(pos);
        long length = in.skip(n);
        pos += length;
        return length;
    }

    synchronized public void seek(long position) {
        seek0(position);
    }

    private void seek0(long position) {
        in.reset();
        pos = 0;
        if(position + offset > 0) {
            long length = in.skip(position + offset);
            pos += length;
        }
    }

    public boolean markSupported() {
        return true;
    }

    public synchronized void mark(int readlimit) {
        mark = pos;
    }

    public synchronized void reset() {
        seek0(mark);
    }

    public long getFilePointer() {
        return pos;
    }

    public long length() {
        return count - offset;
    }

    public RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) {
        return new BaisWrapper(this, offset, length, byteOrder);
    }

    public void close() {
        IOutils.closeStream(in);
    }

    public InputStream createInputStream(long offset) {
        return new BaisWrapper(this, offset, byteOrder);
    }

    public InputStream createInputStream(long offset, long length) {
        return new BaisWrapper(this, offset, length, byteOrder);
    }

    public long getChildPosition(InputStream child) {
        if(child instanceof BaisWrapper) {
            BaisWrapper wrapper = (BaisWrapper) child;
            return wrapper.getFilePointer();
        }
        return -1;
    }

    public long getChildOffset(InputStream child) {
        return offset;
    }

    public void setChildPosition(InputStream child, long position) {
        if (child instanceof BaisWrapper) {
            BaisWrapper wrapper = (BaisWrapper) child;
            wrapper.seek(position);
        }
    }
}
