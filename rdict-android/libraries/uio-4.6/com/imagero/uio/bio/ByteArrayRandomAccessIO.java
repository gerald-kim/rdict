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
package com.imagero.uio.bio;

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.RandomAccessOutput;
import com.imagero.uio.impl.AbstractRandomAccessIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ByteArrayRandomAccessIO is like ByteArrayOutputStream and ByteArrayInputStream together.
 * It implements also DataInput/DataOutput and other advanced interfaces.
 * @author Andrey Kuznetsov
 */
public class ByteArrayRandomAccessIO extends AbstractRandomAccessIO implements RandomAccessIO {

    VariableSizeByteBuffer buffer;
    BufferPosition position;
    int _offset;
    Integer length;

    private static VariableSizeByteBuffer createBuffer(int size) {
        return new VariableSizeByteBuffer(size);
    }

    private static VariableSizeByteBuffer createBuffer(byte [] data) {
        return new VariableSizeByteBuffer(data);
    }
    public ByteArrayRandomAccessIO(int initialSize) {
        this(createBuffer(initialSize));
    }

    public ByteArrayRandomAccessIO(int offset, int length, VariableSizeByteBuffer buffer) {
        this._offset = offset;
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
        position.pos = offset;
        if(length > 0) {
            this.length = new Integer(length);
        }
    }

    public ByteArrayRandomAccessIO(byte [] data) {
        this(createBuffer(data));
    }

    public ByteArrayRandomAccessIO(VariableSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
    }

    public int read() throws IOException {
        return buffer.read(position);
    }

    public long skip(long n) throws IOException {
        return buffer.skip(n, position);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return buffer.read(b, off, len, position);
    }

    public long getFilePointer() throws IOException {
        return position.pos - _offset;
    }

    public long length() throws IOException {
        if(length != null) {
            return Math.min(length.intValue(), buffer.getCount() - _offset);
        }
        else {
            return buffer.getCount() - _offset;
        }
    }

    public void seek(long offset) throws IOException {
        if (offset + _offset > Integer.MAX_VALUE) {
            throw new IOException("Offset too big: 0x" + Long.toHexString(offset));
        }
        buffer.seek((int) offset + _offset, position);
    }

    public void setLength(long newLength) throws IOException {
        if (newLength > Integer.MAX_VALUE) {
            throw new IOException();
        }
        buffer.setCount((int) newLength);
    }

    public void write(int b) throws IOException {
        buffer.write(b, position);
    }

    public void write(byte b[], int offset, int length) throws IOException {
        buffer.write(b, offset, length, position);
    }

    public RandomAccessIO createIOChild(long offset, long length, int byteOrder, boolean syncPointer) {
        ByteArrayRandomAccessIO io = new ByteArrayRandomAccessIO((int) offset, (int) length, buffer);
        if(syncPointer) {
            io.buffer = buffer;
        }
        io.setByteOrder(byteOrder);
        return io;
    }

    public RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) {
        return createIOChild(offset, length, byteOrder, syncPointer);
    }

    public RandomAccessOutput createOutputChild(long offset, int byteOrder, boolean syncPointer) {
        return createIOChild(offset, 0, byteOrder, syncPointer);
    }

    public byte [] toByteArray() throws IOException {
        byte [] b = new byte[(int)length()];
        int pos = position.pos;
        buffer.seek(0, position);
        buffer.read(b, 0, b.length, position);
        buffer.seek(pos, position);
        return b;
    }

    public InputStream createInputStream(long offset) {
        return buffer.getInputStream((int) offset);
    }

    public InputStream createInputStream(long offset, long length) {
        return buffer.getInputStream((int) offset, (int) length);
    }

    public long getChildPosition(InputStream child) {
        if(child instanceof VSBInputStream) {
            VSBInputStream vsbis = (VSBInputStream) child;
            return vsbis.getPosition();
        }
        return -1;
    }

    public long getChildOffset(InputStream child) {
        if(child instanceof VSBInputStream) {
            VSBInputStream vsbis = (VSBInputStream) child;
            return vsbis.getOffset();
        }
        return -1;
    }

    public void setChildPosition(InputStream child, long pos) {
        if (child instanceof VSBInputStream) {
            VSBInputStream vsbis = (VSBInputStream) child;
            vsbis.setPosition(pos);
        }
    }

    public OutputStream createOutputStream(long offset) {
        return buffer.getOutputStream((int) offset);
    }
}
