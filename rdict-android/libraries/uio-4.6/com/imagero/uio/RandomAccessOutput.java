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

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutput;


/**
 * @author Andrey Kuznetsov
 */
public interface RandomAccessOutput extends DataOutput, Endian, Seekable {

    RandomAccessOutput createOutputChild(long offset, int byteOrder, boolean syncPointer) throws IOException;
    OutputStream createOutputStream(long offset);
    void flush() throws IOException;

    void writeShort(int v, int byteOrder) throws IOException;

    void writeChar(int v, int byteOrder) throws IOException;

    void writeInt(int v, int byteOrder) throws IOException;

    void writeLong(long v, int byteOrder) throws IOException;

    void writeFloat(float v, int byteOrder) throws IOException;

    void writeDouble(double v, int byteOrder) throws IOException;

    /**
     * Set length of stream.
     * @param newLength new stream length
     * @throws IOException
     */
    void setLength(long newLength) throws IOException;
}
