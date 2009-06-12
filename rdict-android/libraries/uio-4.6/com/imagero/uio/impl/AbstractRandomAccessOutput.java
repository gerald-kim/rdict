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
package com.imagero.uio.impl;

import com.imagero.uio.RandomAccessOutput;
import com.imagero.uio.RandomAccessInput;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Andrey Kuznetsov
 */
public abstract class AbstractRandomAccessOutput extends OutputStream implements RandomAccessOutput {

    protected int byteOrder = RandomAccessInput.BIG_ENDIAN;

    public final void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public final void writeBoolean(boolean v) throws IOException {
        write(v ? 1 : 0);
    }

    public final void writeByte(int v) throws IOException {
        write(v);
    }

    public final void writeShort(int v) throws IOException {
        writeShort(v, byteOrder);
    }

    public final void writeShort(int v, int byteOrder) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((v >> 8) & 0xFF);
            write(v & 0xFF);
        } else {
            write(v & 0xFF);
            write((v >> 8) & 0xFF);
        }
    }

    public final void writeChar(int v) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((v >> 8) & 0xFF);
            write(v & 0xFF);
        } else {
            write(v & 0xFF);
            write((v >> 8) & 0xFF);
        }
    }

    public final void writeChar(int v, int byteOrder) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((v >> 8) & 0xFF);
            write(v & 0xFF);
        } else {
            write(v & 0xFF);
            write((v >> 8) & 0xFF);
        }
    }

    public final void writeInt(int v) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((v >> 24) & 0xFF);
            write((v >> 16) & 0xFF);
            write((v >> 8) & 0xFF);
            write(v & 0xFF);
        } else {
            write(v & 0xFF);
            write((v >>> 8) & 0xFF);
            write((v >>> 16) & 0xFF);
            write((v >>> 24) & 0xFF);
        }
    }

    public final void writeInt(int v, int byteOrder) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((v >> 24) & 0xFF);
            write((v >> 16) & 0xFF);
            write((v >> 8) & 0xFF);
            write(v & 0xFF);
        } else {
            write(v & 0xFF);
            write((v >>> 8) & 0xFF);
            write((v >>> 16) & 0xFF);
            write((v >>> 24) & 0xFF);
        }
    }

    public final void writeLong(long v) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((int) ((v >> 56) & 0xFF));
            write((int) ((v >> 48) & 0xFF));
            write((int) ((v >> 40) & 0xFF));
            write((int) ((v >> 32) & 0xFF));
            write((int) ((v >> 24) & 0xFF));
            write((int) ((v >> 16) & 0xFF));
            write((int) ((v >> 8) & 0xFF));
            write((int) (v & 0xFF));
        }
        else {
            write((int) v & 0xFF);
            write((int) ((v >> 8) & 0xFF));
            write((int) ((v >>> 16) & 0xFF));
            write((int) ((v >>> 24) & 0xFF));
            write((int) ((v >>> 32) & 0xFF));
            write((int) ((v >>> 40) & 0xFF));
            write((int) ((v >>> 48) & 0xFF));
            write((int) ((v >>> 56) & 0xFF));
        }
    }

    public final void writeLong(long v, int byteOrder) throws IOException {
        if (byteOrder == BIG_ENDIAN) {
            write((int) ((v >> 56) & 0xFF));
            write((int) ((v >> 48) & 0xFF));
            write((int) ((v >> 40) & 0xFF));
            write((int) ((v >> 32) & 0xFF));
            write((int) ((v >> 24) & 0xFF));
            write((int) ((v >> 16) & 0xFF));
            write((int) ((v >> 8) & 0xFF));
            write((int) (v & 0xFF));
        }
        else {
            write((int) v & 0xFF);
            write((int) ((v >> 8) & 0xFF));
            write((int) ((v >>> 16) & 0xFF));
            write((int) ((v >>> 24) & 0xFF));
            write((int) ((v >>> 32) & 0xFF));
            write((int) ((v >>> 40) & 0xFF));
            write((int) ((v >>> 48) & 0xFF));
            write((int) ((v >>> 56) & 0xFF));
        }
    }

    public final void writeFloat(float v) throws IOException {
        writeFloat(v, byteOrder);
    }

    public final void writeFloat(float f, int byteOrder) throws IOException {
        int i = Float.floatToIntBits(f);
        writeInt(i);
    }

    public final void writeDouble(double v) throws IOException {
        writeDouble(v, byteOrder);
    }

    public final void writeDouble(double d, int byteOrder) throws IOException {
        long l = Double.doubleToLongBits(d);
        writeLong(l);
    }

    public final void writeBytes(String s) throws IOException {
        write(s.getBytes());
    }

    public final void writeChars(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            writeChar(s.charAt(i));
        }
    }

    public final void writeUTF(String str) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(str.length());
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeUTF(str);
        dataOut.flush();
        dataOut.close();
        byte[] b = out.toByteArray();
        write(b);
    }

    public boolean isBuffered() {
        return false;
    }
}
