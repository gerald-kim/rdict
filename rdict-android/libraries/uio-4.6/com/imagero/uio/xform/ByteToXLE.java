package com.imagero.uio.xform;

/**
 * Date: 10.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class ByteToXLE {
    /**
     * write int in LITTLE_ENDIAN order
     *
     * @param source       source byte array
     * @param sourceOffset offset in source array
     * @param destOffset   offset in destination array
     *
     * @return new offset in source array (for next writeUnitXX)
     */
    public static final int byteToInt(byte[] source, int sourceOffset, int[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF))
                | (((source[sourceOffset++] & 0xFF)) << 8)
                | (((source[sourceOffset++] & 0xFF)) << 16)
                | (((source[sourceOffset++] & 0xFF)) << 24);
        dest[destOffset] = v;
        return sourceOffset;
    }

    public static final int byteToInt(byte[] source, int sourceOffset) {
        return ((source[sourceOffset++] & 0xFF))
                | (((source[sourceOffset++] & 0xFF)) << 8)
                | (((source[sourceOffset++] & 0xFF)) << 16)
                | (((source[sourceOffset++] & 0xFF)) << 24);
    }

    public static final void byteToInt(byte[] source, int sourceOffset, int count, int[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF))
                    | (((source[sourceOffset++] & 0xFF)) << 8)
                    | (((source[sourceOffset++] & 0xFF)) << 16)
                    | (((source[sourceOffset++] & 0xFF)) << 24);
            dest[destOffset + i] = v;
        }
    }

    public static int byteToChar(byte[] source, int sourceOffset, char[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF)) | (((source[sourceOffset++] & 0xFF)) << 8);
        dest[destOffset] = (char) v;
        return sourceOffset;
    }

    public static int byteToChar(byte[] source, int sourceOffset) {
        return ((source[sourceOffset++] & 0xFF)) | (((source[sourceOffset++] & 0xFF)) << 8);
    }

    public static void byteToChar(byte[] source, int sourceOffset, int count, char[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF)) | (((source[sourceOffset++] & 0xFF)) << 8);
            dest[destOffset + i] = (char) v;
        }
    }

    public static int byteToDoubleLE(byte[] source, int sourceOffset, double[] dest, int destOffset) {
        long v = (source[sourceOffset++] & 0xFF)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 56);
        dest[destOffset] = Double.longBitsToDouble(v);
        return sourceOffset;
    }

    public static double byteToDoubleLE(byte[] source, int sourceOffset) {
        long v = (source[sourceOffset++] & 0xFF)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 56);
        return Double.longBitsToDouble(v);
    }

    public static void byteToDoubleLE(byte[] source, int sourceOffset, int count, double[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = (source[sourceOffset++] & 0xFF)
                    | ((long) (source[sourceOffset++] & 0xFF) << 8)
                    | ((long) (source[sourceOffset++] & 0xFF) << 16)
                    | ((long) (source[sourceOffset++] & 0xFF) << 24)
                    | ((long) (source[sourceOffset++] & 0xFF) << 32)
                    | ((long) (source[sourceOffset++] & 0xFF) << 40)
                    | ((long) (source[sourceOffset++] & 0xFF) << 48)
                    | ((long) (source[sourceOffset++] & 0xFF) << 56);
            dest[destOffset + i] = Double.longBitsToDouble(v);
        }
    }

    public static int byteToFloatLE(byte[] source, int sourceOffset, float[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF))
                | (((source[sourceOffset++] & 0xFF)) << 8)
                | (((source[sourceOffset++] & 0xFF)) << 16)
                | (((source[sourceOffset++] & 0xFF)) << 24);
        dest[destOffset] = Float.intBitsToFloat(v);
        return sourceOffset;
    }

    public static float byteToFloatLE(byte[] source, int sourceOffset) {
        int v = ((source[sourceOffset++] & 0xFF))
                | (((source[sourceOffset++] & 0xFF)) << 8)
                | (((source[sourceOffset++] & 0xFF)) << 16)
                | (((source[sourceOffset++] & 0xFF)) << 24);
        return Float.intBitsToFloat(v);
    }

    public static void byteToFloatLE(byte[] source, int sourceOffset, int count, float[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF))
                    | (((source[sourceOffset++] & 0xFF)) << 8)
                    | (((source[sourceOffset++] & 0xFF)) << 16)
                    | (((source[sourceOffset++] & 0xFF)) << 24);
            dest[destOffset + i] = Float.intBitsToFloat(v);
        }
    }

    public static int byteToLongLE(byte[] source, int sourceOffset, long[] dest, int destOffset) {
        long v = (source[sourceOffset++] & 0xFF)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 56);
        dest[destOffset] = v;
        return sourceOffset;
    }

    public static long byteToLongLE(byte[] source, int sourceOffset) {
        return (source[sourceOffset++] & 0xFF)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 56);
    }

    public static void byteToLongLE(byte[] source, int sourceOffset, int count, long[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = (source[sourceOffset++] & 0xFF)
                    | ((long) (source[sourceOffset++] & 0xFF) << 8)
                    | ((long) (source[sourceOffset++] & 0xFF) << 16)
                    | ((long) (source[sourceOffset++] & 0xFF) << 24)
                    | ((long) (source[sourceOffset++] & 0xFF) << 32)
                    | ((long) (source[sourceOffset++] & 0xFF) << 40)
                    | ((long) (source[sourceOffset++] & 0xFF) << 48)
                    | ((long) (source[sourceOffset++] & 0xFF) << 56);
            dest[destOffset + i] = v;
        }
    }

    public static int byteToShortLE(byte[] source, int sourceOffset, short[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF))
                | (((source[sourceOffset++] & 0xFF)) << 8);
        dest[destOffset] = (short) v;
        return sourceOffset;
    }

    public static int byteToShortLE(byte[] source, int sourceOffset) {
        return ((source[sourceOffset++] & 0xFF)) | (((source[sourceOffset++] & 0xFF)) << 8);
    }

    public static void byteToShortLE(byte[] source, int sourceOffset, int count, short[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF))
                    | (((source[sourceOffset++] & 0xFF)) << 8);
            dest[destOffset + i] = (short) v;
        }
    }

    public static final void byteToIntLE(final int bytesInInt, byte[] source, int sourceOffset, final int count, int[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = 0;
            for (int j = 0, shift = 0; j < bytesInInt; j++) {
                v = v | ((source[sourceOffset + j] & 0xFF) << shift);
                shift += 8;
            }
            dest[destOffset++] = v & 0xFFFFFFFF;
            sourceOffset += bytesInInt;
        }
    }

    public static final void byteToShortLE(final int bytesInShort, byte[] source, int sourceOffset, final int count, short[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = 0;
            for (int j = 0, shift = 0; j < bytesInShort; j++) {
                v = v | ((source[sourceOffset + j] & 0xFF) << shift);
                shift += 8;
            }
            dest[destOffset++] = (short) v;
            sourceOffset += bytesInShort;
        }
    }

    public static final void byteToLongLE(final int bytesInLong, byte[] source, int sourceOffset, final int count, long[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = 0;
            for (int j = 0, shift = 0; j < bytesInLong; j++) {
                v = v | ((source[sourceOffset + j] & 0xFF) << shift);
                shift += 8;
            }
            dest[destOffset++] = v;
            sourceOffset += bytesInLong;
        }
    }

    public static final int byteToIntLE(int bytesInInt, byte[] source, int sourceOffset, int[] dest, int destOffset) {
        int v = 0;
        for (int i = 0, shift = 0; i < bytesInInt; i++) {
            v = v | ((source[sourceOffset++] & 0xFF) << shift);
            shift += 8;
        }
        dest[destOffset] = v & 0xFFFFFFFF;
        return sourceOffset;
    }

    public static final int byteToShortLE(int bytesInShort, byte[] source, int sourceOffset, short[] dest, int destOffset) {
        int v = 0;
        for (int i = 0, shift = 0; i < bytesInShort; i++) {
            v = v | ((source[sourceOffset++] & 0xFF) << shift);
            shift += 8;
        }
        dest[destOffset] = (short) v;
        return sourceOffset;
    }

    public static final int byteToLongLE(int bytesInLong, byte[] source, int sourceOffset, long[] dest, int destOffset) {
        long v = 0;
        for (int i = 0, shift = 0; i < bytesInLong; i++) {
            v = v | ((source[sourceOffset++] & 0xFF) << shift);
            shift += 8;
        }
        dest[destOffset] = v;
        return sourceOffset;
    }
}
