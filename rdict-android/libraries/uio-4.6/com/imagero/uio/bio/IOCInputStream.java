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

import java.io.IOException;
import java.io.InputStream;

/**
 * Independent InputStream with shared IOController.
 *
 * @author Andrey Kuznetsov
 */
public class IOCInputStream extends InputStream {

    FixedSizeByteBuffer buffer;
    BufferIndex bufferIndex;
    BufferPosition bufferPosition;
    IOController controller;

    StreamPosition streamPosition = new StreamPosition();
    long offset;
    long length;

    long mark;

    public IOCInputStream(IOController controller) {
        this.controller = controller;
        controller.streamCount++;
        length = controller.length() - offset;
    }

    public IOCInputStream(IOController controller, long offset) {
        this.controller = controller;
        controller.streamCount++;
        this.offset = offset;
        bufferPosition = new BufferPosition(controller.bufferSize);
        this.length = controller.length() - offset;
        seek(0);
    }

    public IOCInputStream(IOController controller, long offset, long length) {
        this.controller = controller;
        controller.streamCount++;
        this.offset = offset;
        bufferPosition = new BufferPosition(Math.min(controller.bufferSize, (int) ((length + (offset % controller.bufferSize)) & Integer.MAX_VALUE)));
        this.length = Math.min(length, controller.length() - offset);
        seek(0);
    }

    public void seek(long offset) {
        streamPosition.pos = offset + this.offset;
        bufferPosition.pos = (int) ((streamPosition.pos) % controller.bufferSize);
    }

    private void prepareBufferForReading() {
        BufferIndex index = controller.getBufferIndex(streamPosition.pos);

        if (!index.equals(bufferIndex) || buffer == null || buffer.buf == null) {
            bufferIndex = index;
            try {
                buffer = controller.getBuffer(streamPosition.pos, true);
            } catch (IOException ex) {
                //ignore
            }
        }
        if (buffer != null) {
            bufferPosition.pos = (int) ((streamPosition.pos) % controller.bufferSize);
        }
    }

    public int read() throws IOException {
        checkBuffer();

        if (buffer != null) {
            streamPosition.pos++;
            return buffer.read(bufferPosition);
        }
        return -1;
    }

    public int available() throws IOException {
        if (buffer != null) {
            int av = buffer.availableForReading(bufferPosition);
            return Math.max(0, Math.min(av, (int) (length - getPosition()) & Integer.MAX_VALUE));
        }
        return 0;
    }

    private void checkBuffer() {
        if (buffer == null || bufferPosition.available() <= 0) {
            prepareBufferForReading();
        }
    }

    public long skip(long n) throws IOException {
        checkBuffer();
        if (buffer == null) {
            return 0;
        }
        long skp = buffer.skip(n, bufferPosition);
        streamPosition.pos += skp;
        return skp;
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int offset, int length) throws IOException {
        checkBuffer();
        if (buffer == null) {
            return -1;
        }
        int av = available();
        if (av == 0) {
            return 0;
        }
        int rc = buffer.read(b, offset, Math.min(av, length), bufferPosition);
        if (rc > 0) {
            streamPosition.pos += rc;
        }
        return rc;
    }

    public boolean markSupported() {
        return true;
    }

    public synchronized void mark(int readlimit) {
        this.mark = streamPosition.pos;
    }

    public synchronized void reset() throws IOException {
        seek(mark);
    }

    public void close() throws IOException {
        if (controller != null) {
            controller.close();
            controller = null;
        }
    }

    public long getPosition() {
        return streamPosition.pos - offset;
    }

    public long getOffset() {
        return offset;
    }

    public long getAbsolutePosition() {
        return streamPosition.pos;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
