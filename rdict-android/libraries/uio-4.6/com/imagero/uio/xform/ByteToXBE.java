package com.imagero.uio.xform;

/**
 * Big endian transform from bytes to other primitives.
 * Date: 10.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class ByteToXBE {
    /**
     * write int in BIG_ENDIAN order
     *
     * @param source       source byte array
     * @param sourceOffset offset in source array
     * @param destOffset   offset in destination array
     *
     * @return new offset in source array (for next writeUnitXX)
     */
    public static final int byteToInt(byte[] source, int sourceOffset, int[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF) << 24)
                | ((source[sourceOffset++] & 0xFF) << 16)
                | ((source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        dest[destOffset] = v;
        return sourceOffset;
    }

    public static final int byteToInt(byte[] source, int sourceOffset) {
        return ((source[sourceOffset++] & 0xFF) << 24)
                | ((source[sourceOffset++] & 0xFF) << 16)
                | ((source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
    }

    public static final void byteToInt(byte[] source, int sourceOffset, int count, int[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF) << 24)
                    | ((source[sourceOffset++] & 0xFF) << 16)
                    | ((source[sourceOffset++] & 0xFF) << 8)
                    | (source[sourceOffset++] & 0xFF);
            dest[destOffset + i] = v;
        }
    }

    public static int byteToChar(byte[] source, int sourceOffset, char[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        dest[destOffset] = (char) v;
        return sourceOffset;
    }

    public static int byteToChar(byte[] source, int sourceOffset) {
        return ((source[sourceOffset++] & 0xFF) << 8) | (source[sourceOffset++] & 0xFF);
    }

    public static void byteToChar(byte[] source, int sourceOffset, int count, char[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF) << 8)
                    | (source[sourceOffset++] & 0xFF);
            dest[destOffset + i] = (char) v;
        }
    }

    public static int byteToDoubleBE(byte[] source, int sourceOffset, double[] dest, int destOffset) {
        long v = ((long) (source[sourceOffset++] & 0xFF) << 56)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        dest[destOffset] = Double.longBitsToDouble(v);
        return sourceOffset;
    }

    public static double byteToDoubleBE(byte[] source, int sourceOffset) {
        long v = ((long) (source[sourceOffset++] & 0xFF) << 56)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        return Double.longBitsToDouble(v);
    }

    public static void byteToDoubleBE(byte[] source, int sourceOffset, int count, double[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = ((long) (source[sourceOffset++] & 0xFF) << 56)
                    | ((long) (source[sourceOffset++] & 0xFF) << 48)
                    | ((long) (source[sourceOffset++] & 0xFF) << 40)
                    | ((long) (source[sourceOffset++] & 0xFF) << 32)
                    | ((long) (source[sourceOffset++] & 0xFF) << 24)
                    | ((long) (source[sourceOffset++] & 0xFF) << 16)
                    | ((long) (source[sourceOffset++] & 0xFF) << 8)
                    | (source[sourceOffset++] & 0xFF);
            dest[destOffset + i] = Double.longBitsToDouble(v);
        }
    }

    public static int byteToFloatBE(byte[] source, int sourceOffset, float[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF) << 24)
                | ((source[sourceOffset++] & 0xFF) << 16)
                | ((source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        dest[destOffset] = Float.intBitsToFloat(v);
        return sourceOffset;
    }

    public static float byteToFloatBE(byte[] source, int sourceOffset) {
        int v = ((source[sourceOffset++] & 0xFF) << 24)
                | ((source[sourceOffset++] & 0xFF) << 16)
                | ((source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        return Float.intBitsToFloat(v);
    }

    public static void byteToFloatBE(byte[] source, int sourceOffset, int count, float[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF) << 24)
                    | ((source[sourceOffset++] & 0xFF) << 16)
                    | ((source[sourceOffset++] & 0xFF) << 8)
                    | (source[sourceOffset++] & 0xFF);
            dest[destOffset + i] = Float.intBitsToFloat(v);
        }
    }

    public static int byteToLongBE(byte[] source, int sourceOffset, long[] dest, int destOffset) {
        long v = ((long) (source[sourceOffset++] & 0xFF) << 56)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);

        dest[destOffset] = v;
        return sourceOffset;
    }

    public static long byteToLongBE(byte[] source, int sourceOffset) {
        return ((long) (source[sourceOffset++] & 0xFF) << 56)
                | ((long) (source[sourceOffset++] & 0xFF) << 48)
                | ((long) (source[sourceOffset++] & 0xFF) << 40)
                | ((long) (source[sourceOffset++] & 0xFF) << 32)
                | ((long) (source[sourceOffset++] & 0xFF) << 24)
                | ((long) (source[sourceOffset++] & 0xFF) << 16)
                | ((long) (source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
    }

    public static void byteToLongBE(byte[] source, int sourceOffset, int count, long[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = ((long) (source[sourceOffset++] & 0xFF) << 56)
                    | ((long) (source[sourceOffset++] & 0xFF) << 48)
                    | ((long) (source[sourceOffset++] & 0xFF) << 40)
                    | ((long) (source[sourceOffset++] & 0xFF) << 32)
                    | ((long) (source[sourceOffset++] & 0xFF) << 24)
                    | ((long) (source[sourceOffset++] & 0xFF) << 16)
                    | ((long) (source[sourceOffset++] & 0xFF) << 8)
                    | (source[sourceOffset++] & 0xFF);

            dest[destOffset + i] = v;
        }
    }

    public static final void byteToIntBE(final int bytesInInt, byte[] source, int sourceOffset, final int count, int[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = 0;
            for (int j = 0; j < bytesInInt; j++) {
                v = (v << 8) | (source[sourceOffset + j] & 0xFF);
            }
            dest[destOffset++] = v & 0xFFFFFFFF;
            sourceOffset += bytesInInt;
        }
    }

    public static final void byteToShortBE(final int bytesInShort, byte[] source, int sourceOffset, final int count, short[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = 0;
            for (int j = 0; j < bytesInShort; j++) {
                v = (v << 8) | (source[sourceOffset + j] & 0xFF);
            }
            dest[destOffset++] = (short) v;
            sourceOffset += bytesInShort;
        }
    }

    public static final void byteToLongBE(final int bytesInLong, byte[] source, int sourceOffset, final int count, long[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = 0;
            for (int j = 0; j < bytesInLong; j++) {
                v = (v << 8) | (source[sourceOffset + j] & 0xFF);
            }
            dest[destOffset++] = v;
            sourceOffset += bytesInLong;
        }
    }

    public static final int byteToIntBE(int bytesInInt, byte[] source, int sourceOffset, int[] dest, int destOffset) {
        int v = 0;
        for (int i = 0; i < bytesInInt; i++) {
            v = (v << 8) | (source[sourceOffset++] & 0xFF);
        }
        dest[destOffset] = v & 0xFFFFFFFF;
        return sourceOffset;
    }

    public static final int byteToShortBE(int bytesInShort, byte[] source, int sourceOffset, short[] dest, int destOffset) {
        int v = 0;
        for (int i = 0; i < bytesInShort; i++) {
            v = (v << 8) | (source[sourceOffset++] & 0xFF);
        }
        dest[destOffset] = (short) v;
        return sourceOffset;
    }

    public static final int byteToLongBE(int bytesInLong, byte[] source, int sourceOffset, long[] dest, int destOffset) {
        long v = 0;
        for (int i = 0; i < bytesInLong; i++) {
            v = (v << 8) | (source[sourceOffset++] & 0xFF);
        }
        dest[destOffset] = v;
        return sourceOffset;
    }

    public static int byteToShortBE(byte[] source, int sourceOffset, short[] dest, int destOffset) {
        int v = ((source[sourceOffset++] & 0xFF) << 8)
                | (source[sourceOffset++] & 0xFF);
        dest[destOffset] = (short) v;
        return sourceOffset;
    }

    public static int byteToShortBE(byte[] source, int sourceOffset) {
        return ((source[sourceOffset++] & 0xFF) << 8) | (source[sourceOffset++] & 0xFF);
    }

    public static void byteToShortBE(byte[] source, int sourceOffset, int count, short[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = ((source[sourceOffset++] & 0xFF) << 8)
                    | (source[sourceOffset++] & 0xFF);
            dest[destOffset + i] = (short) v;
        }
    }
}
