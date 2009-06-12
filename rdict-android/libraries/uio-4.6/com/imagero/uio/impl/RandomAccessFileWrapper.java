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

package com.imagero.uio.impl;

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.RandomAccessOutput;
import com.imagero.uio.UIOStreamBuilder;
import com.imagero.uio.bio.BufferedRandomAccessIO;
import com.imagero.uio.bio.IOCInputStream;
import com.imagero.uio.bio.IOCOutputStream;
import com.imagero.uio.bio.IOController;
import com.imagero.uio.bio.content.Content;
import com.imagero.uio.bio.content.RandomAccessFileContent;
import com.imagero.uio.io.IOutils;

import java.io.*;

/**
 * Wrap RandomAccessFile in RandomAccessIO<br>
 * Attention - this class is not buffered.
 * That means if you make extensive use of writeInt, writeLong, writeChar, ...,
 * then performance will be pretty poor. Use buffered classes instead.
 *
 * @author Andrei Kouznetsov
 *         Date: 08.11.2003
 *         Time: 13:04:44
 */
public class RandomAccessFileWrapper extends AbstractRandomAccessIO {

    RandomAccessFile in;

    IOController controller;

    long offset;
    Long length;

    public RandomAccessFileWrapper(RandomAccessFile in, int byteOrder) throws IOException {
        this.in = in;
        setByteOrder(byteOrder);
    }

    public RandomAccessFileWrapper(RandomAccessFile in, long offset, int byteOrder) throws IOException {
        if (offset < 0 || offset >= in.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.in = in;
        this.offset = offset;
        setByteOrder(byteOrder);
    }

    public RandomAccessFileWrapper(RandomAccessFile in, long offset, long length, int byteOrder) throws IOException {
        if (offset < 0 || offset >= in.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.in = in;
        this.offset = offset;
        this.length = new Long(length);
        setByteOrder(byteOrder);
    }

    protected int _read() throws IOException {
        if (getFilePointer() >= length()) {
            throw new EOFException();
        }
        int i = in.read();
        if (i < 0) {
            throw new EOFException();
        }
        return i;
    }

    public void write(int b) throws IOException {
        in.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        int max = len;
        if (length != null) {
            max = (int) Math.min(Math.max(length() - getFilePointer(), 0), len);
        }
        in.write(b, off, max);
    }

    public long getFilePointer() throws IOException {
        if (length == null) {
            return in.getFilePointer() - offset;
        }
        else {
            return Math.min(length.longValue(), in.getFilePointer() - offset);
        }
    }

    public long length() throws IOException {
        if (length == null) {
            return in.length() - offset;
        }
        else {
            return Math.min(length.longValue(), in.length() - offset);
        }
    }

    public void seek(long pos) throws IOException {
        long max = pos;
        if (length != null) {
            max = Math.min(Math.max(pos, 0), length.longValue());
        }
        in.seek(max + offset);
    }

    public int read() throws IOException {
        if (getFilePointer() >= length()) {
            return -1;
        }
        return in.read();
    }

    public long skip(long n) throws IOException {
        int max = (int) Math.min(Math.max(length() - getFilePointer(), 0), n);
        return in.skipBytes(max);
    }

    public void close() throws IOException {
        IOutils.closeStream(in);
        in = null;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int max = (int) Math.min(Math.max(length() - getFilePointer(), 0), len);
        if(max == 0) {
            return -1;
        }
        return in.read(b, off, max);
    }

    /**
     * Set length - only possible if length was not set in constructor.
     * With 1.2 and later this method works as expected,
     * with 1.1 it can only grow the file, but can not truncate it.
     *
     * @param newLength
     *
     * @throws java.io.IOException
     */
    public void setLength(long newLength) throws IOException {
        if (length == null) {
            in.setLength(newLength);
        }
    }

    public void readFully(byte b[], int off, int len) throws IOException {
        in.readFully(b, off, len);
    }

    public RandomAccessIO createIOChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException {
        if (controller == null) {
            Content content = new RandomAccessFileContent(in);
            controller = new IOController(UIOStreamBuilder.DEFAULT_CHUNK_SIZE, content);

        }
        BufferedRandomAccessIO rio = new BufferedRandomAccessIO(controller, offset);
        if(length > 0) {
            rio.setLength(length);
        }
        return rio;
    }

    public RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException {
        return createIOChild(offset, length, byteOrder, syncPointer);
    }

    public RandomAccessOutput createOutputChild(long offset, int byteOrder, boolean syncPointer) throws IOException {
        return createIOChild(offset, 0, byteOrder, syncPointer);
    }

    public InputStream createInputStream(long offset) {
        if(controller == null) {
            Content bc = new RandomAccessFileContent(in);
            controller = new IOController(UIOStreamBuilder.DEFAULT_CHUNK_SIZE, bc);
        }
        return new IOCInputStream(controller, offset);
    }

    public InputStream createInputStream(long offset, long length) {
        if(controller == null) {
            Content bc = new RandomAccessFileContent(in);
            int len = ((int) length) & Integer.MAX_VALUE;
            controller = new IOController(Math.min(UIOStreamBuilder.DEFAULT_CHUNK_SIZE, len), bc);
        }
        return new IOCInputStream(controller, offset, length);
    }

    public long getChildPosition(InputStream child) {
        if(child instanceof IOCInputStream) {
            IOCInputStream iocis = (IOCInputStream) child;
            return iocis.getPosition();
        }
        return -1;
    }

    public long getChildOffset(InputStream child) {
        if(child instanceof IOCInputStream) {
            IOCInputStream iocis = (IOCInputStream) child;
            return iocis.getOffset();
        }
        return -1;
    }

    public void setChildPosition(InputStream child, long position) {
        if (child instanceof IOCInputStream) {
            IOCInputStream iocis = (IOCInputStream) child;
            iocis.seek(position);
        }
    }

    public OutputStream createOutputStream(long offset) {
        if (controller == null) {
            Content bc = new RandomAccessFileContent(in);
            controller = new IOController(UIOStreamBuilder.DEFAULT_CHUNK_SIZE, bc);
        }
        return new IOCOutputStream(controller, offset);
    }
}
