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
package com.imagero.uio.imageio;

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.Transformer;
import com.imagero.uio.Endian;
import com.imagero.uio.io.BitOutputStream;
import com.imagero.uio.io.IOutils;
import com.imagero.uio.io.RandomAccessOutputStream;

import javax.imageio.stream.ImageOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Andrey Kuznetsov
 */
public class UioImageOutputStream extends UioImageInputStream implements ImageOutputStream {

    RandomAccessIO io;

    public UioImageOutputStream(RandomAccessIO ra) {
        super(ra);
        this.io = ra;
        iosFilter = new IOSFilter(ra);
        bos = new IOSBitOutputStream(iosFilter);
    }

    public void write(int b) throws IOException {
        io.write(b);
    }

    public void write(byte b[]) throws IOException {
        io.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        io.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        io.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        io.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        io.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        io.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        io.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        io.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        io.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        io.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        io.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        io.writeChars(s);
    }

    public void writeUTF(String s) throws IOException {
        io.writeUTF(s);
    }

    public void writeShorts(short[] s, int off, int len) throws IOException {
        byte[] buffer = new byte[len * 2];
        Transformer.shortToByte(s, off, len, buffer, 0, in.getByteOrder() == Endian.BIG_ENDIAN);
        io.write(buffer);
    }

    public void writeChars(char[] c, int off, int len) throws IOException {
        byte[] buffer = new byte[len * 2];
        Transformer.charToByte(c, off, len, buffer, 0, in.getByteOrder() == Endian.BIG_ENDIAN);
        io.write(buffer);
    }

    public void writeInts(int[] i, int off, int len) throws IOException {
        byte[] buffer = new byte[len * 4];
        Transformer.intToByte(i, off, len, buffer, 0, in.getByteOrder() == Endian.BIG_ENDIAN);
        io.write(buffer);
    }

    public void writeLongs(long[] l, int off, int len) throws IOException {
        byte[] buffer = new byte[len * 8];
        Transformer.longToByte(l, off, len, buffer, 0, in.getByteOrder() == Endian.BIG_ENDIAN);
        io.write(buffer);
    }

    public void writeFloats(float[] f, int off, int len) throws IOException {
        byte[] buffer = new byte[len * 4];
        Transformer.floatToByte(f, off, len, buffer, 0, in.getByteOrder() == Endian.BIG_ENDIAN);
        io.write(buffer);
    }

    public void writeDoubles(double[] d, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 8];
        Transformer.doubleToByte(d, off, len, buffer,0, in.getByteOrder() == Endian.BIG_ENDIAN);
        io.write(buffer);
    }

    public void writeBit(int bit) throws IOException {
        bos.write(bit, 1);
    }

    public void writeBits(long bits, int numBits) throws IOException {
        int restBits = numBits % 8;
        while (numBits > 8) {
            numBits -= 8;
            int a = (int) ((bits >> numBits) & 0xFF);
            bos.write(a, 8);
        }
        if (restBits > 0) {
            bos.write((int) bits, restBits);
        }
    }

    public void close() throws IOException {
        super.close();
        IOutils.closeStream(bos);
        io = null;
        bos = null;
        iosFilter = null;
    }

    class IOSFilter extends RandomAccessOutputStream {

        public IOSFilter(RandomAccessIO ra) {
            super(ra);
        }

        public IOSFilter(RandomAccessIO ra, long startPos) {
            super(ra, startPos);
        }

        public void write(int b) throws IOException {
            super.write(b);
        }

        long getPosition() {
            return pos;
        }

        void setPosition(long pos) {
            this.pos = pos;
        }
    }

    class IOSBitOutputStream extends BitOutputStream {

        public IOSBitOutputStream(OutputStream out) {
            super(out);
        }

        public void write(int b, int nbits) throws IOException {
            long pos0 = iosFilter.getPosition();
            long streamPosition = getStreamPosition();
            if (vbits != 0 && pos0 != streamPosition) {
                flush();
                iosFilter.setPosition(streamPosition);
            }
            super.write(b, nbits);
        }
    }

    IOSFilter iosFilter;
    IOSBitOutputStream bos;
}
