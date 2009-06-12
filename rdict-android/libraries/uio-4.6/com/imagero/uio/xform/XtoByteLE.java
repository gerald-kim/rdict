package com.imagero.uio.xform;



/**
 * Date: 10.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class XtoByteLE {
    static final int[] SHIFTS_INT_LE = new int[]{0, 8, 16, 24};

    /**
     * read int in LITTLE_ENDIAN order
     *
     * @param sourceOffset     offset in source array
     * @param dest       byte array (destination)
     * @param destOffset offset in destination array
     *
     * @return offset in destination array (updated)
     */
    public static final int intToByte(int[] source, int sourceOffset, byte[] dest, int destOffset) {
        int v = source[sourceOffset];
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        return destOffset;
    }

    public static final int intToByte(int v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        return destOffset;
    }

    /**
     * convert <code>count</code> ints to bytes (Little Endian)
     * @param source int array
     * @param srcOffset start offset in <code>source</code> array
     * @param count how much ints to process
     * @param dest destination byte array
     * @param destOffset start offset in <code>dest</code> array
     */
    public static final void intToByte(int[] source, int srcOffset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = source[srcOffset + i];
            dest[destOffset++] = (byte) (v & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        }
    }

    /**
     * convert <code>count</code> ints to bytes (Little Endian)
     * @param source int array
     * @param srcOffset start offset in <code>source</code> array
     * @param count how much ints to process
     * @param dest destination byte array
     * @param destOffset start offset in <code>dest</code> array
     * @param skip how much bytes should be thrown away before start writing to destination (1 to 3)
     */
    public static final void intToByteLE(int[] source, int srcOffset, int count, byte[] dest, int destOffset, int skip) {
        if (skip > 0) {
            int v = source[srcOffset++];
            for (int i = skip; i < 4; i++) {
                dest[destOffset++] = (byte) ((v >>> SHIFTS_INT_LE[i]) & 0xFF);
            }
        }
        intToByte(source, srcOffset, count, dest, destOffset);
    }

    public static final int charToByte(char[] source, int srcOffset, byte[] dest, int destOffset) {
        int v = source[srcOffset];
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        return destOffset;
    }

    public static final int charToByte(char v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        return destOffset;
    }

    public static final void charToByte(char[] source, int srcOffset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = source[srcOffset + i];
            dest[destOffset++] = (byte) (v & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        }
    }

    public static int doubleToByteLE(double[] source, int srcOffset, byte[] dest, int destOffset) {
        double d = source[srcOffset];
        long v = Double.doubleToLongBits(d);
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        return destOffset;
    }

    public static int doubleToByteLE(double d, byte[] dest, int destOffset) {
        long v = Double.doubleToLongBits(d);
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        return destOffset;
    }

    public static void doubleToByteLE(double[] source, int srcOffset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            double d = source[srcOffset + i];
            long v = Double.doubleToLongBits(d);
            dest[destOffset++] = (byte) (v & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        }
    }

    public static int floatToByteLE(float[] source, int offset, byte[] dest, int destOffset) {
        float f = source[offset];
        int v = Float.floatToIntBits(f);
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        return destOffset;
    }

    public static int floatToByteLE(float f, byte[] dest, int destOffset) {
        int v = Float.floatToIntBits(f);
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        return destOffset;
    }

    public static void floatToByteLE(float[] source, int offset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            float f = source[offset + i];
            int v = Float.floatToIntBits(f);
            dest[destOffset++] = (byte) (v & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        }
    }

    public static int longToByteLE(long[] source, int offset, byte[] dest, int destOffset) {
        long v = source[offset];
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);

        return destOffset;
    }

    public static int longToByteLE(long v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);

        return destOffset;
    }

    public static byte [] longToByteLE(long v) {
        byte[] dest = new byte[8];
        longToByteLE(v, dest, 0);
        return dest;
    }


    public static void longToByteLE(long[] source, int offset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            long v = source[offset + i];
            dest[destOffset++] = (byte) (v & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 16) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 24) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 32) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 40) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 48) & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 56) & 0xFF);
        }
    }

    public static int shortToByteLE(short[] source, int offset, byte[] dest, int destOffset) {
        int v = source[offset];
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        return destOffset;
    }

    public static int shortToByteLE(short v, byte[] dest, int destOffset) {
        dest[destOffset++] = (byte) (v & 0xFF);
        dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        return destOffset;
    }

    public static void shortToByteLE(short[] source, int offset, int count, byte[] dest, int destOffset) {
        for (int i = 0; i < count; i++) {
            int v = source[offset + i];
            dest[destOffset++] = (byte) (v & 0xFF);
            dest[destOffset++] = (byte) ((v >>> 8) & 0xFF);
        }
    }

    public static int shortToByteLE(final byte mask, short[] source, int offset, byte[] dest, int destOffset) {
        int v = source[offset];
        final int bytesInShort = 2;
        for (int j = 0; j < bytesInShort; j++) {
            if ((mask & XtoByteBE.byte_mask[j]) != 0) {
                dest[destOffset++] = (byte) ((v >>> XtoByteBE.shift_mask[j]) & 0xFF);
            }
        }
        return destOffset;
    }

    public static void shortToByteLE(final byte mask, short[] source, int offset, int count, byte[] dest, int destOffset) {
        final int bytesInShort = 2;
        for (int i = 0; i < count; i++) {
            int v = source[offset + i];
            for (int j = 0; j < bytesInShort; j++) {
                if ((mask & XtoByteBE.byte_mask[j]) != 0) {
                    dest[destOffset++] = (byte) ((v >>> XtoByteBE.shift_mask[j]) & 0xFF);
                }
            }
        }
    }

    public static int intToByteLE(final byte mask, int[] source, int offset, byte[] dest, int destOffset) {
        int v = source[offset];
        final int bytesInInt = 4;
        for (int j = 0; j < bytesInInt; j++) {
            if ((mask & XtoByteBE.byte_mask[j]) != 0) {
                dest[destOffset++] = (byte) ((v >>> XtoByteBE.shift_mask[j]) & 0xFF);
            }
        }
        return destOffset;
    }

    public static void intToByteLE(final byte mask, int[] source, int offset, int count, byte[] dest, int destOffset) {
        final int bytesInInt = 4;
        for (int i = 0; i < count; i++) {
            int v = source[offset + i];
            for (int j = 0; j < bytesInInt; j++) {
                if ((mask & XtoByteBE.byte_mask[j]) != 0) {
                    dest[destOffset++] = (byte) ((v >>> XtoByteBE.shift_mask[j]) & 0xFF);
                }
            }
        }
    }

    public static int longToByteLE(final byte mask, long[] source, int offset, byte[] dest, int destOffset) {
        long v = source[offset];
        final int bytesInLong = 8;
        for (int j = 0; j < bytesInLong; j++) {
            if ((mask & XtoByteBE.byte_mask[j]) != 0) {
                dest[destOffset++] = (byte) ((v >>> XtoByteBE.shift_mask[j]) & 0xFF);
            }
        }
        return destOffset;
    }

    public static void longToByteLE(final byte mask, long[] source, int offset, int count, byte[] dest, int destOffset) {
        final int bytesInLong = 8;
        for (int i = 0; i < count; i++) {
            long v = source[offset + i];
            for (int j = 0; j < bytesInLong; j++) {
                if ((mask & XtoByteBE.byte_mask[j]) != 0) {
                    dest[destOffset++] = (byte) ((v >>> XtoByteBE.shift_mask[j]) & 0xFF);
                }
            }
        }
    }
}
