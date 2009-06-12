package com.imagero.uio.ba;

/**
 * Date: 06.11.2008
 *
 * @author Andrey Kuznetsov
 */
public class BitState {
    int vbits = 0;
    int bitbuf = 0;
    int markBitbuf;
    int markVbits;
    boolean invertBitOrder;
    int fillByte;

    public BitState() {
    }

    public BitState(BitState bitState) {
        vbits = bitState.vbits;
        bitbuf = bitState.bitbuf;
        markBitbuf = bitState.markBitbuf;
        markVbits = bitState.markVbits;
        invertBitOrder = bitState.invertBitOrder;
        fillByte = bitState.fillByte;
    }

    public boolean isInvertBitOrder() {
        return invertBitOrder;
    }

    public void setInvertBitOrder(boolean invertBitOrder) {
        this.invertBitOrder = invertBitOrder;
        if (invertBitOrder) {
            BitInput.getFlipTable();
        }
    }

    public int getFillByte() {
        return fillByte;
    }

    public void setFillByte(int fillByte) {
        this.fillByte = fillByte;
    }

    /**
     * empties bit buffer.
     */
    public void resetBuffer() {
        vbits = 0;
        bitbuf = 0;
        markBitbuf = 0;
        markVbits = 0;
    }

    public synchronized void mark(int readlimit) {
        markBitbuf = bitbuf;
        markVbits = vbits;
    }

    public synchronized void reset() {
        bitbuf = markBitbuf;
        vbits = markVbits;
    }
}
