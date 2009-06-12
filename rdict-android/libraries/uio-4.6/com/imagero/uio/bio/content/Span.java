package com.imagero.uio.bio.content;

/**
 * Date: 19.03.2008
 *
 * @author Andrey Kuznetsov
 */
public class Span {
    public final long offset;
    public final long length;

    public Span(long offset, long length) {
        this.offset = offset;
        this.length = length;
    }
}
