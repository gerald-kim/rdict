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

import com.imagero.uio.io.IOutils;
import com.imagero.uio.io.UnexpectedEOFException;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 05.01.2008
 *
 * @author Andrey Kuznetsov
 */
public class HTTPContent extends Content {
    URL url;

    long length;

    public HTTPContent(URL url) {
        String protocol = url.getProtocol();
        if (!"http".equalsIgnoreCase(protocol)) {
            throw new IllegalArgumentException("http protokol only");
        }
        this.url = url;
    }

    public int load(long offset, int bpos, byte[] buffer) throws IOException {
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.setAllowUserInteraction(true);
        httpcon.setDoInput(true);
        httpcon.setDoOutput(true);
        httpcon.setRequestMethod("GET");
        httpcon.setUseCaches(false);
        httpcon.setRequestProperty("Range", "bytes=" + offset + "-" + (offset + buffer.length - bpos));
        httpcon.connect();

        int responseCode = httpcon.getResponseCode();
        if (responseCode != 206) {
            httpcon.disconnect();
            throw new IOException("byteserving not supported by server");
        }
        InputStream in = httpcon.getInputStream();

        int count = 0;
        try {
            int len = buffer.length - bpos;
            IOutils.readFully(in, buffer, bpos, len);
            count = len;
        } catch (UnexpectedEOFException ex) {
            count = (int) ex.getCount();
        } finally {
            httpcon.disconnect();
        }
        return count;
    }

    public void close() {
    }

    public void save(long offset, int bpos, byte[] buffer, int length) throws IOException {
    }

    public long length() throws IOException {
        if (length == 0) {
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.setRequestMethod("HEAD");
            httpcon.setUseCaches(false);
            httpcon.connect();
            length = httpcon.getContentLength();
            httpcon.disconnect();
        }
        return length;
    }

    public boolean canReload() {
        return false;
    }

    public boolean writable() {
        return false;
    }
}
