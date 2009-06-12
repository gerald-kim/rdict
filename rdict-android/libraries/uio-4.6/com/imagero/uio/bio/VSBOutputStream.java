package com.imagero.uio.bio;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
class VSBOutputStream extends OutputStream {
    VariableSizeByteBuffer buffer;
    BufferPosition position;

    public VSBOutputStream(VariableSizeByteBuffer buffer) {
        this(0, buffer);
    }

    public VSBOutputStream(int offset, VariableSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
        buffer.seek(offset, position);
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
