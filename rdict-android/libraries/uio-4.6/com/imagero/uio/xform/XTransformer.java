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
package com.imagero.uio.xform;


/**
 * Primitive type conversion, array copying, etc.
 * The difference to Transformer is that XTransformer does not restricted to big and little endiannes.
 * User may read bytes in any order.
 *
 * @author Andrey Kuznetsov
 */
public class XTransformer {

    protected static final int[] shifts = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
    protected static final int[] POSITIONS_BE = {0, 1, 2, 3, 4, 5, 6, 7};
    protected static final int[] POSITIONS_LE_SHORT = {1, 0};
    protected static final int[] POSITIONS_LE_INT = {3, 2, 1, 0};
    protected static final int[] POSITIONS_LE_LONG = {7, 6, 5, 4, 3, 2, 1, 0};
    protected static final int[][] POSITIONS_LE = {POSITIONS_BE, POSITIONS_LE_SHORT, POSITIONS_LE_INT, POSITIONS_LE_LONG};

    public static final int TYPE_BE = 0;
    public static final int TYPE_LE_SHORT = 1;
    public static final int TYPE_LE_INT = 2;
    public static final int TYPE_LE_LONG = 3;

    public static final int[] get(int type) {
        return POSITIONS_LE[type];
    }

    /**
     * write int in BIG_ENDIAN order
     *
     * @param source       source byte array
     * @param sourceOffset offset in source array
     * @param destOffset   offset in destination array
     *
     * @return new offset in source array (for next writeUnitXX)
     */
    public static final int byteToInt(byte[] source, int sourceOffset, int[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        dest[destOffset] = v;
        return sourceOffset + p;
    }

    public static final int byteToInt(byte[] source, int sourceOffset, int[] positions) {
        int p = 0;
        return ((source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
    }

    /**
     * read int in BIG_ENDIAN order
     *
     * @param sourceOffset     offset in source array
     * @param dest       byte array (destination)
     * @param destOffset offset in destination array
     *
     * @return offset in destination array (updated)
     */
    public static final int intToByte(int[] source, int sourceOffset, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = source[sourceOffset];
        dest[destOffset + positions[p++]] = (byte) ((v >> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 4;
    }

    public static final int intToByte(int v, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        dest[destOffset + positions[p++]] = (byte) ((v >> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 4;
    }

    /**
     * convert <code>count</code> ints to bytes (Big Endian)
     * @param source int array
     * @param srcOffset start offset in <code>source</code> array
     * @param count how much ints to process
     * @param dest destination byte array
     * @param destOffset
     */
    public static final void intToByte(int[] source, int srcOffset, int count, byte[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = source[srcOffset + i];
            dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
            destOffset += 4;
        }
    }


    public static final void byteToInt(byte[] source, int sourceOffset, int count, int[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 24)
                    | ((source[sourceOffset + positions[p++]] & 0xFF) << 16)
                    | ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                    | (source[sourceOffset + positions[p++]] & 0xFF);
            dest[destOffset + i] = v;
            sourceOffset += 4;
        }
    }


/* **********************************************************************/

    public static int byteToChar(byte[] source, int sourceOffset, char[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        dest[destOffset] = (char) v;
        return sourceOffset + 2;
    }

    public static int byteToChar(byte[] source, int sourceOffset, int[] positions) {
        int p = 0;
        return ((source[sourceOffset + positions[p++]] & 0xFF) << 8) | (source[sourceOffset + positions[p++]] & 0xFF);
    }

    public static void byteToChar(byte[] source, int sourceOffset, int count, char[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                    | (source[sourceOffset + positions[p++]] & 0xFF);
            dest[destOffset + i] = (char) v;
        }
    }

/* **********************************************************************/

    public static int charToByte(char[] source, int srcOffset, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = source[srcOffset];
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 2;
    }

    public static int charToByte(char v, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 2;
    }

    public static void charToByte(char[] source, int srcOffset, int count, byte[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = source[srcOffset + i];
            dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
            destOffset += 2;
        }
    }

/* **********************************************************************/

    public static int byteToDouble(byte[] source, int sourceOffset, double[] dest, int destOffset, int[] positions) {
        int p = 0;
        long v = ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 56)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 48)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 40)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 32)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        dest[destOffset] = Double.longBitsToDouble(v);
        return sourceOffset + 8;
    }

    public static double byteToDouble(byte[] source, int sourceOffset, int[] positions) {
        int p = 0;
        long v = ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 56)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 48)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 40)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 32)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        return Double.longBitsToDouble(v);
    }

    public static void byteToDouble(byte[] source, int sourceOffset, int count, double[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            long v = ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 56)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 48)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 40)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 32)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 24)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 16)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 8)
                    | (source[sourceOffset + positions[p++]] & 0xFF);
            dest[destOffset + i] = Double.longBitsToDouble(v);
        }
    }

/* **********************************************************************/

    public static int doubleToByte(double[] source, int srcOffset, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        double d = source[srcOffset];
        long v = Double.doubleToLongBits(d);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 8;
    }

    public static int doubleToByte(double d, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        long v = Double.doubleToLongBits(d);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 8;
    }

    public static void doubleToByte(double[] source, int srcOffset, int count, byte[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            double d = source[srcOffset + i];
            long v = Double.doubleToLongBits(d);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 56) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 48) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 40) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 32) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
            destOffset += 8;
        }
    }

/* **********************************************************************/

    public static int byteToFloat(byte[] source, int sourceOffset, float[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        dest[destOffset] = Float.intBitsToFloat(v);
        return sourceOffset;
    }

    public static float byteToFloat(byte[] source, int sourceOffset, int[] positions) {
        int p = 0;
        int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        return Float.intBitsToFloat(v);
    }

    public static void byteToFloat(byte[] source, int sourceOffset, int count, float[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 24)
                    | ((source[sourceOffset + positions[p++]] & 0xFF) << 16)
                    | ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                    | (source[sourceOffset + positions[p++]] & 0xFF);
            dest[destOffset + i] = Float.intBitsToFloat(v);
        }
    }

/* **********************************************************************/

    public static int floatToByte(float[] source, int offset, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        float f = source[offset];
        int v = Float.floatToIntBits(f);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 4;
    }

    public static int floatToByte(float f, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = Float.floatToIntBits(f);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 4;
    }

    public static void floatToByte(float[] source, int offset, int count, byte[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            float f = source[offset + i];
            int v = Float.floatToIntBits(f);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
            destOffset += 4;
        }
    }

/* **********************************************************************/

    public static int byteToLong(byte[] source, int sourceOffset, long[] dest, int destOffset, int[] positions) {
        int p = 0;
        long v = ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 56)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 48)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 40)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 32)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);

        dest[destOffset] = v;
        return sourceOffset + 8;
    }

    public static long byteToLong(byte[] source, int sourceOffset, int[] positions) {
        int p = 0;
        return ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 56)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 48)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 40)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 32)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 24)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 16)
                | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
    }

    public static void byteToLong(byte[] source, int sourceOffset, int count, long[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            long v = ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 56)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 48)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 40)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 32)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 24)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 16)
                    | ((long) (source[sourceOffset + positions[p++]] & 0xFF) << 8)
                    | (source[sourceOffset + positions[p++]] & 0xFF);

            dest[destOffset + i] = v;
        }
    }

/* **********************************************************************/

    public static int longToByte(long[] source, int offset, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        long v = source[offset];
        dest[destOffset + positions[p++]] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);

        return destOffset + 8;
    }

    public static int longToByte(long v, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        dest[destOffset + positions[p++]] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);

        return destOffset;
    }

    public static void longToByte(long[] source, int offset, int count, byte[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            long v = source[offset + i];
            dest[destOffset + positions[p++]] = (byte) ((v >>> 56) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 48) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 40) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 32) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
            destOffset += 8;
        }
    }

/* **********************************************************************/

    public static int byteToShort(byte[] source, int sourceOffset, short[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                | (source[sourceOffset + positions[p++]] & 0xFF);
        dest[destOffset] = (short) v;
        return sourceOffset + 2;
    }

    public static int byteToShort(byte[] source, int sourceOffset, int[] positions) {
        int p = 0;
        return ((source[sourceOffset + positions[p++]] & 0xFF) << 8) | (source[sourceOffset + positions[p++]] & 0xFF);
    }

    public static void byteToShort(byte[] source, int sourceOffset, int count, short[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = ((source[sourceOffset + positions[p++]] & 0xFF) << 8)
                    | (source[sourceOffset + positions[p++]] & 0xFF);
            dest[destOffset + i] = (short) v;
        }
    }

/* **********************************************************************/

    public static int shortToByte(short[] source, int offset, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        int v = source[offset];
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 2;
    }

    public static int shortToByte(short v, byte[] dest, int destOffset, int[] positions) {
        int p = 0;
        dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
        return destOffset + 2;
    }

    public static void shortToByte(short[] source, int offset, int count, byte[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int p = 0;
            int v = source[offset + i];
            dest[destOffset + positions[p++]] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset + positions[p++]] = (byte) (v & 0xFF);
            destOffset += 2;
        }
    }

    /* **********************************************************************/

    public static final void byteToInt(final int bytesInInt, byte[] source, int sourceOffset, final int count, int[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int v = 0;
            for (int j = 0; j < bytesInInt; j++) {
                v = (v << 8) | (source[sourceOffset + positions[j]] & 0xFF);
            }
            dest[destOffset++] = v & 0xFFFFFFFF;
            sourceOffset += bytesInInt;
        }
    }

    public static final void byteToShort(final int bytesInShort, byte[] source, int sourceOffset, final int count, short[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            int v = 0;
            for (int j = 0; j < bytesInShort; j++) {
                v = (v << 8) | (source[sourceOffset + positions[j]] & 0xFF);
            }
            dest[destOffset++] = (short) v;
            sourceOffset += bytesInShort;
        }
    }

    public static final void byteToLong(final int bytesInLong, byte[] source, int sourceOffset, final int count, long[] dest, int destOffset, int[] positions) {
        for (int i = 0; i < count; i++) {
            long v = 0;
            for (int j = 0; j < bytesInLong; j++) {
                v = (v << 8) | (source[sourceOffset + positions[j]] & 0xFF);
            }
            dest[destOffset++] = v;
            sourceOffset += bytesInLong;
        }
    }

/* **********************************************************************/


    public static final int byteToInt(int bytesInInt, byte[] source, int sourceOffset, int[] dest, int destOffset, int[] positions) {
        int v = 0;
        for (int i = 0; i < bytesInInt; i++) {
            v = (v << 8) | (source[sourceOffset + positions[i]] & 0xFF);
        }
        dest[destOffset] = v & 0xFFFFFFFF;
        return sourceOffset + bytesInInt;
    }

    public static final int byteToShort(int bytesInShort, byte[] source, int sourceOffset, short[] dest, int destOffset, int[] positions) {
        int v = 0;
        for (int i = 0; i < bytesInShort; i++) {
            v = (v << 8) | (source[sourceOffset + positions[i]] & 0xFF);
        }
        dest[destOffset] = (short) v;
        return sourceOffset + bytesInShort;
    }

    public static final int byteToLong(int bytesInLong, byte[] source, int sourceOffset, long[] dest, int destOffset, int[] positions) {
        long v = 0;
        for (int i = 0; i < bytesInLong; i++) {
            v = (v << 8) | (source[sourceOffset + positions[i]] & 0xFF);
        }
        dest[destOffset] = v;
        return sourceOffset + bytesInLong;
    }
}