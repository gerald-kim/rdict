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
package com.imagero.uio.imageio.spi;

import com.imagero.uio.imageio.UioImageInputStream;
import com.imagero.uio.RandomAccessInput;
import com.imagero.uio.UIOStreamBuilder;
import com.imagero.uio.impl.RandomAccessFileX;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.util.Locale;

/**
 * @author Andrey Kuznetsov
 */
public class UioRAFImageInputStreamSpi extends ImageInputStreamSpi {

    private static final String vendorName = "imagero.com (Andrey Kuznetsov)";

    private static final String version = "0.9";

    private static final Class inputClass = com.imagero.uio.impl.RandomAccessFileX.class;

    public UioRAFImageInputStreamSpi() {
        super(vendorName, version, inputClass);
    }

    public String getDescription(Locale locale) {
        return "Service provider that instantiates a UioImageInputStream from a com.imagero.uio.impl.RandomAccessFileX";
    }

    public ImageInputStream createInputStreamInstance(Object input, boolean useCache, File cacheDir) {
        if (input instanceof RandomAccessFileX) {
            try {
                RandomAccessFileX raf = (RandomAccessFileX) input;
                RandomAccessInput ro = new UIOStreamBuilder(raf).setBuffered(true).create();
                return new UioImageInputStream(ro);
            }
            catch (Exception ex) {
                return null;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
