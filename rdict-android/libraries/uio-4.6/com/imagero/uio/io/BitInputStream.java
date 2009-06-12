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
package com.imagero.uio.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * adds ability to read streams bitewise and also to read predefined amount of bits every read() call
 * @author Andrey Kuznetsov
 */
public class BitInputStream extends FilterInputStream implements Snapable {

    int vbits = 0;
    int bitbuf = 0;
    int markBitbuf;
    int markVbits;
    private int bitsToRead = 8;
    boolean invertBitOrder;

    public BitInputStream(InputStream in) {
        super(in);
    }

    public BitInputStream(InputStream in, BisSnapshot snapshot) {
        super(in);
        vbits = snapshot.vbits;
        bitbuf = snapshot.bitbuf;
        markBitbuf = snapshot.markBitbuf;
        markVbits = snapshot.markVbits;
        bitsToRead = snapshot.bitsToRead;
        invertBitOrder = snapshot.invertBitOrder;
    }

    /**
     * how much bits is read every read() call (default - 8)
     * @return
     */
    public int getBitsToRead() {
        return bitsToRead;
    }

    /**
     * set how much bits is read every read() call (max 8)
     * @param bitsToRead
     */
    public void setBitsToRead(int bitsToRead) {
        if (bitsToRead > 32) {
            throw new IllegalArgumentException("" + bitsToRead);
        }
        this.bitsToRead = bitsToRead;
    }

    public boolean isInvertBitOrder() {
        return invertBitOrder;
    }

    public void setInvertBitOrder(boolean invertBitOrder) {
        this.invertBitOrder = invertBitOrder;
        if (invertBitOrder) {
            createFlipTable();
        }
    }

    public int read() throws IOException {
        return read(bitsToRead);
    }

    public int read(int nbits) throws IOException {
        int ret;
        //nothing to read
        if (nbits == 0) {
            return 0;
        }
        //too many bits requested
        if (nbits > 32) {
            throw new IllegalArgumentException("only 32 bit can be read at once");
        }
        if (nbits > 24) {
            int nbits0 = nbits / 2;
            int nbits1 = nbits - nbits0;
            return (read(nbits0) << nbits1) | read(nbits1);
        }
        //not anough bits in buffer
        if (nbits > vbits) {
            fillBuffer(nbits);
        }
        //buffer still empty => we are reached EOF
        if (vbits == 0) {
            return -1;
        }
        ret = (bitbuf << (32 - vbits)) >>> (32 - nbits);
        vbits -= nbits;

        if (vbits < 0) {
            vbits = 0;
        }

        return ret;
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Reads data from input stream into an byte array.
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or -1 if the EOF has been reached.
     * @exception IOException if an I/O error occurs.
     * @exception NullPointerException if supplied byte array is null
     */
    public int read(byte b[], int off, int len) throws IOException {
        if (len <= 0) {
            return 0;
        }
        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        for (; i < len; ++i) {
            c = read();
            if (c == -1) {
                break;
            }
            b[off + i] = (byte) c;
        }
        return i; 
    }

    /**
     * empties bit buffer.
     */
    public void resetBuffer() {
        vbits = 0;
        bitbuf = 0;
    }

    /**
     * Skips some bytes from the input stream.
     * If bit buffer is not empty, n - (vbits + 8) / 8 bytes skipped,
     * then buffer is resetted and filled with same amount of bits as it has before skipping.
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @exception IOException if an I/O error occurs.
     */
    public long skip(long n) throws IOException {
        if (vbits == 0) {
            return in.skip(n);
        }
        else {
            int b = (vbits + 7) / 8;
            in.skip(n - b);
            int vbits = this.vbits;
            resetBuffer();
            fillBuffer(vbits);
            return n;
        }
    }

    /**
     *
     * @param n bits to skip
     * @return number of bits skipped
     */
    public int skipBits(int n) throws IOException {
        int k = n;
        int nbits = k % 8;
        read(nbits);
        k -= nbits;
        while (k > 0) {
            try {
                read(8);
                k -= 8;
            }
            catch (IOException ex) {
                break;
            }
        }
        return n;
    }

    public int skipToByteBoundary() throws IOException {
        int nbits = vbits % 8;
        read(nbits);
        return nbits;
    }

    private void fillBuffer(int nbits) throws IOException {
        int c;
        while (vbits < nbits) {
            c = in.read();
            if (c == -1) {
                break;
            }
            if (invertBitOrder) {
                c = flipTable[c] & 0xFF;
            }
            bitbuf = (bitbuf << 8) + (c & 0xFF);
            vbits += 8;
        }
    }

    public int getBitOffset() {
        return 7 - (vbits % 8);
    }

    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
        markBitbuf = bitbuf;
        markVbits = vbits;
    }

    public synchronized void reset() throws IOException {
        in.reset();
        bitbuf = markBitbuf;
        vbits = markVbits;
    }
    static private byte[] flipTable;

    static byte[] getFlipTable() {
        if (flipTable == null) {
            createFlipTable();
        }
        return flipTable;
    }

    static private void createFlipTable() {
        flipTable = new byte[256];
        for (int i = 0; i < flipTable.length; i++) {
            int b = 0;
            for (int j = 0; j < 8; j++) {
                int k = (i >> j) & 1;
                b = (b << 1) | k;
            }
            flipTable[i] = (byte) b;
        }
    }

    public StreamSnapshot makeSnapshot() {
        BisSnapshot snp = new BisSnapshot();
        snp.vbits = vbits;
        snp.bitbuf = bitbuf;
        snp.markBitbuf = markBitbuf;
        snp.markVbits = markVbits;
        snp.bitsToRead = bitsToRead;
        snp.invertBitOrder = invertBitOrder;
        return snp;
    }

    public static class BisSnapshot extends StreamSnapshot {
        int vbits = 0;
        int bitbuf = 0;
        int markBitbuf;
        int markVbits;
        private int bitsToRead = 8;
        boolean invertBitOrder;
    }
}
