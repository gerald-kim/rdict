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
import java.io.InputStream;
import java.io.DataInput;

/**
 * @author Andrey Kuznetsov
 */
public interface RandomAccessInput extends Input, DataInput, Endian, Seekable {

    /**
     * Create RandomAccessInput child from given offset
     * @param offset offset in parent stream
     * @param byteOrder byte order for new stream
     * @param syncPointer if true then streams will share same stream position
     * @return RandomAccessInput
     * @throws IOException
     */
    RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) throws IOException;

    /**
     * Create InputStream starting from given offset.
     * @param offset offset in parent (for created InputStream) stream
     * @return InputStream
     */
    InputStream createInputStream(long offset);

    InputStream createInputStream(long offset, long length);

    /**
     * get stream position of child InputStream (created with createInputStream());
     * @param child child InputStream
     * @return Stream position or -1 if supplied InputStream is not a child of this stream
     */
    long getChildPosition(InputStream child);

    long getChildOffset(InputStream child);

    /**
     * set stream position of child InputStream.
     * @param child child InputStream
     * @param position new stream position
     */ 
    void setChildPosition(InputStream child, long position);

    /**
     * Same as readLine, but returns byte array instead of String
     * @return byte array
     * @throws IOException
     */
    byte [] readByteLine() throws IOException;

    /**
     * Same as readByteLine(), but read in given byte array and returns how much was read.
     * @param dest byte array
     * @return how much bytes read
     * @throws IOException
     */
    int readByteLine(byte [] dest) throws IOException;

    /**
     * Same as readShort() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return short
     * @throws IOException
     */
    short readShort(int byteOrder) throws IOException;

    /**
     * Same as readUnsignedShort() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return int
     * @throws IOException
     */
    int readUnsignedShort(int byteOrder) throws IOException;

    /**
     * Same as readChar() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return char
     * @throws IOException
     */
    char readChar(int byteOrder) throws IOException;

    /**
     * Same as readInt() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return int
     * @throws IOException
     */
    int readInt(int byteOrder) throws IOException;

    /**
     * Same as readLong() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return long
     * @throws IOException
     */
    long readLong(int byteOrder) throws IOException;

    /**
     * Same as readFloat() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return float
     * @throws IOException
     */
    float readFloat(int byteOrder) throws IOException;

    /**
     * Same as readDouble() from DataInput, but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return double
     * @throws IOException
     */
    double readDouble(int byteOrder) throws IOException;

    /**
     * Reads four input bytes and returns an long value in the range 0 through 0xFFFFFFFF.
     * @return long
     * @throws IOException
     */
    long readUnsignedInt() throws IOException;

    /**
     * Same as readUnsignedInt(), but uses given byte order instead of streams byte order
     * @param byteOrder byte order
     * @return long
     * @throws IOException
     */
    long readUnsignedInt(int byteOrder) throws IOException;

    boolean isBuffered();
}
