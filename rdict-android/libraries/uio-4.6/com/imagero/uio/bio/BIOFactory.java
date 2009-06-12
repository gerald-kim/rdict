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
package com.imagero.uio.bio;

import com.imagero.uio.UIOStreamBuilder;
import com.imagero.uio.bio.content.Content;
import com.imagero.uio.bio.content.DummyContent;
import com.imagero.uio.bio.content.FileCachedInputStreamContent;
import com.imagero.uio.bio.content.MemoryCachedInputStreamContent;
import com.imagero.uio.bio.content.FileCachedHTTPContent;
import com.imagero.uio.bio.content.HTTPContent;
import com.imagero.java.util.Debug;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.net.URL;

/**
 * This class could be removed in the future.
 * Use UIOStreamBuilder to create uio streams.
 * @author Andrey Kuznetsov
 */
public class BIOFactory {

    public static BufferedRandomAccessIO create(int chunkSize) {
        IOController controller = createIOController(chunkSize);
        BufferedRandomAccessIO out = new BufferedRandomAccessIO(controller);
        return out;
    }

    public static BufferedRandomAccessIO create(OutputStream out) {
        return create(out, UIOStreamBuilder.DEFAULT_CHUNK_SIZE);
    }

    public static BufferedRandomAccessIO create(final OutputStream out, int chunkSize) {
        IOController ctrl = createIOController(chunkSize);
        BufferedRandomAccessIO bio = new BufferedRandomAccessIO(ctrl) {
            public void close() throws IOException {
                controller.writeTo(out);
                super.close();
            }

//            public void flush() throws IOException {
//                controller.writeTo(out);
//                controller.flushBefore(getFilePointer());
//            }
        };
        return bio;
    }

    public static BufferedRandomAccessIO create(DataOutput out) {
        return create(out, UIOStreamBuilder.DEFAULT_CHUNK_SIZE);
    }

    public static BufferedRandomAccessIO create(final DataOutput out, int chunkSize) {
        IOController ctrl = createIOController(chunkSize);
        BufferedRandomAccessIO bio = new BufferedRandomAccessIO(ctrl) {
            public void close() throws IOException {
                controller.writeTo(out);
                super.close();
            }

//            public void flush() throws IOException {
//                controller.writeTo(out);
//                controller.flushBefore(getFilePointer());
//            }
        };
        return bio;
    }

    public static IOController createIOController(int chunkSize) {
        Content bc = new DummyContent();
        IOController sb = new IOController(chunkSize, bc);
        return sb;
    }

    public static IOController createIOController(InputStream in, File tmp, int chunkSize) {
        Content bc;
        if (tmp != null) {
            try {
                bc = new FileCachedInputStreamContent(in, tmp);
            } catch (IOException ex) {
                Debug.println("Unable to use file cache, switching to memory cache.", System.err);
                Debug.print(ex);
                bc = new MemoryCachedInputStreamContent(in, chunkSize);
            }
        } else {
            bc = new MemoryCachedInputStreamContent(in, chunkSize);
        }
        IOController sb = new IOController(chunkSize, bc);
        return sb;
    }

    public static IOController createIOController(URL url) {
        return createIOController(url, UIOStreamBuilder.DEFAULT_CHUNK_SIZE);
    }

    public static IOController createIOController(URL url, int chunkSize) {
        return createIOController(url, null, chunkSize);
    }

    public static IOController createIOController(URL url, File tmp) {
        return createIOController(url, tmp, UIOStreamBuilder.DEFAULT_CHUNK_SIZE);
    }

    public static IOController createIOController(URL url, File tmp, int chunkSize) {
        Content bc;
        if (tmp != null) {
            try {
                bc = new FileCachedHTTPContent(url, tmp);
            } catch (IOException ex) {
                Debug.println("Unable to use file cache, switching to memory cache.", System.err);
                Debug.print(ex);
                bc = new HTTPContent(url);
            }
        } else {
            bc = new HTTPContent(url);
        }
        IOController sb = new IOController(chunkSize, bc);
        return sb;
    }
}
