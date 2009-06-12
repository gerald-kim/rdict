package com.imagero.uio.test;

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessOutput;
import com.imagero.uio.test.ComparingRandomAccessInput;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Trace method calls and their results.
 *
 * Date: 29.02.2008
 * @author Andrey Kuznetsov
 */
public class ComparingRandomAccessIO extends ComparingRandomAccessInput implements RandomAccessIO {

    RandomAccessIO r0;
    RandomAccessIO r1;

    public ComparingRandomAccessIO(RandomAccessIO ra1, RandomAccessIO ra2) {
        super(ra1, ra2);
        this.r0 = ra1;
        this.r1 = ra2;
    }

    public RandomAccessIO createIOChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException {
        RandomAccessIO c0 = r0.createIOChild(offset, 0, byteOrder, syncPointer);
        RandomAccessIO c1 = r1.createIOChild(offset, 0, byteOrder, syncPointer);
        return new ComparingRandomAccessIO(c0, c1);
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
}
