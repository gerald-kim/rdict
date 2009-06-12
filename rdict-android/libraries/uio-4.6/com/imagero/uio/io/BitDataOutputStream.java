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
 *  o Neither the name of Andrei Kouznetsov nor the names of
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

package com.imagero.uio.io;

import com.imagero.uio.FilterDataOutput;

import java.io.DataOutput;
import java.io.IOException;

/**
 * adds ability to write streams bitwise
 * @author Andrey Kuznetsov
 */
public class BitDataOutputStream extends FilterDataOutput {

    protected static final int[] mask = new int[64];

    static {
        for (int i = 0; i < mask.length; i++) {
            mask[i] =  (1 << (i + 1)) - 1;
        }
    }

    protected int bitbuf;
    protected int vbits;

    private int bitsToWrite = 8;

    protected byte [] flipTable = BitInputStream.getFlipTable();

    protected boolean invertBitOrder;

    protected int fillByte = 0;

    public BitDataOutputStream(DataOutput out) {
        super(out);
    }

    public int getBitsToWrite() {
        return bitsToWrite;
    }

    /**
     * set how much bits should be written to stream every write() call
     * @param bitsToWrite
     */
    public void setBitsToWrite(int bitsToWrite) {
        this.bitsToWrite = bitsToWrite;
    }

    public boolean isInvertBitOrder() {
        return invertBitOrder;
    }

    public void setInvertBitOrder(boolean invertBitOrder) {
        this.invertBitOrder = invertBitOrder;
    }

    /**
     * Writes some bits from the specified int to stream.
     * @param b int which should be written
     * @throws IOException if an I/O error occurs
     * @see #setBitsToWrite
     * @see #getBitsToWrite
     */
    public void write(int b) throws IOException {
        write(b, bitsToWrite);
    }

    /**
     * Writes some bits from the specified int to stream.
     * @param b int which should be written
     * @param nbits bit count to write
     * @throws IOException if an I/O error occurs
     */
    public void write(int b, int nbits) throws IOException {
        if (nbits == 0) {
            return;
        }
        final int k = b & mask[nbits];
        bitbuf = (bitbuf << nbits) | k;
        vbits += nbits;

        write8();
    }

    protected void write8() throws IOException {
        while (vbits > 8) {
            int c = (int) (bitbuf << (32 - vbits) >>> 24);
            vbits -= 8;
            if(invertBitOrder) {
                c = flipTable[c] & 0xFF;
            }
            out.write(c);
        }
    }

    /**
     * get fill byte used to adjust stream to byte boundary.
     * @return int
     */
    public int getFillByte() {
        return fillByte;
    }

    /**
     * set fill byte used to adjust stream to byte boundary
     * @param fillByte int
     */
    public void setFillByte(int fillByte) {
        this.fillByte = fillByte & 0xFF;
    }

    /**
     * writes bits from buffer to output stream
     * @throws IOException if I/O error occurs
     */
    public void flush() throws IOException {
        write8();   //rather not needed, just to ensure
        if(vbits > 0) {
            write(fillByte, 8);
        }
        vbits = 0;
        bitbuf = 0;
    }
}
