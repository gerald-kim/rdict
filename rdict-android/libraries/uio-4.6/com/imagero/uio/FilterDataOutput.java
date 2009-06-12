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
 package com.imagero.uio;

import java.io.DataOutput;
import java.io.IOException;

public class FilterDataOutput implements DataOutput {
    /**
     * The underlying output to be filtered.
     */
    protected DataOutput out;

    public FilterDataOutput(DataOutput out) {
        this.out = out;
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if ((off | len | (b.length - (len + off)) | (off + len)) < 0)
            throw new IndexOutOfBoundsException();

        for (int i = 0; i < len; i++) {
            write(b[off + i]);
        }
    }

    public void writeBoolean(boolean v) throws IOException {
        out.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        out.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        out.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        out.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        out.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        out.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        out.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        out.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        out.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        out.writeChars(s);
    }

    public void writeUTF(String str) throws IOException {
        out.writeUTF(str);
    }
}
