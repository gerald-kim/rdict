/*
 * Copyright (c) Andrey Kuznetsov. All Rights Reserved.
 *
 * http://res.imagero.com
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
package com.imagero.uio.io;

import java.io.IOException;
import java.io.Reader;

/**
 * A character stream whose source is a string array.
 * Very similar to java.io.StringReader.
 * @author Andrey Kuznetsov
 */
public class StringArrayReader extends Reader {

    private String[] str;
    private int[] ends;
    private int[] starts;
    private int length;
    private int next = 0;
    private int mark = 0;

    int current;

    /**
     * Create a new StringArrayReader.
     *
     * @param s  String array providing the character stream.
     */
    public StringArrayReader(String[] s) {
        this.str = s;
        for (int i = 0; i < s.length; i++) {
            length += s[i].length();
        }

        starts = new int[s.length];
        for (int i = 1; i < s.length; i++) {
            starts[i] = starts[i - 1] + s[i - 1].length();
        }

        ends = new int[s.length];
        ends[0] = s[0].length();
        for (int i = 1; i < s.length; i++) {
            ends[i] = ends[i - 1] + s[i].length();
        }
    }

    /**
     * Check to make sure that the stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (str == null) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Read a single character.
     * @return The character read, or -1 if the end of the stream has been reached
     * @exception IOException  If an I/O error occurs
     */
    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next >= length) {
                return -1;
            }
            if (next >= ends[current]) {
                current++;
            }
            return str[current].charAt(next++ - starts[current]);
        }
    }

    /**
     * Read characters into a portion of supplied char array.
     *
     * @param cbuf Destination char array
     * @param off Where to start writing characters
     * @param len Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the stream has been reached
     * @exception IOException If an I/O error occurs
     */
    public int read(char cbuf[], int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            }
            else if (len == 0) {
                return 0;
            }
            if (next >= length) {
                return -1;
            }
            if (next >= ends[current]) {
                current++;
            }
            int n = Math.min(ends[current] - next, len);
            str[current].getChars(next - starts[current], next - starts[current] + n, cbuf, off);
            next += n;
            return n;
        }
    }

    /**
     * Skip characters.
     * @exception  IOException  If an I/O error occurs
     */
    public long skip(long ns) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next >= length) {
                return 0;
            }
            if (next >= ends[current]) {
                current++;
            }
            long n = Math.min(ends[current] - next, ns);
            next += n;
            return n;
        }
    }

    /**
     * Tell whether this stream is ready to be read.
     * @return True if the next read() is guaranteed not to block for input
     * @exception IOException If the stream is closed
     */
    public boolean ready() throws IOException {
        synchronized (lock) {
            ensureOpen();
            return true;
        }
    }

    /**
     * Tell whether this stream supports the mark() operation.
     * @return true
     */
    public boolean markSupported() {
        return true;
    }

    /**
     * Mark the present position in the stream.
     * Subsequent calls to reset() will reposition the stream to this point.
     * @param  readAheadLimit Limit on the number of characters that may be
     * read while still preserving the mark.
     * @exception IllegalArgumentException If readAheadLimit is < 0
     * @exception IOException If an I/O error occurs
     */
    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (lock) {
            ensureOpen();
            mark = next;
        }
    }

    /**
     * Reset the stream to the most recent mark,
     * or to the beginning of the string if it has never been marked.
     * @exception IOException If an I/O error occurs
     */
    public void reset() throws IOException {
        synchronized (lock) {
            ensureOpen();
            next = mark;
            if (starts[current] > next) {
                current++;
            }
        }
    }

    /**
     * Close the stream.
     */
    public void close() {
        str = null;
    }
}
