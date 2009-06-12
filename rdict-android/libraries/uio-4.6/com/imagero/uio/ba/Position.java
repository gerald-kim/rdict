package com.imagero.uio.ba;

/**
 * Date: 06.11.2008
 *
 * @author Andrey Kuznetsov
 */
public class Position {
    int bitOffset;
    public int position;
    public final int length;

    Position(int length) {
        this.length = length;
    }

    public Position(Position p) {
        this.length = p.length;
        bitOffset = p.bitOffset;
        position = p.position;
    }

    /**
     * Skips some bytes from the input stream.
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     */
    public long skipBytes(long n) {
        int max = (int) Math.min(length - position, n);
        position += max;
        return max;
    }

    public int getPosition() {
        return position;
    }

    public void seek(int pos) {
        position = pos;
        bitOffset = 0;
    }

    public void seek(int pos, int bitPos) {
        position = pos + bitPos / 8;
        bitOffset = bitPos % 8;
    }

    public int skipToByteBoundary() {
        if (bitOffset > 0) {
            int ret = 8 - bitOffset;
            bitOffset = 0;
            position++;
            return ret;
        }
        return 0;
    }

    public int getBitOffset() {
        return bitOffset;
    }
}
