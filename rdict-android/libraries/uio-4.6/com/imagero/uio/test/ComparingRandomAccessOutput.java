package com.imagero.uio.test;

import com.imagero.uio.RandomAccessOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Trace method calls and their results.
 *
 * Date: 29.02.2008
 * @author Andrey Kuznetsov
 */
public class ComparingRandomAccessOutput implements RandomAccessOutput {

    RandomAccessOutput r0;
    RandomAccessOutput r1;

    public ComparingRandomAccessOutput(RandomAccessOutput ra1, RandomAccessOutput ra2) {
        this.r0 = ra1;
        this.r1 = ra2;
    }

    public RandomAccessOutput createOutputChild(long offset, int byteOrder, boolean syncPointer) throws IOException {
        RandomAccessOutput c0 = r0.createOutputChild(offset, byteOrder, syncPointer);
        RandomAccessOutput c1 = r1.createOutputChild(offset, byteOrder, syncPointer);
        return new ComparingRandomAccessOutput(c0, c1);
    }

    public OutputStream createOutputStream(long offset) {
        OutputStream out = r0.createOutputStream(offset);
        return out;
    }

    public void flush() throws IOException {
        r0.flush();
        r1.flush();
    }

    public void write(int b) throws IOException {
        r0.write(b);
        r1.write(b);
    }

    public void write(byte b[]) throws IOException {
        r0.write(b);
        r1.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        r0.write(b, off, len);
        r1.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        r0.writeBoolean(v);
        r1.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        r0.writeByte(v);
        r1.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        r0.writeShort(v);
        r1.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        r0.writeChar(v);
        r1.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        r0.writeInt(v);
        r1.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        r0.writeLong(v);
        r1.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        r0.writeFloat(v);
        r1.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        r0.writeDouble(v);
        r1.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        r0.writeBytes(s);
        r1.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        r0.writeChars(s);
        r1.writeChars(s);
   }

    public void writeUTF(String s) throws IOException {
        r0.writeUTF(s);
        r1.writeUTF(s);
    }

    public void writeShort(int v, int byteOrder) throws IOException {
        r0.writeShort(v, byteOrder);
        r1.writeShort(v, byteOrder);
    }

    public void writeChar(int v, int byteOrder) throws IOException {
        r0.writeChar(v, byteOrder);
        r1.writeChar(v, byteOrder);
   }

    public void writeInt(int v, int byteOrder) throws IOException {
        r0.writeInt(v, byteOrder);
        r1.writeInt(v, byteOrder);
    }

    public void writeLong(long v, int byteOrder) throws IOException {
        r0.writeLong(v, byteOrder);
        r1.writeLong(v, byteOrder);
    }

    public void writeFloat(float v, int byteOrder) throws IOException {
        r0.writeFloat(v, byteOrder);
        r1.writeFloat(v, byteOrder);
    }

    public void writeDouble(double v, int byteOrder) throws IOException {
        r0.writeDouble(v, byteOrder);
        r1.writeDouble(v, byteOrder);
   }

    public void setLength(long newLength) throws IOException {
        r0.setLength(newLength);
        r1.setLength(newLength);
    }

    public int getByteOrder() {
        int b0 = r0.getByteOrder();
        int b1 = r1.getByteOrder();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public void close() throws IOException {
        r0.close();
        r1.close();
    }

    public void seek(long offset) throws IOException {
        r0.seek(offset);
        r1.seek(offset);
    }

    public long getFilePointer() throws IOException {
        long p0 = r0.getFilePointer();
        long p1 = r1.getFilePointer();
        Assertions.assertEquals(p0, p1);
        return p0;
    }

    public void setByteOrder(int byteOrder) {
        r0.setByteOrder(byteOrder);
        r1.setByteOrder(byteOrder);
    }

    public long length() throws IOException {
        long k0 = r0.length();
        long k1 = r1.length();
        Assertions.assertEquals(k0, k1);
        return k0;
    }
}
