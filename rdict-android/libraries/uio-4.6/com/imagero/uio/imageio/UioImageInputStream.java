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

import com.imagero.uio.io.RandomAccessInputStream;
import com.imagero.uio.io.BitInputStream;
import com.imagero.uio.io.IOutils;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.Transformer;
import com.imagero.uio.Endian;
import com.imagero.uio.bio.BufferedRandomAccessIO;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.IIOByteBuffer;
import java.nio.ByteOrder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * @author Andrey Kuznetsov
 */
public class UioImageInputStream implements ImageInputStream {

    RandomAccessInput in;
    IISFilter iisFilter;
    IISBitInpitStream bis;

    int bitOffset;

    public UioImageInputStream(RandomAccessInput ro) {
        this.in = ro;
        iisFilter = new IISFilter(ro);
        bis = new IISBitInpitStream(iisFilter);
    }

    public void setByteOrder(ByteOrder byteOrder) {
        int bOrder = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            bOrder = RandomAccessInput.BIG_ENDIAN;
        }
        else if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            bOrder = RandomAccessInput.LITTLE_ENDIAN;
        }
        else {
            throw new IllegalArgumentException(String.valueOf(byteOrder));
        }
        in.setByteOrder(bOrder);
    }

    public ByteOrder getByteOrder() {
        switch (in.getByteOrder()) {
            case RandomAccessInput.BIG_ENDIAN:
                return ByteOrder.BIG_ENDIAN;
            case RandomAccessInput.LITTLE_ENDIAN:
                return ByteOrder.LITTLE_ENDIAN;
            default:
                throw new RuntimeException("Unknown byte order");
        }
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(byte[] b) throws IOException {
        return in.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public void readBytes(IIOByteBuffer buf, int len) throws IOException {
        if (len < 0) {
            throw new IllegalArgumentException("length can't negative:" + len);
        }
        buf.setOffset(0);
        byte[] b = new byte[len];
        in.readFully(b);
        buf.setData(b);
        buf.setLength(len);
    }

    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    public byte readByte() throws IOException {
        return in.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return in.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return in.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return in.readChar();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public long readUnsignedInt() throws IOException {
        return ((long) in.readInt()) & 0xffffffff;
    }

    public long readLong() throws IOException {
        return in.readLong();
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public double readDouble() throws IOException {
        return in.readDouble();
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public String readUTF() throws IOException {
        return in.readUTF();
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        in.readFully(b, off, len);
    }

    public void readFully(byte[] b) throws IOException {
        in.readFully(b);
    }

    public void readFully(short[] s, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 2];
        in.readFully(buffer);
        Transformer.byteToShort(buffer, 0, s.length, s, off, in.getByteOrder() == Endian.BIG_ENDIAN);
    }

    public void readFully(char[] c, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 2];
        in.readFully(buffer);
        Transformer.byteToChar(buffer, 0, c.length, c, off, in.getByteOrder() == Endian.BIG_ENDIAN);
    }

    public void readFully(int[] i, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 4];
        in.readFully(buffer);
        Transformer.byteToInt(buffer, 0, i.length, i, off, in.getByteOrder() == Endian.BIG_ENDIAN);
    }

    public void readFully(long[] l, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 8];
        in.readFully(buffer);
        Transformer.byteToLong(buffer, 0, l.length, l, off, in.getByteOrder() == Endian.BIG_ENDIAN);
    }

    public void readFully(float[] f, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 4];
        in.readFully(buffer);
        Transformer.byteToFloat(buffer, 0, f.length, f, off, in.getByteOrder() == Endian.BIG_ENDIAN);
    }

    public void readFully(double[] d, int off, int len) throws IOException {
        byte [] buffer = new byte[len * 5];
        in.readFully(buffer);
        Transformer.byteToDouble(buffer, 0, d.length, d, off, in.getByteOrder() == Endian.BIG_ENDIAN);
    }

    public long getStreamPosition() throws IOException {
        return in.getFilePointer();
    }

    public int getBitOffset() throws IOException {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) throws IOException {
        if (bitOffset < 0 || bitOffset > 7) {
            throw new IllegalArgumentException("Illegal bitOffset:" + bitOffset);
        }
        this.bitOffset = bitOffset;
    }

    public int readBit() throws IOException {
        return bis.read(1);
    }

    public long readBits(int numBits) throws IOException {
        long result = 0;
        while(numBits > 8) {
            result = (result << 8) | bis.read(8);
            numBits -= 8;
        }
        result = (result << numBits) | bis.read(numBits);
        return 0;
    }

    public long length() throws IOException {
        return in.length();
    }

    public int skipBytes(int n) throws IOException {
        return in.skipBytes(n);
    }

    public long skipBytes(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        long len = in.length();
        long pos = in.getFilePointer();
        long newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        in.seek(newpos);
        return (int) (newpos - pos);
    }

    public void seek(long pos) throws IOException {
        in.seek(pos);
    }

    Stack markStack = new Stack();

    static class Mark {
        long position;
        int bitOffset;

        public Mark(long position, int bitOffset) {
            this.position = position;
            this.bitOffset = bitOffset;
        }
    }

    public void mark() {
        try {
            Mark mark = new Mark(in.getFilePointer(), bitOffset);
            markStack.push(mark);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reset() throws IOException {
        if (markStack.empty()) {
            return;
        }

        Mark mark = (Mark) markStack.pop();
        in.seek(mark.position);
        setBitOffset(mark.bitOffset);
    }

    long flushedPosition;

    /**
     * Discards the initial portion of the stream prior to the
     * indicated postion.
     */
    public void flushBefore(long pos) throws IOException {
        if(in instanceof BufferedRandomAccessIO) {
            BufferedRandomAccessIO bio = (BufferedRandomAccessIO) in;
            flushedPosition = bio.flushBefore(pos);
        }
    }

    public void flush() throws IOException {
        flushBefore(in.getFilePointer());
    }

    public long getFlushedPosition() {
        return flushedPosition;
    }

    public boolean isCached() {
        return true;
    }

    public boolean isCachedMemory() {
        return true;
    }

    public boolean isCachedFile() {
        return false;
    }

    public void close() throws IOException {
        flush();
        IOutils.closeStream(in);
        IOutils.closeStream(bis);
        in = null;
        bis = null;
        iisFilter = null;
    }

    boolean clearBitBuffer;

    class IISFilter extends RandomAccessInputStream {

        public IISFilter(RandomAccessInput ro) {
            super(ro);
        }

        public IISFilter(RandomAccessInput ro, long startPos) {
            super(ro, startPos);
        }

        protected void checkPos() throws IOException {
            if(pos != ro.getFilePointer()) {
                bitOffset = 0;
                clearBitBuffer = true;
                pos = ro.getFilePointer();
            }
        }
    }

    class IISBitInpitStream extends BitInputStream {

        public IISBitInpitStream(InputStream in) {
            super(in);
        }

        public int read(int nbits) throws IOException {
            if(clearBitBuffer) {
                resetBuffer();
            }
            if(bitOffset != 0) {
                super.read(bitOffset);
            }
            return super.read(nbits);
        }
    }
}
