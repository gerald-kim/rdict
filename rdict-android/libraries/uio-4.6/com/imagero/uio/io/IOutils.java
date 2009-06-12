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

package com.imagero.uio.io;

import com.imagero.uio.*;

import java.io.*;

/**
 * IOutils.java
 *
 * @author Andrei Kouznetsov
 */
public class IOutils {
    private static final int BIG_ENDIAN = 0x4D4D;
    private static final int LITTLE_ENDIAN = 0x4949;

    /**
     * close silently stream<br>
     * no exception it thrown
     *
     * @param bw
     */
    public static void closeStream(BufferedWriter bw) {
        try {
            if (bw != null) {
                bw.close();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * close silently stream<br>
     * no exception it thrown
     *
     * @param br
     */
    public static void closeStream(BufferedReader br) {
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * close silently stream<br>
     * no exception it thrown
     *
     * @param is
     */
    public static void closeStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * close silently stream<br>
     * no exception it thrown
     *
     * @param os
     */
    public static void closeStream(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * close silently stream<br>
     * no exception it thrown
     *
     * @param raf
     */
    public static void closeStream(RandomAccessFile raf) {
        try {
            if (raf != null) {
                raf.close();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * close silently stream<br>
     * no exception it thrown
     *
     * @param ro
     */
    public static void closeStream(RandomAccessInput ro) {
        try {
            if (ro != null) {
                ro.close();
            }
        } catch (IOException ex) {
        }
    }

    public static void closeStream(RandomAccessOutput ro) {
        try {
            if (ro != null) {
                ro.close();
            }
        } catch (IOException ex) {
        }
    }

    static final int[] mask = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768};
    static byte b0 = (byte) '0';
    static byte b1 = (byte) '1';

    public static String toBinaryString(byte value) {
        byte[] b = new byte[8];
        int cnt = 0;
        for (int i = 7; i > -1; i--) {
            b[cnt++] = (value & mask[i]) == 0 ? b0 : b1;
        }
        return new String(b);
    }

    public static String toBinaryString(char value) {
        byte[] b = new byte[16];
        int cnt = 0;
        for (int i = 15; i > -1; i--) {
            b[cnt++] = (value & mask[i]) == 0 ? b0 : b1;
        }
        return new String(b);
    }

    public static String toBinaryString(int value, int length) {
        byte[] b = new byte[length];
        int cnt = 0;
        for (int i = length - 1; i > -1; i--) {
            if (((value >> i) & 1) == 1) {
                b[cnt++] = b1;
            } else {
                b[cnt++] = b0;
            }
        }
        return new String(b);
    }

    final static byte[] digits = {
        (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
        (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
        (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    public static String toHexString(byte value) {
        return toUnsignedString(value & 0xFF, 4);
    }

    static String toUnsignedString(int i, int shift) {
        byte[] buf = new byte[]{(byte) '0', (byte) '0'};
        int charPos = 2;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[--charPos] = digits[i & mask];
            i >>>= shift;
        } while (i != 0);

        return new String(buf);
    }

    public static void printHexByte(int value) {
        printHexImpl(value & 0xFFFF, 2);
    }

    public static void printlnHexByte(int value) {
        printHexImpl(value & 0xFFFF, 2);
        Sys.out.println("");
    }

    public static void printHexShort(int value) {
        printHexImpl(value & 0xFFFF, 4);
    }

    public static void printlnHexShort(int value) {
        printHexImpl(value & 0xFFFF, 4);
        Sys.out.println("");
    }

    public static void printHexInt(int value) {
        printHexImpl(value & 0xFFFFFFFF, 8);
    }

    public static void printlnHexInt(int value) {
        printHexImpl(value & 0xFFFFFFFF, 8);
        Sys.out.println("");
    }

    public static void printHexLong(long value) {
        printHexImpl(value & 0xFFFFFFFFFFFFFFFFL, 16);
    }

    public static void printlnHexLong(long value) {
        printHexImpl(value & 0xFFFFFFFFFFFFFFFFL, 16);
        Sys.out.println("");
    }

    static void printHexImpl(long value, int length) {
        String s = Long.toHexString(value);
        //Sys.out.println("***********************" + s + " " + value);
        for (int i = 0, size = length - s.length(); i < size; i++) {
            Sys.out.print("0");
        }
        Sys.out.print(s);
    }

    static void printHexImpl(int value, int length) {
        String s = Integer.toHexString(value);
        if (s.length() > length) {
            s = s.substring(s.length() - length);
        }
        //Sys.out.println("***********************" + s + " " + value);
        for (int i = 0, size = length - s.length(); i < size; i++) {
            Sys.out.print("0");
        }
        Sys.out.print(s);
    }

    public static String getExtension(File f) {
        String s = f.getName();
        return s.substring(s.lastIndexOf(".") + 1).toUpperCase();
    }

    /**
     * read little-endian short
     */
    public static int readShort4D(InputStream in) throws IOException {
        return ((in.read() & 0xFF) << 8) + ((in.read() & 0xFF) << 0);
    }

    /**
     * read little-endian short
     */
    public static int readShort4D(DataInput in) throws IOException {
        return ((in.readByte() & 0xFF) << 8) + ((in.readByte() & 0xFF) << 0);
    }

    /**
     * read big-endian short
     */
    public static int readShort49(InputStream in) throws IOException {
        return ((in.read() & 0xFF) << 0) + ((in.read() & 0xFF) << 8);
    }

    /**
     * read big-endian short
     */
    public static int readShort49(DataInput in) throws IOException {
        return ((in.readByte() & 0xFF) << 0) + ((in.readByte() & 0xFF) << 8);
    }

    /**
     * read little-endian int
     */
    public static int readInt4D(InputStream in) throws IOException {
        return ((in.read() & 0xFF) << 24) + ((in.read() & 0xFF) << 16) + ((in.read() & 0xFF) << 8) + ((in.read() & 0xFF) << 0);
    }

    public static long readUnsignedInt4D(InputStream in) throws IOException {
        long l = ((in.read() & 0xFF) << 24);
        return l + ((in.read() & 0xFF) << 16) + ((in.read() & 0xFF) << 8) + ((in.read() & 0xFF) << 0);
    }

    /**
     * read big-endian int
     */
    public static int readInt4D(DataInput in) throws IOException {
        int b0 = (in.readByte() & 0xFF);
        int b1 = (in.readByte() & 0xFF);
        int b2 = (in.readByte() & 0xFF);
        int b3 = (in.readByte() & 0xFF);
        return (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0);
    }

    public static long readUnsignedInt4D(DataInput in) throws IOException {
        long b0 = in.readUnsignedByte();
        long b1 = in.readUnsignedByte();
        long b2 = in.readUnsignedByte();
        long b3 = in.readUnsignedByte();
        return (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0);
    }

    /**
     * read little-endian int
     */
    public static int readInt49(InputStream in) throws IOException {
        int b0 = in.read();
        int b1 = in.read();
        int b2 = in.read();
        int b3 = in.read();
        if((b3 < 0) || (b2 < 0) || (b1 < 0) || (b0 < 0)) {
            throw new EOFException();
        }
        return (b0 << 0) + (b1 << 8) + (b2 << 16) + (b3 << 24);
    }

    public static long readUnsignedInt49(InputStream in) throws IOException {
        long b0 = in.read();
        long b1 = in.read();
        long b2 = in.read();
        long b3 = in.read();
        if((b3 < 0) || (b2 < 0) || (b1 < 0) || (b0 < 0)) {
            throw new EOFException();
        }
        return (b0 << 0) + (b1 << 8) + (b2 << 16) + (b3 << 24);
    }

    /**
     * read little-endian int
     */
    public static int readInt49(DataInput in) throws IOException {
        int b0 = in.readUnsignedByte();
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        return (b0 << 0) + (b1 << 8) + (b2 << 16) + (b3 << 24);
    }

    /**
     * read unsigned little-endian int
     */
    public static long readUnsignedInt49(DataInput in) throws IOException {
        long b0 = in.readUnsignedByte();
        long b1 = in.readUnsignedByte();
        long b2 = in.readUnsignedByte();
        long b3 = in.readUnsignedByte();
        return (b0 << 0) + (b1 << 8) + (b2 << 16) + (b3 << 24);
    }

    /**
     * read big-endian long
     */
    public static long readLong4D(InputStream in) throws IOException {
        long b0 = in.read();
        long b1 = in.read();
        long b2 = in.read();
        long b3 = in.read();
        long b4 = in.read();
        long b5 = in.read();
        long b6 = in.read();
        long b7 = in.read();
        if((b7 < 0) || (b6 < 0) || (b5 < 0) || (b4 < 0) || (b3 < 0) || (b2 < 0) || (b1 < 0) || (b0 < 0)) {
            throw new EOFException();
        }
        return (b0 << 56) + (b1 << 48) + (b2 << 40) + (b3 << 32) | (b4 << 24) + (b5 << 16) + (b6 << 8) + (b7 << 0);
    }

    /**
     * read big-endian long
     */
    public static long readLong4D(DataInput in) throws IOException {
        long b0 = in.readUnsignedByte();
        long b1 = in.readUnsignedByte();
        long b2 = in.readUnsignedByte();
        long b3 = in.readUnsignedByte();
        long b4 = in.readUnsignedByte();
        long b5 = in.readUnsignedByte();
        long b6 = in.readUnsignedByte();
        long b7 = in.readUnsignedByte();
        return (b0 << 56) + (b1 << 48) + (b2 << 40) + (b3 << 32) | (b4 << 24) + (b5 << 16) + (b6 << 8) + (b7 << 0);
    }

    /**
     * read little-endian long
     */
    public static long readLong49(InputStream in) throws IOException {
        long b0 = in.read();
        long b1 = in.read();
        long b2 = in.read();
        long b3 = in.read();
        long b4 = in.read();
        long b5 = in.read();
        long b6 = in.read();
        long b7 = in.read();
        if((b7 < 0) || (b6 < 0) || (b5 < 0) || (b4 < 0) || (b3 < 0) || (b2 < 0) || (b1 < 0) || (b0 < 0)) {
            throw new EOFException();
        }
        return (b0 << 0) + (b1 << 8) + (b2 << 16) + (b3 << 24) | (b4 << 32) + (b5 << 40) + (b6 << 48) + (b7 << 56);
    }

    /**
     * read little-endian long
     */
    public static long readLong49(DataInput in) throws IOException {
        long b0 = in.readUnsignedByte();
        long b1 = in.readUnsignedByte();
        long b2 = in.readUnsignedByte();
        long b3 = in.readUnsignedByte();
        long b4 = in.readUnsignedByte();
        long b5 = in.readUnsignedByte();
        long b6 = in.readUnsignedByte();
        long b7 = in.readUnsignedByte();
        return (b0 << 0) + (b1 << 8) + (b2 << 16) + (b3 << 24) | (b4 << 32) + (b5 << 40) + (b6 << 48) + (b7 << 56);
    }

    public static byte readSByte(DataInput ro) throws IOException {
        byte b = ro.readByte();
        if (b < 0) {
            b = (byte) -(~(b + 1));
        }
        return b;
    }

    public static short readSShort(DataInput ro) throws IOException {
        short b = ro.readShort();
        if (b < 0) {
            b = (short) -(~(b + 1));
        }
        return b;
    }

    public static int readSInt(DataInput ro) throws IOException {
        int b = ro.readInt();
        if (b < 0) {
            b = -(~(b + 1));
        }
        return b;
    }

    public static long readSLong(DataInput ro) throws IOException {
        long b = ro.readLong();
        if (b < 0) {
            b = -(~(b + 1));
        }
        return b;
    }

    /**
     * Read byte array and convert from 2's complement
     * @param ro RandomAccessRO
     * @param b0 byte array
     * @throws IOException
     */
    public static void readFullyS(DataInput ro, byte[] b0) throws IOException {
        ro.readFully(b0);
        convertFrom2C(b0);
    }

    /**
     * Convert byte array from 2's complement
     */
    public static final void convertFrom2C(byte[] b0) {
        for (int i = 0; i < b0.length; i++) {
            if (b0[i] < 0) {
                b0[i] = (byte) -(~(b0[i] + 1));
            }
        }
    }

    /**
     * Read short array and convert from 2's complement.
     * @param ro RandomAccessRO
     * @param b0 short array
     * @throws IOException
     */
    public static void readFullyS(RandomAccessInput ro, short[] b0) throws IOException {
        ReadUtil.readFully(ro, b0);
        convertFrom2C(b0);
    }

    /**
     * Convert short array from 2's complement
     */
    public static final void convertFrom2C(short[] b0) {
        for (int i = 0; i < b0.length; i++) {
            if (b0[i] < 0) {
                b0[i] = (short) -(~(b0[i] + 1));
            }
        }
    }

    /**
     * Read int array and convert from 2's complement
     * @param ro RandomAccessRO
     * @param b0 int array
     * @throws IOException
     */
    public static void readFullyS(RandomAccessInput ro, int[] b0) throws IOException {
        ReadUtil.readFully(ro, b0);
        convertFrom2C(b0);
    }

    /**
     * Convert int array from 2's complement
     */
    public static final void convertFrom2C(int[] b0) {
        for (int i = 0; i < b0.length; i++) {
            if (b0[i] < 0) {
                b0[i] = -(~(b0[i] + 1));
            }
        }
    }

    /**
     * Read short array and convert from 2's complement
     * @param ro RandomAccessRO
     * @param b0 long array
     * @throws IOException
     */
    public static void readFullyS(RandomAccessInput ro, long[] b0) throws IOException {
        ReadUtil.readFully(ro, b0);
        convertFrom2C(b0);
    }

    /**
     * Convert long array from 2's complement
     */
    public static final void convertFrom2C(long[] b0) {
        for (int i = 0; i < b0.length; i++) {
            if (b0[i] < 0) {
                b0[i] = -(~(b0[i] + 1));
            }
        }
    }

    public static void readFully(InputStream in, byte b[]) throws UnexpectedEOFException, IOException {
        readFully(in, b, 0, b.length);
    }

    public static void readFully(InputStream in, byte b[], int off, int len) throws UnexpectedEOFException, IOException {
        int n = 0;
        do {
            int count = in.read(b, off + n, len - n);
            if (count <= 0) {
                throw new UnexpectedEOFException(n > 0 ? n : 0);
            }
            n += count;
        } while (n < len);
    }

    /**
     * this method is like readFully, but instead of throwing <code>EOFException</code> it returns count of read bytes
     *
     * @param in InputStream to read
     * @param b  byte array to fill
     *
     * @return number of bytes read into the buffer, or -1 if EOF was reached
     *
     * @throws IOException
     *
     */
    public static int readFully2(InputStream in, byte b[]) throws IOException {
        return readFully2(in, b, 0, b.length);
    }

    /**
     * this method is like readFully, but instead of throwing <code>EOFException</code> it returns count of read bytes
     *
     * @param in  InputStream to read
     * @param b   byte array to fill
     * @param off start offset in byte array
     * @param len number of bytes to read
     *
     * @return number of bytes read into the buffer, or -1 if EOF was reached
     *
     * @throws IOException
     */
    public static int readFully2(InputStream in, byte b[], int off, int len) throws IOException {
        int n = 0;
        do {
            int read = in.read(b, off + n, len - n);
            if (read <= 0) {
                return n == 0 ? -1 : n;
            }
            n += read;
        } while (n < len);
        return n;
    }

    /**
     * copy <code>length</code> bytes from <code>in</code> to <code>out</code>
     * @param length amount of bytes to copy
     * @param in source InputStream
     * @param out destination OutputStream
     * @throws IOException
     */
    public static long copy(long length, InputStream in, OutputStream out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        while (length > 0) {
            int read = in.read(buffer, 0, (int) Math.min(buffer.length, length));
            if (read <= 0) {
                break;
            }
            copy += read;
            length -= read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * copy <code>length</code> bytes from source to destination stream
     * @param length amount of bytes to copy
     * @param in source stream
     * @param out destination stream
     * @throws IOException
     */
    public static long copy(long length, InputStream in, DataOutput out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        while (length > 0) {
            int read = in.read(buffer, 0, (int) Math.min(buffer.length, length));
            if (read <= 0) {
                break;
            }
            copy += read;
            length -= read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * Copy file.
     * @param src source file
     * @param dest destination file
     * @return how much bytes were copied.
     * @throws IOException
     */
    public static long copy(File src, File dest) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);
        try {
            return copy(in, out);
        } finally {
            IOutils.closeStream(in);
            IOutils.closeStream(out);
        }
    }

    /**
     * copy data from <code>in</code> to <code>out</code>
     * @param in source InputStream
     * @param out destination OutputStream
     * @return how much bytes were copied.
     * @throws IOException
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        while (true) {
            int read = in.read(buffer);
            if (read <= 0) {
                break;
            }
            copy += read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    public static long copy(InputStream in, OutputStream out, long count) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        while (true) {
            int read = in.read(buffer, 0, (int) Math.min(buffer.length, count) & Integer.MAX_VALUE);
            if (read <= 0) {
                break;
            }
            count -= read;
            copy += read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    public static long copy(InputStream in, DataOutput out, long count) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        while (true) {
            int read = in.read(buffer, 0, (int) Math.min(buffer.length, count) & Integer.MAX_VALUE);
            if (read <= 0) {
                break;
            }
            count -= read;
            copy += read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * copy data from source to destination stream
     * @param in source stream
     * @param out destination stream
     * @throws IOException
     * @return amount of copied bytes
     */
    public static long copy(InputStream in, DataOutput out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        while (true) {
            int read = in.read(buffer);
            if (read <= 0) {
                break;
            }
            copy += read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * copy data from <code>in</code> to <code>out</code>
     * @param in source RandomAccessRO
     * @param offset offset in <code>in</code>
     * @param out destination OutputStream
     * @throws IOException
     */
    public static long copy(RandomAccessInput in, long offset, OutputStream out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        in.seek(offset);
        while (true) {
            int read = in.read(buffer);
            if (read <= 0) {
                break;
            }
            copy += read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * copy data from source to destination stream
     * @param in source stream
     * @param offset offset in source stream
     * @param out destination stream
     * @throws IOException
     * @return how much bytes was copied
     */
    public static long copy(RandomAccessInput in, long offset, DataOutput out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        in.seek(offset);
        while (true) {
            int read = in.read(buffer);
            if (read <= 0) {
                break;
            }
            copy += read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * copy data from source to destination stream
     * @param offset offset in source stream
     * @param length amount of bytes to copy
     * @param in source stream
     * @param out destination stream
     * @throws IOException
     */
    public static long copy(long offset, long length, RandomAccessInput in, OutputStream out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048];
        in.seek(offset);
        while (length > 0) {
            int read = in.read(buffer, 0, (int) Math.min(buffer.length, length));
            if (read <= 0) {
                break;
            }
            copy += read;
            length -= read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * copy data from source to destination stream
     * @param offset offset in source stream
     * @param length amount of bytes to copy
     * @param in source stream
     * @param out destination stream
     * @throws IOException
     */
    public static long copy(long offset, long length, RandomAccessInput in, DataOutput out) throws IOException {
        long copy = 0;
        byte[] buffer = new byte[2048 * 10];
        in.seek(offset);
        while (length > 0) {
            int read = in.read(buffer, 0, (int) Math.min(buffer.length, length));
            if (read <= 0) {
                break;
            }
            copy += read;
            length -= read;
            out.write(buffer, 0, read);
        }
        return copy;
    }

    /**
     * read big/little-endian short
     */
    public static int readShort(InputStream in, int byteOrder) throws IOException {
        switch (byteOrder) {
            case BIG_ENDIAN:
                return readShort4D(in);
            case LITTLE_ENDIAN:
                return readShort49(in);
            default:
                throw new IllegalArgumentException("" + byteOrder);
        }
    }

    public static int readShort(DataInput in, int byteOrder) throws IOException {
        switch (byteOrder) {
            case BIG_ENDIAN:
                return readShort4D(in);
            case LITTLE_ENDIAN:
                return readShort49(in);
            default:
                throw new IllegalArgumentException("" + byteOrder);
        }
    }

    /**
     * read big/little-endian int
     */
    public static int readInt(InputStream in, int byteOrder) throws IOException {
        switch (byteOrder) {
            case BIG_ENDIAN:
                return readInt4D(in);
            case LITTLE_ENDIAN:
                return readInt49(in);
            default:
                throw new IllegalArgumentException("" + byteOrder);
        }
    }

    /**
     * read big/little-endian int
     */
    public static int readInt(DataInput in, int byteOrder) throws IOException {
        switch (byteOrder) {
            case BIG_ENDIAN:
                return readInt4D(in);
            case LITTLE_ENDIAN:
                return readInt49(in);
            default:
                throw new IllegalArgumentException("" + byteOrder);
        }
    }

    public static String readFile(File f) throws IOException {
        StringBuffer sb = new StringBuffer();
        InputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            readFile(reader, sb);
        } finally {
            reader.close();
        }
        return sb.toString();
    }

    public static StringBuffer readFileSB(File f) throws IOException {
        InputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        try {
            readFile(reader, sb);
        } finally {
            reader.close();
        }
        return sb;
    }

    public static void readFile(BufferedReader reader, StringBuffer sb) throws IOException {
        String line = null;
        do {
            line = reader.readLine();
            if (line != null) {
                sb.append(line);
                sb.append("\n");
            }
        } while (line != null);
    }

    public static String readUTF(byte[] data) throws IOException {
        return readUTF(data, 0, data.length);
    }

    public static String readUTF(byte[] data, int offset, int length) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(bout);

        int first = offset;
        int last = offset + length;
        while (first < last) {
            int c = (int) data[first++] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    bos.write(c, 16);
                    break;
                case 12:
                case 13:
                    first += 1;
                    bos.write(c & 0x1F, 10);
                    bos.write(data[first - 1], 6);
                    break;
                case 14:
                    first += 2;
                    bos.write(c & 0x0F, 4);
                    bos.write(data[first - 2] & 0x3F, 6);
                    bos.write(data[first - 1] & 0x3F, 6);
                    break;
                default:
                    throw new UTFDataFormatException();
            }
        }
        bos.flush();
        byte[] bytes = bout.toByteArray();
        char[] dest = new char[bytes.length / 2];
        Transformer.byteToChar(bytes, 0, bytes.length / 2, dest, 0, true);
        return new String(dest);
    }
}
