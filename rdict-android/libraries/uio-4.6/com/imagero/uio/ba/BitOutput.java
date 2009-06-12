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

package com.imagero.uio.ba;


/**
 * adds ability to write streams bitewise
 * @author Andrey Kuznetsov
 */
public class BitOutput {

    protected static final int[] mask = new int[33];

    static {
        for (int i = 0; i < mask.length; i++) {
            mask[i] = (1 << (i + 1)) - 1;
        }
    }

    /**
     * Writes some bits from the specified int to stream.
     * @param b value which should be written
     * @param nbits bit count to write
     */
    public static void write(ByteArray ba, Position position, BitState bitState, long b, int nbits) {
        if (nbits == 0) {
            return;
        }
        if (bitState.vbits + nbits > 31) {
            write(ba, position, bitState, b >> 8, nbits - 8);
            write(ba, position, bitState, b, 8);
        } else {
            final int k = (int) (b & mask[nbits - 1]);
            bitState.bitbuf = (bitState.bitbuf << nbits) | k;
            bitState.vbits += nbits;

            write8(ba, position, bitState);
        }
    }

    public static void write(ByteArray ba, Position position, BitState bitState, int b, int nbits) {
        if (nbits == 0) {
            return;
        }
        if (bitState.vbits + nbits > 31) {
            write(ba, position, bitState, b >> 8, nbits - 8);
            write(ba, position, bitState, b, 8);
        } else {
            final int k = (b & mask[nbits - 1]);
            bitState.bitbuf = (bitState.bitbuf << nbits) | k;
            bitState.vbits += nbits;

            write8(ba, position, bitState);
        }
    }

    private static int write8(ByteArray ba, Position position, BitState bitState) {
        int res = 0;
        while (bitState.vbits > 8) {
            int c = (bitState.bitbuf << (32 - bitState.vbits) >>> 24);
            bitState.vbits -= 8;
            if (bitState.invertBitOrder) {
                c = BitInput.flipTable[c] & 0xFF;
            }
            ByteArrayOutput.write(ba, position, c);
            res++;
        }
        return res;
    }

    /**
     * writes bits from buffer to output stream
     * @return flushed bit count
     */
    public static int flush(ByteArray ba, Position position, BitState bitState) {
//        int a = write8(ba, position, bitState);   //rather not needed, just to ensure
        int vbits = bitState.vbits;
        if (vbits > 0) {
            write(ba, position, bitState, ((ba.buffer[position.position] << vbits) & 0xFF) >> vbits, 8 - vbits);
        }
        bitState.vbits = 0;
        bitState.bitbuf = 0;
        return vbits;
    }

    public static long skip(ByteArray ba, Position position, BitState bitState, long n) {
        if (bitState.vbits == 0) {
            return position.skipBytes(n);
        } else {
            final int vbits = bitState.vbits;
            skipBits(ba, position, bitState, vbits);
            //vbits must be 0 now, so we can skip whole bytes
            n = position.skipBytes(n - 1) + 1;
            //get top bits
            int top = ba.buffer[position.position] >> (8 - vbits);
            //and fill bit buffer
            BitOutput.write(ba, position, bitState, top, vbits);
            return n;
        }
    }

    public static void skipBits(ByteArray ba, Position position, BitState bitState, int n) {
        if (n > 8) {
            throw new IllegalArgumentException("Too many bits to skip:" + n);
        }
        //clone position
        Position p = new Position(position);
        //and bit state
        BitState bs = new BitState(bitState);
        //fill bit buffer
        int a = BitInput.read(ba, p, bs, 8);
        //throw away top bits
        a = ((a << n) & 0xFF) >> n;
        //write bottom bits
        BitOutput.write(ba, position, bitState, a, 8 - n);
    }
}
