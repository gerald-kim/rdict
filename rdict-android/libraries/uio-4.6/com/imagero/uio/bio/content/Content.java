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
package com.imagero.uio.bio.content;

import java.io.IOException;

/**
 *
 * @author Andrey Kuznetsov
 */
public abstract class Content {

    /**
     * Load stream content to specified buffer
     * @param offset stream offset
     * @param buffer byte array
     * @return how much bytes were loaded
     * @throws java.io.IOException
     */
    public final int load(long offset, byte[] buffer) throws IOException {
        return load(offset, 0, buffer);
    }

    /**
     * Load stream content to specified buffer
     * @param offset stream offset
     * @param bpos buffer position
     * @param buffer byte array
     * @return how much bytes were loaded
     * @throws java.io.IOException
     */
    public abstract int load(long offset, int bpos, byte[] buffer) throws IOException;

    /**
     * Save buffer content to stream.
     * Not always supported.
     * @param offset stream offset
     * @param bpos buffer position
     * @param buffer byte array
     * @param length how much bytes should be saved
     * @throws java.io.IOException
     */
    public abstract void save(long offset, int bpos, byte[] buffer, int length) throws IOException;

    /**
     * Get stream length. Not always known.
     * @return
     * @throws java.io.IOException
     */
    public abstract long length() throws IOException;

    /**
     * close stream
     */
    public abstract void close();

    /**
     * Determine if data may be reloaded or not.
     * For example: data from InputStream cannot be reloaded,
     * however if content uses file or memory based cache then it is possible to reload data.
     * @return true if data can be reloaded.
     */
    public abstract boolean canReload();

    public abstract boolean writable();

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
