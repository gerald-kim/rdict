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

import com.imagero.uio.io.BitInputStream;
import com.imagero.uio.io.BitOutputStream;
import com.imagero.uio.io.ByteArrayOutputStreamExt;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.xform.XTransformer;
import com.imagero.uio.xform.ByteToXBE;
import com.imagero.uio.xform.ByteToXLE;
import com.imagero.uio.xform.XtoByteBE;
import com.imagero.uio.xform.XtoByteLE;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Primitive type conversion, array copying, etc.
 *
 * @author Andrey Kuznetsov
 */
public class Transformer extends XTransformer {



    public static final int byteToInt(byte[] source, int sourceOffset, int[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToInt(source, sourceOffset, dest, destOffset);
        } else {
            return ByteToXLE.byteToInt(source, sourceOffset, dest, destOffset);
        }
    }

    public static final void byteToInt(byte[] source, int sourceOffset, int count, int[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            ByteToXBE.byteToInt(source, sourceOffset, count, dest, destOffset);
        } else {
            ByteToXLE.byteToInt(source, sourceOffset, count, dest, destOffset);
        }
    }

    public static final int intToByte(int[] source, int sourceOffset, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.intToByte(source, sourceOffset, dest, destOffset);
        } else {
            return XtoByteLE.intToByte(source, sourceOffset, dest, destOffset);
        }
    }

    public static final void intToByte(int[] source, int srcOffset, int count, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            XtoByteBE.intToByte(source, srcOffset, count, dest, destOffset);
        } else {
            XtoByteLE.intToByte(source, srcOffset, count, dest, destOffset);
        }
    }

    public static int intToByte(int v, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.intToByte(v, dest, destOffset);
        } else {
            return XtoByteLE.intToByte(v, dest, destOffset);
        }
    }

    public static byte [] intToByte(int v, boolean bigEndian) {
        byte[] dest = new byte[4];
        if (bigEndian) {
            XtoByteBE.intToByte(v,dest, 0);
        } else {
            XtoByteLE.intToByte(v, dest, 0);
        }
        return dest;
    }

    public static final int byteToInt(byte[] source, int sourceOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToInt(source, sourceOffset);
        } else {
            return ByteToXLE.byteToInt(source, sourceOffset);
        }
    }


/* **********************************************************************/

    public static int byteToChar(byte[] source, int sourceOffset, char[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToChar(source, sourceOffset, dest, destOffset);
        } else {
            return ByteToXLE.byteToChar(source, sourceOffset, dest, destOffset);
        }
    }

    public static void byteToChar(byte[] source, int sourceOffset, int count, char[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            ByteToXBE.byteToChar(source, sourceOffset, count, dest, destOffset);
        } else {
            ByteToXLE.byteToChar(source, sourceOffset, count, dest, destOffset);
        }
    }

    public static int byteToChar(byte[] source, int sourceOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToChar(source, sourceOffset);
        } else {
            return ByteToXLE.byteToChar(source, sourceOffset);
        }
    }

/* **********************************************************************/
    public static int charToByte(char[] source, int srcOffset, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.charToByte(source, srcOffset, dest, destOffset);
        } else {
            return XtoByteLE.charToByte(source, srcOffset, dest, destOffset);
        }
    }

    public static int charToByte(char v, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.charToByte(v, dest, destOffset);
        } else {
            return XtoByteLE.charToByte(v, dest, destOffset);
        }
    }

    public static byte [] charToByte(char v, boolean bigEndian) {
        byte [] dest = new byte[2];
        if (bigEndian) {
            XtoByteBE.charToByte(v, dest, 0);
        } else {
            XtoByteLE.charToByte(v, dest, 0);
        }
        return dest;
    }

    public static void charToByte(char[] source, int srcOffset, int count, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            XtoByteBE.charToByte(source, srcOffset, count, dest, destOffset);
        } else {
            XtoByteLE.charToByte(source, srcOffset, count, dest, destOffset);
        }
    }

/* **********************************************************************/

    public static int byteToDouble(byte[] source, int sourceOffset, double[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToDoubleBE(source, sourceOffset, dest, destOffset);
        } else {
            return ByteToXLE.byteToDoubleLE(source, sourceOffset, dest, destOffset);
        }
    }

    public static void byteToDouble(byte[] source, int sourceOffset, int count, double[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            ByteToXBE.byteToDoubleBE(source, sourceOffset, count, dest, destOffset);
        } else {
            ByteToXLE.byteToDoubleLE(source, sourceOffset, count, dest, destOffset);
        }
    }

    public static double byteToDouble(byte[] source, int sourceOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToDoubleBE(source, sourceOffset);
        } else {
            return ByteToXLE.byteToDoubleLE(source, sourceOffset);
        }
    }

/* **********************************************************************/

    public static int doubleToByte(double[] source, int srcOffset, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.doubleToByteBE(source, srcOffset, dest, destOffset);
        } else {
            return XtoByteLE.doubleToByteLE(source, srcOffset, dest, destOffset);
        }
    }

    public static void doubleToByte(double[] source, int srcOffset, int count, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            XtoByteBE.doubleToByteBE(source, srcOffset, count, dest, destOffset);
        } else {
            XtoByteLE.doubleToByteLE(source, srcOffset, count, dest, destOffset);
        }
    }

    public static int doubleToByte(double d, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.doubleToByteBE(d, dest, destOffset);
        } else {
            return XtoByteLE.doubleToByteLE(d, dest, destOffset);
        }
    }

    public static byte [] doubleToByte(double d, boolean bigEndian) {
        byte[] dest = new byte[8];
        if (bigEndian) {
            XtoByteBE.doubleToByteBE(d, dest, 0);
        } else {
            XtoByteLE.doubleToByteLE(d, dest, 0);
        }
        return dest;
    }

/* **********************************************************************/

    public static int byteToFloat(byte[] source, int sourceOffset, float[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToFloatBE(source, sourceOffset, dest, destOffset);
        } else {
            return ByteToXLE.byteToFloatLE(source, sourceOffset, dest, destOffset);
        }
    }

    public static void byteToFloat(byte[] source, int sourceOffset, int count, float[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            ByteToXBE.byteToFloatBE(source, sourceOffset, count, dest, destOffset);
        } else {
            ByteToXLE.byteToFloatLE(source, sourceOffset, count, dest, destOffset);
        }
    }

    public static float byteToFloat(byte[] source, int sourceOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToFloatBE(source, sourceOffset);
        } else {
            return ByteToXLE.byteToFloatLE(source, sourceOffset);
        }
    }

/* **********************************************************************/

    public static int floatToByte(float[] source, int offset, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.floatToByteBE(source, offset, dest, destOffset);
        } else {
            return XtoByteLE.floatToByteLE(source, offset, dest, destOffset);
        }
    }

    public static void floatToByte(float[] source, int offset, int count, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            XtoByteBE.floatToByteBE(source, offset, count, dest, destOffset);
        } else {
            XtoByteLE.floatToByteLE(source, offset, count, dest, destOffset);
        }
    }

    public static int floatToByte(float f, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.floatToByteBE(f, dest, destOffset);
        } else {
            return XtoByteLE.floatToByteLE(f, dest, destOffset);
        }
    }

    public static byte [] floatToByte(float f, boolean bigEndian) {
        byte[] dest = new byte[4];
        if (bigEndian) {
            XtoByteBE.floatToByteBE(f, dest, 0);
        } else {
            XtoByteLE.floatToByteLE(f, dest, 0);
        }
        return dest;
    }

/* **********************************************************************/

    public static int byteToLong(byte[] source, int sourceOffset, long[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToLongBE(source, sourceOffset, dest, destOffset);
        } else {
            return ByteToXLE.byteToLongLE(source, sourceOffset, dest, destOffset);
        }
    }

    public static void byteToLong(byte[] source, int sourceOffset, int count, long[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            ByteToXBE.byteToLongBE(source, sourceOffset, count, dest, destOffset);
        } else {
            ByteToXLE.byteToLongLE(source, sourceOffset, count, dest, destOffset);
        }
    }

    public static long byteToLong(byte[] source, int sourceOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToLongBE(source, sourceOffset);
        } else {
            return ByteToXLE.byteToLongLE(source, sourceOffset);
        }
    }

/* **********************************************************************/

    public static int longToByte(long[] source, int offset, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.longToByteBE(source, offset, dest, destOffset);
        } else {
            return XtoByteLE.longToByteLE(source, offset, dest, destOffset);
        }
    }

    public static void longToByte(long[] source, int offset, int count, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            XtoByteBE.longToByteBE(source, offset, count, dest, destOffset);
        } else {
            XtoByteLE.longToByteLE(source, offset, count, dest, destOffset);
        }
    }

    public static int longToByte(long v, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.longToByteBE(v, dest, destOffset);
        } else {
            return XtoByteLE.longToByteLE(v, dest, destOffset);
        }
    }

    public static byte [] longToByte(long v, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.longToByteBE(v);
        } else {
            return XtoByteLE.longToByteLE(v);
        }
    }

/* **********************************************************************/


    public static int byteToShort(byte[] source, int sourceOffset, short[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToShortBE(source, sourceOffset, dest, destOffset);
        } else {
            return ByteToXLE.byteToShortLE(source, sourceOffset, dest, destOffset);
        }
    }

    public static void byteToShort(byte[] source, int sourceOffset, int count, short[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            ByteToXBE.byteToShortBE(source, sourceOffset, count, dest, destOffset);
        } else {
            ByteToXLE.byteToShortLE(source, sourceOffset, count, dest, destOffset);
        }
    }

    public static int byteToShort(byte[] source, int sourceOffset, boolean bigEndian) {
        if (bigEndian) {
            return ByteToXBE.byteToShortBE(source, sourceOffset);
        } else {
            return ByteToXLE.byteToShortLE(source, sourceOffset);
        }
    }

/* **********************************************************************/

    public static int shortToByte(short[] source, int offset, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.shortToByteBE(source, offset, dest, destOffset);
        } else {
            return XtoByteBE.shortToByteBE(source, offset, dest, destOffset);
        }
    }

    public static void shortToByte(short[] source, int offset, int count, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            XtoByteBE.shortToByteBE(source, offset, count, dest, destOffset);
        } else {
            XtoByteLE.shortToByteLE(source, offset, count, dest, destOffset);
        }
    }

    public static int shortToByte(short v, byte[] dest, int destOffset, boolean bigEndian) {
        if (bigEndian) {
            return XtoByteBE.shortToByteBE(v, dest, destOffset);
        } else {
            return XtoByteLE.shortToByteLE(v, dest, destOffset);
        }
    }

    public static byte [] shortToByte(short v, boolean bigEndian) {
        byte[] dest = new byte[2];
        if (bigEndian) {
            XtoByteBE.shortToByteBE(v, dest, 0);
        } else {
            XtoByteLE.shortToByteLE(v, dest, 0);
        }
        return dest;
    }

    public static final byte[] readByteLine(RandomAccessInput in) throws IOException {
        long start = in.getFilePointer();
        long end = start;
        boolean finished = false;
        boolean eof = false;
        int length = 0;
        while (!finished) {
            int k = in.read();
            switch (k) {
                case -1:
                    eof = true;
                    finished = true;
                    break;
                case '\n':
                    finished = true;
                    end = in.getFilePointer();
                    length = (int) (end - start);
                    break;
                case '\r':
                    finished = true;
                    end = in.getFilePointer();
                    length = (int) (end - start);
                    if ((in.read()) == '\n') {
                        end = in.getFilePointer();
                    }
                    break;
                default:
                    k = 0;
                    break;
            }
        }
        if (eof && length == 0) {
            return new byte[0];
        }
        byte[] b = new byte[length];
        in.seek(start);
        in.readFully(b);
        in.seek(end);
        return b;
    }

    public static final int readByteLine(RandomAccessInput in, byte[] dest) throws IOException {
        long start = in.getFilePointer();
        long end = start;
        boolean finished = false;
        boolean eof = false;
        int length = 0;
        int cnt = 0;
        while (!finished) {
            if (cnt++ >= dest.length) {
                finished = true;
                break;
            }
            switch (in.read()) {
                case -1:
                    eof = true;
                    finished = true;
                    break;
                case '\n':
                    finished = true;
                    end = in.getFilePointer();
                    length = (int) (end - start);
                    break;
                case '\r':
                    finished = true;
                    end = in.getFilePointer();
                    length = (int) (end - start);
                    if ((in.read()) == '\n') {
                        end = in.getFilePointer();
                    }
                    break;
            }
        }

        if (eof && length == 0) {
            return 0;
        }
        if (length == 0) {
            end = in.getFilePointer();
            length = Math.min(dest.length, (int) (end - start));
        }
        in.seek(start);
        in.readFully(dest, 0, length);
        in.seek(end);
        return length;
    }

    /**
     * Make right shift for all bytes of given array
     * @param src byte array
     * @param first value used to fill empty bits of first byte in array
     * @param shift shift amount (from 1 to 7)
     * @throws java.io.IOException
     */
    public static void shiftRight(byte[] src, int first, int shift) throws IOException {
        ByteArrayOutputStreamExt out = new ByteArrayOutputStreamExt(src.length + 1);
        BitOutputStream bos = new BitOutputStream(out);

        bos.write(first, shift);
        for (int i = 0; i < src.length; i++) {
            bos.write(src[i] & 0xFF);
        }
        bos.close();

        byte[] dst = out.drain();
        System.arraycopy(dst, 0, src, 0, src.length);
    }

    /**
     * Make left shift for all bytes in given array
     * @param src byte array
     * @param shift shift amount (from 1 to 7)
     * @throws java.io.IOException
     */
    public static void shiftLeft(byte[] src, int shift) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(src);
        BitInputStream bis = new BitInputStream(in);
        bis.read(shift);
        bis.setBitsToRead(8);
        int a = bis.read();
        int p = 0;
        while (a != -1 && p < src.length) {
            int b = bis.read();
            src[p++] = (byte) a;
            if (b == -1) {
                break;
            }
            a = b;
        }
    }
}
