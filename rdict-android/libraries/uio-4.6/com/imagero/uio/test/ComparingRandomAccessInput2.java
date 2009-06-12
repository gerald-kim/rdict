package com.imagero.uio.test;

import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.impl.OffsetRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Compare input from RandomAccessFile and RandomAccessInput.
 *
 * Date: 29.02.2008
 * @author Andrey Kuznetsov
 */
public class ComparingRandomAccessInput2 implements RandomAccessInput {

    File f;
    RandomAccessFile r0;
    RandomAccessInput r1;

    public ComparingRandomAccessInput2(File f, RandomAccessInput ra2) throws IOException {
        this.f = f;
        this.r0 = new RandomAccessFile(f, "r");
        this.r1 = ra2;
    }

    protected ComparingRandomAccessInput2(File f, RandomAccessFile ra1,  RandomAccessInput ra2) throws IOException {
        this.f = f;
        this.r0 = ra1;
        this.r1 = ra2;
    }

    public RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException {
        RandomAccessFile c0 = new OffsetRandomAccessFile(f, "r", offset);
        RandomAccessInput c1 = r1.createInputChild(offset, 0, byteOrder, syncPointer);
        return new ComparingRandomAccessInput2(f, c0, c1);
    }

    public InputStream createInputStream(long offset) {
        InputStream in = r1.createInputStream(offset);
        return in;
    }

    public InputStream createInputStream(long offset, long length) {
        InputStream in = r1.createInputStream(offset, length);
        return in;
    }

    public long getChildPosition(InputStream child) {
        return r1.getChildPosition(child);
    }

    public long getChildOffset(InputStream child) {
        return r1.getChildOffset(child);
    }

    public void setChildPosition(InputStream child, long position) {
        r1.setChildPosition(child, position);
    }

    public int read() throws IOException {
        int a0 = r0.read();
        int a1 = r1.read();
        Assertions.assertEquals(a0, a1);
        return a0;
    }

    public long skip(long n) throws IOException {
        r0.seek(r0.getFilePointer() + n);
        long s0 = r0.getFilePointer();
        long s1 = r1.skip(n);
        Assertions.assertEquals(s0, s1);
        return s0;
    }

    public int read(byte[] b0) throws IOException {
        byte [] b1 = new byte[b0.length];
        int k0 = r0.read(b0);
        int k1 = r1.read(b1);
        Assertions.assertEquals(k0, k1);
        Assertions.assertEquals(b0, b1);
        return k0;
    }

    public int read(byte[] b0, int off, int len) throws IOException {
        byte[] b1 = new byte[b0.length];
        int k0 = r0.read(b0, off, len);
        int k1 = r1.read(b1, off, len);
        Assertions.assertEquals(k0, k1);
        Assertions.assertEquals(off, b0, b1);
        return k0;
    }

    public void seek(long offset) throws IOException {
        r0.seek(offset);
        r1.seek(offset);
    }

    public void close() throws IOException {
        r0.close();
        r1.close();
    }

    public int getByteOrder() {
        int b1 = r1.getByteOrder();
        Assertions.assertEquals(BIG_ENDIAN, b1);//we need BIG_ENDIAN
        return b1;
    }

    public void setByteOrder(int byteOrder) {
        r1.setByteOrder(byteOrder);
    }

    public long getFilePointer() throws IOException {
        long fp0 = r0.getFilePointer();
        long fp1 = r1.getFilePointer();
        Assertions.assertEquals(fp0, fp1);
        return fp0;
    }

    public long length() throws IOException {
        long e0 = r0.length();
        long e1 = r1.length();
        Assertions.assertEquals(e0, e1);
        return e0;
    }

    public boolean isBuffered() {
        boolean b1 = r1.isBuffered();
        return b1;
    }

    public void readFully(byte b0[]) throws IOException {
        getFilePointer();
        r0.readFully(b0);
        byte [] b1 = new byte[b0.length];
        r1.readFully(b1);
        getFilePointer();
        Assertions.assertEquals(b0, b1);
    }

    public void readFully(byte b0[], int off, int len) throws IOException {
        getFilePointer();
        r0.readFully(b0, off, len);
        byte[] b1 = new byte[b0.length];
        r1.readFully(b1, off, len);
        getFilePointer();
        Assertions.assertEquals(off, len, b0, b1);
    }

    public int skipBytes(int n) throws IOException {
        getFilePointer();
        int s0 = r0.skipBytes(n);
        int s1 = r1.skipBytes(n);
        getFilePointer();
        Assertions.assertEquals(s0, s1);
        return s0;
    }

    public boolean readBoolean() throws IOException {
        getFilePointer();
        boolean b0 = r0.readBoolean();
        boolean b1 = r1.readBoolean();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public byte readByte() throws IOException {
        getFilePointer();
        byte b0 = r0.readByte();
        byte b1 = r1.readByte();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public int readUnsignedByte() throws IOException {
        getFilePointer();
        int b0 = r0.readUnsignedByte();
        int b1 = r1.readUnsignedByte();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public short readShort() throws IOException {
        getFilePointer();
        short b0 = r0.readShort();
        short b1 = r1.readShort();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public int readUnsignedShort() throws IOException {
        getFilePointer();
        int b0 = r0.readUnsignedShort();
        int b1 = r1.readUnsignedShort();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public char readChar() throws IOException {
        getFilePointer();
        char b0 = r0.readChar();
        char b1 = r1.readChar();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public int readInt() throws IOException {
        getFilePointer();
        int b0 = r0.readInt();
        int b1 = r1.readInt();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public long readLong() throws IOException {
        getFilePointer();
        long b0 = r0.readLong();
        long b1 = r1.readLong();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public float readFloat() throws IOException {
        getFilePointer();
        float b0 = r0.readFloat();
        float b1 = r1.readFloat();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public double readDouble() throws IOException {
        getFilePointer();
        double b0 = r0.readDouble();
        double b1 = r1.readDouble();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public String readLine() throws IOException {
        getFilePointer();
        String s0 = r0.readLine();
        String s1 = r1.readLine();
        getFilePointer();
        Assertions.assertEquals(s0, s1);
        return s0;
    }

    public String readUTF() throws IOException {
        getFilePointer();
        String s0 = r0.readUTF();
        String s1 = r1.readUTF();
        getFilePointer();
        Assertions.assertEquals(s0, s1);
        return s0;
    }

    public byte[] readByteLine() throws IOException {
        getFilePointer();
//        byte [] s0 = r0.readByteLine();
        byte [] s1 = r1.readByteLine();
        r0.seek(r1.getFilePointer());//can't compare - ergo don't read, just move file pointer
        getFilePointer();
//        Assertions.assertEquals(s0, s1);
        return s1;
    }

    public int readByteLine(byte[] d0) throws IOException {
        getFilePointer();
//        int s0 = r0.readByteLine(d0);
        byte [] d1 = new byte[d0.length];
        int s1 = r1.readByteLine(d1);
        r0.seek(r1.getFilePointer());//can't compare - ergo don't read, just move file pointer
        getFilePointer();
//        Assertions.assertEquals(s0, s1);
//        Assertions.assertEquals(0, s0, d0, d1);
        return s1;
    }

    public short readShort(int byteOrder) throws IOException {
        getFilePointer();
        short b0 = r0.readShort();
        short b1 = r1.readShort(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }

    public int readUnsignedShort(int byteOrder) throws IOException {
        getFilePointer();
        int b0 = r0.readUnsignedShort();
        int b1 = r1.readUnsignedShort(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }

    public char readChar(int byteOrder) throws IOException {
        getFilePointer();
        char b0 = r0.readChar();
        char b1 = r1.readChar(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }

    public int readInt(int byteOrder) throws IOException {
        getFilePointer();
        int b0 = r0.readInt();
        int b1 = r1.readInt(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }

    public long readLong(int byteOrder) throws IOException {
        getFilePointer();
        long b0 = r0.readLong();
        long b1 = r1.readLong(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }

    public float readFloat(int byteOrder) throws IOException {
        getFilePointer();
        float b0 = r0.readFloat();
        float b1 = r1.readFloat(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }

    public double readDouble(int byteOrder) throws IOException {
        getFilePointer();
        double b0 = r0.readDouble();
        double b1 = r1.readDouble(byteOrder);
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public long readUnsignedInt() throws IOException {
        getFilePointer();
        long b0 = ((long) r0.readInt()) & 0xFFFFFFFFL;
        long b1 = r1.readUnsignedInt();
        getFilePointer();
        Assertions.assertEquals(b0, b1);
        return b0;
    }

    public long readUnsignedInt(int byteOrder) throws IOException {
        getFilePointer();
        long b0 = r0.readInt();
        long b1 = r1.readUnsignedInt(byteOrder);
        getFilePointer();
//        Assertions.assertEquals(b0, b1); //don't compare
        return b0;
    }
}
