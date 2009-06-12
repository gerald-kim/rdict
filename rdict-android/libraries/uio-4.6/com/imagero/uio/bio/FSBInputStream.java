package com.imagero.uio.bio;

import java.io.InputStream;
import java.io.IOException;

/**
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
class FSBInputStream extends InputStream {
    FixedSizeByteBuffer buffer;
    BufferPosition position;
    int mark;
    long offset;

    public FSBInputStream(FixedSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(buffer.buf.length);
    }

    public FSBInputStream(int offset, FixedSizeByteBuffer buffer) {
        this.buffer = buffer;
        position = new BufferPosition(Integer.MAX_VALUE);
        position.pos = offset;
        this.offset = offset;
    }

    public FSBInputStream(int offset, FixedSizeByteBuffer buffer, int length) {
        this.buffer = buffer;
        position = new BufferPosition(length);
        position.pos = offset;
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
        position.pos = mark;
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
        position.pos = (int) (pos + offset);
    }
}
