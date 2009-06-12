package com.imagero.uio.xform;



/**
 * Date: 10.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class XtoByteBE {
    //private static final int[] SHIFTS_LONG_BE = new int[]{56, 48, 40, 32, 24, 16, 8, 0};
    static final int[] SHIFTS_INT_BE = new int[]{24, 16, 8, 0};
    static final int[] byte_mask = {1, 2, 4, 8, 16, 32, 64, 128, 256};
    static final int[] shift_mask = {0, 8, 16, 24, 32, 40, 48, 56};

    /**
     * read int in BIG_ENDIAN order
     *
     * @param sourceOffset     offset in source array
     * @param dest       byte array (destination)
     * @param destOffset offset in destination array
     *
     * @return offset in destination array (updated)
     */
    public static final int intToByte(int[] source, int sourceOffset, byte[] dest, int destOffset) {
        int v = source[sourceOffset];
        dest[destOffset++] = (byte) ((v >> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static final int intToByte(int v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) ((v >> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    /**
     * convert <code>count</code> ints to bytes (Big Endian)
     * @param source int array
     * @param srcOffset start offset in <code>source</code> array
     * @param count how much ints to process
     * @param dest destination byte array
     * @param destOffset
     */
    public static final void intToByte(int[] source, int srcOffset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = source[srcOffset + i];
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) (v & 0xFF);
        }
    }

    /**
     *
     * convert <code>count</code> ints to bytes (Big Endian)
     * @param source int array
     * @param srcOffset start offset in <code>source</code> array
     * @param count how much ints to process
     * @param dest destination byte array
     * @param skip how much bytes should be thrown away before start writing to destination (1 to 3)
     */
    public static final void intToByteBE(int[] source, int srcOffset, int count, byte[] dest, int destOffset, int skip) {
        if (skip > 0) {
            int v = source[srcOffset++];
            for (int i = skip; i < 4; i++) {
                dest[destOffset++] = (byte) ((v >>> SHIFTS_INT_BE[i]) & 0xFF);
            }
        }
        intToByte(source, srcOffset, count, dest, destOffset);
    }

    public static int charToByte(char[] source, int srcOffset, byte[] dest, int destOffset) {
        int v = source[srcOffset];
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static int charToByte(char v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static void charToByte(char[] source, int srcOffset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = source[srcOffset + i];
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) (v & 0xFF);
        }
    }

    public static int doubleToByteBE(double[] source, int srcOffset, byte[] dest, int destOffset) {
        double d = source[srcOffset];
        long v = Double.doubleToLongBits(d);
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static int doubleToByteBE(double d, byte[] dest, int destOffset) {
        long v = Double.doubleToLongBits(d);
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static void doubleToByteBE(double[] source, int srcOffset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            double d = source[srcOffset + i];
            long v = Double.doubleToLongBits(d);
            dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) (v & 0xFF);
        }
    }

    public static int floatToByteBE(float[] source, int offset, byte[] dest, int destOffset) {
        float f = source[offset];
        int v = Float.floatToIntBits(f);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static int floatToByteBE(float f, byte[] dest, int destOffset) {
        int v = Float.floatToIntBits(f);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static void floatToByteBE(float[] source, int offset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            float f = source[offset + i];
            int v = Float.floatToIntBits(f);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) (v & 0xFF);
        }
    }

    public static int longToByteBE(long[] source, int offset, byte[] dest, int destOffset) {
        long v = source[offset];
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);

        return destOffset;
    }

    public static int longToByteBE(long v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);

        return destOffset;
    }

    public static byte [] longToByteBE(long v) {
        byte[] dest = new byte[8];
        longToByteBE(v, dest, 0);
        return dest;
    }

    public static void longToByteBE(long[] source, int offset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = source[offset + i];
            dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) (v & 0xFF);
        }
    }

    public static int shortToByteBE(short[] source, int offset, byte[] dest, int destOffset) {
        int v = source[offset];
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static int shortToByteBE(short v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) (v & 0xFF);
        return destOffset;
    }

    public static void shortToByteBE(short[] source, int offset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = source[offset + i];
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) (v & 0xFF);
        }
    }

    public static int shortToByteBE(final byte mask, short[] source, int offset, byte[] dest, int destOffset) {
        final int bytesInShort = 2;
        int v = source[offset];
        for (int j = 0; j < bytesInShort; j++) {
            if ((mask & byte_mask[j]) != 0) {
                dest[destOffset++] = (byte) ((v >>> shift_mask[bytesInShort - 1 - j]) & 0xFF);
            }
        }
        return destOffset;
    }

    public static void shortToByteBE(final byte mask, short[] source, int offset, int count, byte[] dest, int destOffset) {
        final int bytesInShort = 2;
        for (int i = 0; i < count; i++) {
            int v = source[offset + i];
            for (int j = 0; j < bytesInShort; j++) {
                if ((mask & byte_mask[j]) != 0) {
                    dest[destOffset++] = (byte) ((v >>> shift_mask[bytesInShort - 1 - j]) & 0xFF);
                }
            }
        }
    }

    public static int intToByteBE(final byte mask, int[] source, int offset, byte[] dest, int destOffset) {
        final int bytesInInt = 4;
        int v = source[offset];
        for (int j = 0; j < bytesInInt; j++) {
            if ((mask & byte_mask[j]) != 0) {
                dest[destOffset++] = (byte) ((v >>> shift_mask[bytesInInt - 1 - j]) & 0xFF);
            }
        }
        return destOffset;
    }

    public static void intToByteBE(final byte mask, int[] source, int offset, int count, byte[] dest, int destOffset) {
        final int bytesInInt = 4;
        for (int i = 0; i < count; i++) {
            int v = source[offset + i];
            for (int j = 0; j < bytesInInt; j++) {
                if ((mask & byte_mask[j]) != 0) {
                    dest[destOffset++] = (byte) ((v >>> shift_mask[bytesInInt - 1 - j]) & 0xFF);
                }
            }
        }
    }

    public static int longToByteBE(final byte mask, long[] source, int offset, byte[] dest, int destOffset) {
        final int bytesInLong = 8;
        long v = source[offset];
        for (int j = 0; j < bytesInLong; j++) {
            if ((mask & byte_mask[j]) != 0) {
                dest[destOffset++] = (byte) ((v >>> shift_mask[bytesInLong - 1 - j]) & 0xFF);
            }
        }
        return destOffset;
    }

    public static void longToByteBE(final byte mask, long[] source, int offset, int count, byte[] dest, int destOffset) {
        final int bytesInLong = 8;
        for (int i = 0; i < count; i++) {
            long v = source[offset + i];
            for (int j = 0; j < bytesInLong; j++) {
                if ((mask & byte_mask[j]) != 0) {
                    dest[destOffset++] = (byte) ((v >>> shift_mask[bytesInLong - 1 - j]) & 0xFF);
                }
            }
        }
    }

    public static final int getBytesPerNumber(long mask) {
        int bpi = 0;
        for (int i = 0; i < byte_mask.length; i++) {
            if ((mask & byte_mask[i]) != 0) {
                bpi++;
            }
        }
        return bpi;
    }
}
