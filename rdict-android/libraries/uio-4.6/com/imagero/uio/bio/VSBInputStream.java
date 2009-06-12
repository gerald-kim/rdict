package com.imagero.uio.bio;

import java.io.InputStream;
import java.io.IOException;

/**
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
class VSBInputStream extends InputStream {
    VariableSizeByteBuffer buffer;
    BufferPosition position;
    int mark;
    long offset;

    public VSBInputStream(VariableSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
    }

    public VSBInputStream(VariableSizeByteBuffer buffer, int length) {
        this.buffer = buffer;
        position = new BufferPosition(length);
    }

    public VSBInputStream(int offset, VariableSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
        buffer.seek(offset, position);
        this.offset = offset;
    }

    public VSBInputStream(int offset, VariableSizeByteBuffer buffer, int length) {
        this.buffer = buffer;
        position = new BufferPosition(length + offset);
        buffer.seek(offset, position);
        this.offset = offset;
    }

    public int read() {
        return buffer.read(position);
    }

    public int read(byte b[]) throws IOException {
        return buffer.read(b, 0, b.length, position);
    }

    public int read(byte b[], int off, int len) {
        return buffer.read(b, off, len, position);
    }

    public long skip(long n) {
        return buffer.skip(n, position);
    }

    public int available() {
        return buffer.availableForReading(position);
    }

    public synchronized void mark(int readlimit) {
        this.mark = position.pos;
    }

    public synchronized void reset() throws IOException {
        buffer.seek(mark, position);
    }

    public boolean markSupported() {
        return true;
    }

    public long getPosition() {
        return position.pos - offset;
    }

    public long getOffset() {
        return offset;
    }

    public void setPosition(long pos) {
        buffer.seek((int) Math.min(Integer.MAX_VALUE, pos + offset), position);
    }
}
