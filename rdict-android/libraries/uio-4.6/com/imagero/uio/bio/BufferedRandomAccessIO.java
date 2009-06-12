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
package com.imagero.uio.bio;

import com.imagero.uio.RandomAccessIO;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.RandomAccessOutput;
import com.imagero.uio.impl.AbstractRandomAccessIO;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * BufferedRandomAccessIO - buffered readable and writable stream with random access.
 * @author Andrey Kuznetsov
 */
public class BufferedRandomAccessIO extends AbstractRandomAccessIO {

    FixedSizeByteBuffer buffer;
    BufferIndex bufferIndex;
    BufferPosition bufferPosition;
    IOController controller;

    StreamPosition streamPosition = new StreamPosition();
    long offset;


    public BufferedRandomAccessIO(IOController controller) {
        this(controller, 0L);
    }

    public BufferedRandomAccessIO(IOController controller, long offset) {
        this.controller = controller;
        this.controller.streamCount++;
        this.offset = offset;
        bufferPosition = new BufferPosition(controller.bufferSize);
        seek(0);
    }

    public final void setLength(long newLength) throws IOException {
        controller.setLength(newLength);
    }

    public long flushBefore(long pos) {
        return controller.flushBefore(pos);
    }

    protected void prepareBufferForReading(BufferIndex index) throws IOException {
        if (!index.equals(bufferIndex) || buffer == null || buffer.buf == null) {
            bufferIndex = index;
            buffer = controller.getBuffer(streamPosition.pos, true);
        }
        bufferPosition.pos = (int) ((streamPosition.pos) % controller.bufferSize);
    }

    protected void prepareBufferForWriting(BufferIndex index) throws IOException {
        if (!index.equals(bufferIndex) || buffer == null || buffer.buf == null) {
            bufferIndex = index;
            buffer = controller.getBuffer(streamPosition.pos, false);
        }
        bufferPosition.pos = (int) ((streamPosition.pos) % controller.bufferSize);
        buffer.changed = true;
    }

    public long getFilePointer() {
        return streamPosition.pos - offset;
    }

    public long length() throws IOException {
        return controller.length() - offset;
    }

    public void seek(long pos) {
        if(pos < 0) {
            throw new IllegalArgumentException("Negative seek offset");
        }
        streamPosition.pos = pos + offset;
        bufferPosition.pos = (int) ((streamPosition.pos) % controller.bufferSize);
    }

    public int available() throws IOException {
        if (buffer != null) {
            return buffer.availableForReading(bufferPosition);
        }
        return 0;
    }

    public void write(int b) throws IOException {
        ensureBuffer(false);
        buffer.write(b, bufferPosition);
        streamPosition.pos++;
    }

    public void write(byte b[], int offset, int length) throws IOException {
        while (length > 0) {
            ensureBuffer(false);
            int written = buffer.write(b, offset, length, bufferPosition);
            length -= written;
            offset += written;
            streamPosition.pos += written;
        }
    }

    public void close() throws IOException {
        flush();
        if (controller != null) {
            controller.close();
            controller = null;
        }
    }

    /**
     * write buffer contents to given OutputStream
     * @param out OutputStream
     */
    public void writeBuffer(OutputStream out) throws IOException {
        controller.writeTo(out);
    }

    /**
     * write buffer contents to DataOutput
     * @param out OutputStream
     */
    public void writeBuffer(DataOutput out) throws IOException {
        controller.writeTo(out);
    }

    private void ensureBuffer(boolean read) throws IOException {
        BufferIndex index = controller.getBufferIndex(streamPosition.pos);
        if (read) {
            if (buffer == null || buffer.availableForReading(bufferPosition) <= 0 || bufferIndex != index) {
                prepareBufferForReading(index);
            }
        } else {
            if (buffer == null || buffer.availableForWriting(bufferPosition) <= 0 || bufferIndex != index) {
                prepareBufferForWriting(index);
            }
        }
    }

    public int read() throws IOException {
        try {
            ensureBuffer(true);
        } catch (IOException ex) {
            return -1;
        }
        if (buffer != null) {
            streamPosition.pos++;
            return buffer.read(bufferPosition);
        }
        return -1;
    }

    public long skip(long n) throws IOException {
        ensureBuffer(true);
        if (buffer == null) {
            return 0;
        }
        long skipped = buffer.skip(n, bufferPosition);
        streamPosition.pos += skipped;
        return skipped;
    }

    public int read(byte[] b, int offset, int length) throws IOException {
        ensureBuffer(true);
        if (buffer == null) {
            return 0;
        }
        int rc = buffer.read(b, offset, length, bufferPosition);
        if (rc > 0) {
            streamPosition.pos += rc;
        }
        return rc;
    }

    public RandomAccessIO createIOChild(long offset, long length, int byteOrder, boolean syncPointer) {
        BufferedRandomAccessIO io = new BufferedRandomAccessIO(controller, this.offset + offset);
        io.setByteOrder(byteOrder);
        if (syncPointer) {
            io.streamPosition = streamPosition;
        }
        return io;
    }

    public RandomAccessInput createInputChild(long offset, long length, int byteOrder, boolean syncPointer) {
        return createIOChild(offset, 0, byteOrder, syncPointer);
    }

    public InputStream createInputStream(long offset) {
        return new IOCInputStream(controller, this.offset + offset);
    }

    public InputStream createInputStream(long offset, long length) {
        return new IOCInputStream(controller, this.offset + offset, length);
    }

    public RandomAccessOutput createOutputChild(long offset, int byteOrder, boolean syncPointer) {
        return createIOChild(offset, 0, byteOrder, syncPointer);
    }

    public OutputStream createOutputStream(long offset) {
        return new IOCOutputStream(controller, this.offset + offset);
    }

    public void flush() throws IOException {
        if(controller != null) {
            controller.sync();
        }
    }

    public boolean isBuffered() {
        return true;
    }

    public long getChildPosition(InputStream child) {
        if(child instanceof IOCInputStream) {
            IOCInputStream in = (IOCInputStream) child;
            return in.getPosition();
        }
        return -1;
    }

    public long getChildOffset(InputStream child) {
        if(child instanceof IOCInputStream) {
            IOCInputStream in = (IOCInputStream) child;
            return in.getOffset();
        }
        return -1;
    }

    public void setChildPosition(InputStream child, long position) {
        if (child instanceof IOCInputStream) {
            IOCInputStream in = (IOCInputStream) child;
            in.seek(position);
        }
    }
}
