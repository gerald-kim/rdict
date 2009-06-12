package com.imagero.uio.io;

import java.io.EOFException;

/**
 * If EOFException is thrown during readFully() it is still useful to know how much bytes were read.
 * Date: 22.12.2007
 *
 * @author Andrey Kuznetsov
 */
public class UnexpectedEOFException extends EOFException {
    private long count;

    public UnexpectedEOFException(long count) {
        this("EOF", count);
    }

    public UnexpectedEOFException(String s, long count) {
        super(s);
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
