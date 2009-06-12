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

import com.imagero.uio.io.UnexpectedEOFException;

import java.io.IOException;
import java.io.DataInput;

/**
 * Methods to fill data in primitive arrays
 *
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
public class ReadUtil {
    public static int read(RandomAccessInput in, short[] dest) throws IOException {
        return read(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, short[] dest, int byteOrder) throws IOException {
        return read(in, dest, 0, dest.length, byteOrder);
    }

    public static int read(RandomAccessInput in, short[] dest, int offset, int length) throws IOException {
        return read(in, dest, offset, length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, char[] dest) throws IOException {
        return read(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, char[] dest, int byteOrder) throws IOException {
        return read(in, dest, 0, dest.length, byteOrder);
    }

    public static int read(RandomAccessInput in, char[] dest, int offset, int length) throws IOException {
        return read(in, dest, offset, length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, int[] dest) throws IOException {
        return read(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, int[] dest, int byteOrder) throws IOException {
        return read(in, dest, 0, dest.length, byteOrder);
    }

    public static int read(RandomAccessInput in, int[] dest, int offset, int length) throws IOException {
        return read(in, dest, offset, length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, long[] dest) throws IOException {
        return read(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, long[] dest, int byteOrder) throws IOException {
        return read(in, dest, 0, dest.length, byteOrder);
    }

    public static int read(RandomAccessInput in, long[] dest, int offset, int length) throws IOException {
        return read(in, dest, offset, length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, float[] dest) throws IOException {
        return read(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, float[] dest, int byteOrder) throws IOException {
        return read(in, dest, 0, dest.length, byteOrder);
    }

    public static int read(RandomAccessInput in, float[] dest, int offset, int length) throws IOException {
        return read(in, dest, offset, length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, double[] dest) throws IOException {
        return read(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static int read(RandomAccessInput in, double[] dest, int byteOrder) throws IOException {
        return read(in, dest, 0, dest.length, byteOrder);
    }

    public static int read(RandomAccessInput in, double[] dest, int offset, int length) throws IOException {
        return read(in, dest, offset, length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, short[] dest) throws IOException {
        readFully(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, short[] dest, int byteOrder) throws IOException {
        readFully(in, dest, 0, dest.length, byteOrder);
    }

    public static void readFully(RandomAccessInput in, short[] dest, int destOffset, int len) throws IOException {
        readFully(in, dest, destOffset, len, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, char[] dest) throws IOException {
        readFully(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, char[] dest, int byteOrder) throws IOException {
        readFully(in, dest, 0, dest.length, byteOrder);
    }

    public static void readFully(RandomAccessInput in, char[] dest, int destOffset, int len) throws IOException {
        readFully(in, dest, destOffset, len, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, int[] dest) throws IOException {
        readFully(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, int[] dest, int byteOrder) throws IOException {
        readFully(in, dest, 0, dest.length, byteOrder);
    }

    public static void readFully(RandomAccessInput in, int[] dest, int destOffset, int len) throws IOException {
        readFully(in, dest, destOffset, len, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, long[] dest) throws IOException {
        readFully(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, long[] dest, int byteOrder) throws IOException {
        readFully(in, dest, 0, dest.length, byteOrder);
    }

    public static void readFully(RandomAccessInput in, long[] dest, int destOffset, int len) throws IOException {
        readFully(in, dest, destOffset, len, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, float[] dest) throws IOException {
        readFully(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, float[] dest, int byteOrder) throws IOException {
        readFully(in, dest, 0, dest.length, byteOrder);
    }

    public static void readFully(RandomAccessInput in, float[] dest, int destOffset, int len) throws IOException {
        readFully(in, dest, destOffset, len, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, double[] dest) throws IOException {
        readFully(in, dest, 0, dest.length, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, double[] dest, int byteOrder) throws IOException {
        readFully(in, dest, 0, dest.length, byteOrder);
    }

    public static void readFully(RandomAccessInput in, double[] dest, int destOffset, int len) throws IOException {
        readFully(in, dest, destOffset, len, in.getByteOrder());
    }

    public static void readFully(RandomAccessInput in, short[] dest, int offset, int length, int byteOrder) throws IOException {
        int sum = 0;
        while (length > 0) {
            int read = read(in, dest, offset, length, byteOrder);
            if (read <= 0) {
                throw new UnexpectedEOFException(sum);
            }
            sum += read;
            length -= read;
            offset += read;
        }
    }

    public static void readFully(RandomAccessInput in, char[] dest, int offset, int length, int byteOrder) throws IOException {
        int sum = 0;
        while (length > 0) {
            int read = read(in, dest, offset, length, byteOrder);
            if (read <= 0) {
                throw new UnexpectedEOFException(sum);
            }
            sum += read;
            length -= read;
            offset += read;
        }
    }

    public static void readFully(RandomAccessInput in, int[] dest, int offset, int length, int byteOrder) throws IOException {
        int sum = 0;
        while (length > 0) {
            int read = read(in, dest, offset, length, byteOrder);
            if (read <= 0) {
                throw new UnexpectedEOFException(sum);
            }
            sum += read;
            length -= read;
            offset += read;
        }
    }

    public static void readFully(RandomAccessInput in, long[] dest, int offset, int length, int byteOrder) throws IOException {
        int sum = 0;
        while (length > 0) {
            int read = read(in, dest, offset, length, byteOrder);
            if (read <= 0) {
                throw new UnexpectedEOFException(sum);
            }
            sum += read;
            length -= read;
            offset += read;
        }
    }

    public static void readFully(RandomAccessInput in, float[] dest, int offset, int length, int byteOrder) throws IOException {
        int sum = 0;
        while (length > 0) {
            int read = read(in, dest, offset, length, byteOrder);
            if (read <= 0) {
                throw new UnexpectedEOFException(sum);
            }
            sum += read;
            length -= read;
            offset += read;
        }
    }

    public static void readFully(RandomAccessInput in, double[] dest, int offset, int length, int byteOrder) throws IOException {
        int sum = 0;
        while (length > 0) {
            int read = read(in, dest, offset, length, byteOrder);
            if (read <= 0) {
                throw new UnexpectedEOFException(sum);
            }
            sum += read;
            length -= read;
            offset += read;
        }
    }

    public static int read(RandomAccessInput in, short[] dest, int destOffset, int len, int byteOrder) throws IOException {
        byte[] b = new byte[len * 2];
        int cnt = in.read(b);

        if ((cnt & 1) != 0) {
            in.seek(in.getFilePointer() - 1);
        }
        final int count = cnt >> 1;
        Transformer.byteToShort(b, 0, count, dest, destOffset, byteOrder == Endian.BIG_ENDIAN);
        return count;
    }

    public static int read(RandomAccessInput in, char[] dest, int destOffset, int len, int byteOrder) throws IOException {
        byte[] b = new byte[len * 2];

        int cnt = in.read(b);

        if ((cnt & 1) != 0) {
            in.seek(in.getFilePointer() - 1);
        }

        final int count = cnt >> 1;
        Transformer.byteToChar(b, 0, count, dest, destOffset, byteOrder == Endian.BIG_ENDIAN);
        return count;
    }

    public static int read(RandomAccessInput in, int[] dest, int destOffset, int len, int byteOrder) throws IOException {
        byte[] b = new byte[len * 4];

        int cnt = in.read(b);

        int r3 = cnt & 3;
        if (r3 != 0) {
            in.seek(in.getFilePointer() - r3);
        }

        final int count = cnt >> 2;
        Transformer.byteToInt(b, 0, count, dest, destOffset, byteOrder == Endian.BIG_ENDIAN);
        return count;
    }

    public static int read(RandomAccessInput in, float[] dest, int destOffset, int len, int byteOrder) throws IOException {
        byte[] b = new byte[len * 4];
        int cnt = in.read(b);

        int r3 = cnt & 3;
        if (r3 != 0) {
            in.seek(in.getFilePointer() - r3);
        }

        final int count = cnt >> 2;
        Transformer.byteToFloat(b, 0, count, dest, destOffset, byteOrder == Endian.BIG_ENDIAN);
        return count;
    }

    public static int read(RandomAccessInput in, long[] dest, int destOffset, int len, int byteOrder) throws IOException {
        byte[] b = new byte[len * 8];
        int cnt = in.read(b);

        int r7 = cnt & 7;
        if (r7 != 0) {
            in.seek(in.getFilePointer() - r7);
        }

        final int count = cnt >> 3;
        Transformer.byteToLong(b, 0, count, dest, destOffset, byteOrder == Endian.BIG_ENDIAN);
        return count;
    }

    public static int read(RandomAccessInput in, double[] dest, int destOffset, int len, int byteOrder) throws IOException {
        byte[] b = new byte[len * 8];
        int cnt = in.read(b);

        int r7 = cnt & 7;
        if (r7 != 0) {
            in.seek(in.getFilePointer() - r7);
        }

        final int count = cnt >> 3;
        Transformer.byteToDouble(b, 0, count, dest, destOffset, byteOrder == Endian.BIG_ENDIAN);
        return count;
    }

    public static String readString(DataInput in, int length) throws IOException {
        byte [] b = new byte[length];
        in.readFully(b);
        return new String(b);
    }
}
