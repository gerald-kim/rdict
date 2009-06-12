package com.imagero.uio.ba;


/**
 * ByteArrayIO - this class gives the possibility to read from and write to given ByteArray.
 * It supports bit offsets and bitwise writing.
 * It does not extends InputStream or OutputStream, but has similar methods.
 *
 * Date: 09.04.2008
 *
 * @author Andrey Kuznetsov
 */
public class ByteArrayIO {

    public static int read(ByteArray ba, Position position) {
        return read(ba, position, 8);
    }

    /**
     * read some bytes from given ByteArray
     * @param ba ByteArray
     * @param nbits bit count to read
     * @return int
     */
    public static int read(ByteArray ba, Position position, int nbits) {
        int ret = 0;
        //nothing to read
        
        while (nbits > 0) {
            int nb = nbits > 8? 8 : nbits;
            ret = (ret << nb) | read8(ba, position, nb);
            nbits -= nb;
        }
        return ret;
    }

    public static int read(ByteArray ba, Position position, byte b[]) {
        return read(ba, position, b, 0, b.length);
    }

    /**
     * Reads data from input stream into an byte array.
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or -1 if the EOF has been reached.
     * @exception java.lang.NullPointerException if supplied byte array is null
     */
    public static int read(ByteArray ba, Position position, byte b[], int off, int len) {
        if (len <= 0) {
            return 0;
        }
        int c = read(ba, position);
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        for (; i < len; ++i) {
            c = read(ba, position);
            if (c == -1) {
                break;
            }
            b[off + i] = (byte) c;
        }
        return i;
    }

    /**
     * Writes some bits from the specified int to stream.
     * @param b int which should be written
     */
    public static void write(ByteArray ba, Position position, int b) {
        write(ba, position, b, 8);
    }

    /**
     * Writes up to 8 bits from the specified int to stream.
     * @param N int which should be written
     * @param N_BITS bit count to write
     */
    public static void write(ByteArray ba, Position position, int N, int N_BITS) {
        int nbits = N_BITS;
        while (nbits > 0) {
            int n = N & 0xFF;
            write8(ba, position, n, Math.min(nbits, 8));
            N >>= 8;
            nbits -= 8;
        }
    }

    private static void write8(ByteArray ba, Position position, int N, int N_BITS) {
        if (position.bitOffset == 0 && N_BITS == 8) {
            ba.buffer[position.position++] = (byte) (N & 0xFF);
        } else {
            write0(ba, position, N, N_BITS);
        }
    }

    /**
     * read up to 8 bits from given ByteArray
     * @param nbits bit count to read
     * @param ba ByteArray
     * @return int
     */
    protected static int read8(ByteArray ba, Position position, final int nbits) {
        if (position.bitOffset == 0 && nbits == 8) {
            return ba.buffer[position.position++] & 0xFF;
        }
        int max = 8 - position.bitOffset;
        if(max > nbits) {
            int ret = ba.buffer[position.position] & ByteArray.N_MASK[position.bitOffset];
            int rshift = max - nbits;
            ret = ret >> rshift;
            position.bitOffset += nbits;
            return ret;
        }
        else if(max == nbits) {
            int ret = ba.buffer[position.position] & ByteArray.N_MASK[position.bitOffset];
            position.bitOffset = 0;
            position.position++;
            return ret;
        }
        else {
            int ret = ba.buffer[position.position] & ByteArray.N_MASK[position.bitOffset];
            int rbits = nbits - max;
            position.bitOffset = 0;
            position.position++;
            return ret << rbits | read8(ba, position, rbits);
        }
    }

    protected final static void write0(ByteArray ba, Position position, final int N, int N_BITS) {
        int available = 8 - position.bitOffset;
        int a = ba.buffer[position.position] & 0xFF;
        int b = a;

        a = ((a >> available) << N_BITS) | (N & ByteArray.K_MASK[N_BITS]);
        if (N_BITS > available) {
            a = a >> (N_BITS - available);
            ba.buffer[position.position++] = (byte) a;
            position.bitOffset = 0;
            N_BITS -= available;
            write0(ba, position, N & ByteArray.K_MASK[N_BITS], N_BITS);
        } else if (available > N_BITS) {
            a = a << (available - N_BITS);
            a |= b & ByteArray.K_MASK[available - N_BITS];
            ba.buffer[position.position] = (byte) a;
            position.bitOffset += N_BITS;
        } else {
            ba.buffer[position.position++] = (byte) a;
            position.bitOffset = 0;
        }
        if (position.bitOffset >= 8) {
            position.position++;
            position.bitOffset -= 8;
        }
    }

    /**
     * skip some bits
     * @param n bits to skip
     * @return number of bits skipped
     */
    public static int skipBits(Position position, int n) {
        int k = n;
        int nbits = k % 8;
        int nbytes = k / 8;
        int bitOffset = position.bitOffset + nbits;
        if (bitOffset > 7) {
            nbytes++;
            bitOffset = bitOffset - 8;
        }
        position.position += nbytes;
        return n;
    }
}
