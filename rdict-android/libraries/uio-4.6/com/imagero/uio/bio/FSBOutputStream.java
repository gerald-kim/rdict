package com.imagero.uio.bio;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
class FSBOutputStream extends OutputStream {
    FixedSizeByteBuffer buffer;
    BufferPosition position;

    public FSBOutputStream(FixedSizeByteBuffer buffer) {
        this(0, buffer);
    }

    public FSBOutputStream(int offset, FixedSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
        position.pos = offset;
    }

    public void write(int b) throws IOException {
        buffer.write(b, position);
    }

    public void write(byte b[]) throws IOException {
        buffer.write(b, 0, b.length, position);
    }

    public void write(byte b[], int off, int len) throws IOException {
        buffer.write(b, off, len, position);
    }

    public void close() throws IOException {
        buffer = null;
    }
}
