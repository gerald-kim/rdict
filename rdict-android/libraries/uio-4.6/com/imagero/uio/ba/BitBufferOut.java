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
 *
 * 
 * @author Andrey Kuznetsov
 */
public abstract class BitBufferOut {
    public abstract void set(int value, int nbits);

    public static class BitBufferByte extends BitBufferOut {
        int position;
        byte [] out;
        int vbits;
        int buffer;

        public BitBufferByte(byte[] out) {
            this.out = out;
        }

        public void set(int value, int nbits) {
            buffer = (value << vbits) | buffer;
            vbits += nbits;
            while(vbits > 8) {
                out[position++] = (byte) (buffer & 0xFF);
                buffer = buffer >> 8;
                vbits -= 8;
            }
        }

        public void setDestination(byte [] dest) {
            this.out = dest;
            buffer = 0;
            vbits = 0;
        }

        public void seek(int position) {
            this.position = position;
        }

        public void seek(int position, int vbits) {
            this.position = position;
            this.buffer = out[position];
            buffer = buffer >> (8 - vbits);
            this.vbits = vbits;
        }
    }
}
