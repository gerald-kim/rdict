package com.imagero.uio.bio;

import com.imagero.uio.impl.AbstractRandomAccessIO;
import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.RandomAccessOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
class FSBRandomAccessIO extends AbstractRandomAccessIO {

    FixedSizeByteBuffer buffer;
    BufferPosition position;

    int offset;
    int length;

    public FSBRandomAccessIO(FixedSizeByteBuffer buffer) {
        this(buffer, 0, buffer.buf.length);
    }

    public FSBRandomAccessIO(FixedSizeByteBuffer buffer, int offset) {
        this(buffer, offset, buffer.buf.length - offset);
    }

    public FSBRandomAccessIO(FixedSizeByteBuffer buffer, int offset, int length) {
        this.buffer = buffer;
        this.offset = offset;
        if(length > 0) {
            this.length = length;
        }
        else {
            this.length = buffer.availableForReading(buffer.createPosition());
        }
    }

    public int read() throws IOException {
        return buffer.read(position);
    }

    public void seek(long pos) {
        position.pos = (int) Math.min(length, pos + offset);
    }

    public long length() throws IOException {
        return length;
    }

    public long getFilePointer() throws IOException {
        return position.pos - offset;
    }

    public void setLength(long newLength) throws IOException {
        this.length = (int) Math.min(buffer.buf.length, newLength);
    }

    public void write(int b) throws IOException {
        buffer.write(b, position);
    }

    public void write(byte b[], int off, int len) throws IOException {
        buffer.write(b, off, len, position);
    }

    public InputStream createInputStream(long offset) {
        return new FSBInputStream((int) offset, buffer);
    }

    public InputStream createInputStream(long offset, long length) {
        return new FSBInputStream((int) offset, buffer, (int) length);
    }

    public long getChildPosition(InputStream child) {
        if(child instanceof FSBInputStream) {
            FSBInputStream fsbis = (FSBInputStream) child;
            return fsbis.getPosition();
        }
        return -1;
    }

    public long getChildOffset(InputStream child) {
        if(child instanceof FSBInputStream) {
            FSBInputStream fsbis = (FSBInputStream) child;
            return fsbis.getOffset();
        }
        return -1;
    }

    public void setChildPosition(InputStream child, long position) {
        if (child instanceof FSBInputStream) {
            FSBInputStream fsbis = (FSBInputStream) child;
            fsbis.setPosition(position);
        }
    }

    public OutputStream createOutputStream(long offset) {
        return new FSBOutputStream((int) offset, buffer) ;
    }

    public RandomAccessIO createIOChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException {
        FSBRandomAccessIO rio = new FSBRandomAccessIO(buffer, (int) offset, (int) length);
        if(syncPointer) {
            rio.position = position;
        }
        rio.setByteOrder(byteOrder);
        return rio;
    }

    public RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException {
        return createIOChild(offset, 0, byteOrder, syncPointer);
    }

    public RandomAccessOutput createOutputChild(long offset, int byteOrder, boolean syncPointer) throws IOException {
        return createIOChild(offset, 0, byteOrder, syncPointer);
    }
}
