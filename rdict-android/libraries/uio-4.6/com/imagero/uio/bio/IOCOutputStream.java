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

import java.io.OutputStream;
import java.io.IOException;

/**
 * OutputStream with shared IOController.
 *
 * @author Andrey Kuznetsov
 */
public class IOCOutputStream extends OutputStream {

    FixedSizeByteBuffer buffer;
    BufferIndex bufferIndex;
    BufferPosition bufferPosition;
    IOController controller;

    StreamPosition streamPosition = new StreamPosition();
    long offset;

    long mark;

    public IOCOutputStream(IOController controller) {
        this.controller = controller;
        controller.streamCount++;
    }

    public IOCOutputStream(IOController controller, long offset) {
        this.controller = controller;
        controller.streamCount++;
        this.offset = offset;
        bufferPosition = new BufferPosition(controller.bufferSize);
        seek(0);
    }

    public void seek(long offset) {
        streamPosition.pos = offset + this.offset;
    }

    protected void prepareBufferForWriting() throws IOException {
        BufferIndex index = controller.getBufferIndex(streamPosition.pos);

        if (!index.equals(bufferIndex) || buffer == null || buffer.buf == null) {
            bufferIndex = index;
            buffer = controller.getBuffer(streamPosition.pos, false);
        }
        bufferPosition.pos = (int) ((streamPosition.pos) % controller.bufferSize);
        buffer.changed = true;
    }

    private void checkBuffer() throws IOException {
        if (buffer == null || !(bufferPosition.available() > 0)) {
            prepareBufferForWriting();
        }
    }

    public void write(int b) throws IOException {
        checkBuffer();
        buffer.write(b, bufferPosition);
        streamPosition.pos++;
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int offset, int length) throws IOException {
        while (length > 0) {
            checkBuffer();
            int written = buffer.write(b, offset, length, bufferPosition);
            length -= written;
            offset += written;
            streamPosition.pos += written;
        }
    }

    public void flush() throws IOException {
        if (controller != null) {
            controller.sync();
        }
    }

    public void close() throws IOException {
        flush();
        if(controller != null) {
            controller.close();
            controller = null;
        }
    }
}
