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
package com.imagero.uio.ba;



/**
 * adds ability to read streams bitewise and also to read predefined amount of bits every read() call
 * @author Andrey Kuznetsov
 */
public class BitInput {

    public static int read(ByteArray ba, Position position, BitState bitState, int nbits) {
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
            return (read(ba, position, bitState, nbits0) << nbits1) | read(ba, position, bitState, nbits1);
        }
        //not anough bits in buffer
        if (nbits > bitState.vbits) {
            fillBuffer(ba, position, bitState, nbits);
        }
        //buffer still empty => we are reached EOF
        if (bitState.vbits == 0) {
            return -1;
        }
        ret = bitState.bitbuf << (32 - bitState.vbits) >>> (32 - nbits);
        bitState.vbits -= nbits;

        if (bitState.vbits < 0) {
            bitState.vbits = 0;
        }

        return ret;
    }

    public static int read(ByteArray ba, Position position, BitState bitState, int nbits, byte b[]) {
        return read(ba, position, bitState, nbits, b, 0, b.length);
    }

    /**
     * Reads data from input stream into an byte array.
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or -1 if the EOF has been reached.
     * @exception NullPointerException if supplied byte array is null
     */
    public static int read(ByteArray ba, Position position, BitState bitState, int nbits, byte b[], int off, int len) {
        if (len <= 0) {
            return 0;
        }
        int c = read(ba, position, bitState, nbits);
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        for (; i < len; ++i) {
            c = read(ba, position, bitState, nbits);
            if (c == -1) {
                break;
            }
            b[off + i] = (byte) c;
        }
        return i;
    }


    /**
     * Skips some bytes from the input stream.
     * If bit buffer is not empty, n - (vbits + 8) / 8 bytes skipped,
     * then buffer is resetted and filled with same amount of bits as it has before skipping.
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     */
    public static long skip(ByteArray ba, Position position, BitState bitState, long n) {
        if (bitState.vbits == 0) {
            return position.skipBytes(n);
        } else {
            int b = (bitState.vbits + 7) / 8;
            position.skipBytes(n - b);
            int vbits = bitState.vbits;
            bitState.resetBuffer();
            fillBuffer(ba, position, bitState, vbits);
            return n;
        }
    }

    /**
     *
     * @param n bits to skip
     * @return number of bits skipped
     */
    public static int skipBits(ByteArray ba, Position position, BitState bitState, int n) {
        int k = n;
        int nbits = k % 8;
        read(ba, position, bitState, nbits);
        k -= nbits;
        while (k > 0) {
            read(ba, position, bitState, 8);
            k -= 8;
        }
        return n;
    }

    public static int skipToByteBoundary(ByteArray ba, Position position, BitState bitState) {
        int nbits = bitState.vbits % 8;
        read(ba, position, bitState, nbits);
        return nbits;
    }

    private static void fillBuffer(ByteArray ba, Position position, BitState bitState, int nbits) {
        int c;
        while (bitState.vbits < nbits) {
            c = ByteArrayInput.read(ba, position);
            if (bitState.invertBitOrder) {
                c = flipTable[c] & 0xFF;
            }
            bitState.bitbuf = (bitState.bitbuf << 8) + (c & 0xFF);
            bitState.vbits += 8;
        }
    }

    static byte[] flipTable;

    static byte[] getFlipTable() {
        if (flipTable == null) {
            createFlipTable();
        }
        return flipTable;
    }

    private  static void createFlipTable() {
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
}
