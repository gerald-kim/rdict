package com.imagero.uio.blob;

import com.imagero.uio.blob.Blob;
import com.imagero.uio.io.UnexpectedEOFException;
import com.imagero.java.util.Debug;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * BlobInputStream.
 * Read data from Blob.
 *
 * Data from Blob first filled into internal buffer and then read.
 *
 * Date: 23.07.2007
 *
 * @author Andrey Kuznetsov
 */
public class BlobInputStream extends ByteArrayInputStream {
    Blob blob;

    long start;

    boolean finished;

    long position;

    long length;

    public BlobInputStream(Blob blob) throws IOException {
        this(blob, blob.getLength());
    }

    public BlobInputStream(Blob blob, long length) throws IOException {
        super(new byte[2048]);
        this.blob = blob;
        this.length = length;
        fillBuffer();
    }

    public int read() {
        if (finished) {
            return -1;
        }
        if (available() > 0) {
            if(position++ >= length) {
                finished = true;
            }
            return super.read();
        }
        return -1;
    }

    public synchronized long skip(long n) {
        int k = available();
        if (k < n) {
            fillBuffer();
        }
        long skip = super.skip(n);
        position+= skip;
        if(position >= length) {
            finished = true;
        }

        return skip;
    }

    public synchronized int available() {
        int k = super.available();
        if (k > 0) {
            return k;
        } else {
            fillBuffer();
            return super.available();
        }
    }

    public synchronized int read(byte b[], int off, int len) {
        if (finished) {
            return -1;
        }
        int k = available();
        int read = super.read(b, off, Math.min(k, len));
        position += read;
        if(position >= length) {
            finished = true;
        }
        return read;
    }

    private void fillBuffer() {
        try {
            count = blob.get(start, buf);
        } catch (UnexpectedEOFException ex) {
            count = (int) ex.getCount();
        } catch (IOException ex) {
            finished = true;
            Debug.print(ex);
        }
        if (count <= 0) {
            count = 0;
            pos = 0;
            finished = true;
            return;
        }
        start += count;
        pos = 0;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long pos) {
        finished = false;
        start = pos;
        position = pos;
        if(position < length) {
            finished = false;
        }
        else {
            finished = true;
        }
        fillBuffer();
    }
}
