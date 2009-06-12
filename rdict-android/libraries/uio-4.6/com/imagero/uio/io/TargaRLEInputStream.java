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
package com.imagero.uio.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andrey Kuznetsov
 */
public class TargaRLEInputStream extends RLEInputStream implements Snapable {

    private int numSamples;
    private byte[] value;
    private boolean rawPacket;
    private int pixelSize;
    private int vindex;

    /**
     * create TargaRLEInputStream
     * @param in InputStream
     * @param pixelSize pixel size in bytes (bytes per pixel)
     */
    public TargaRLEInputStream(InputStream in, int pixelSize) {
        super(in);
        this.pixelSize = pixelSize;
        value = new byte[pixelSize];
    }

    public TargaRLEInputStream(InputStream in, TargaRLE_Snapshot sn) {
        super(in);
        this.numSamples = sn.numSamples;
        this.value = (byte[]) sn.value.clone();
        this.rawPacket = sn.rawPacket;
        this.pixelSize = sn.pixelSize;
        this.vindex = sn.vindex;
    }

    /**
     * Make snaphot of internal state of TargaRLEInputStream
     * @return Snaphot
     */
    public StreamSnapshot makeSnapshot() {
        TargaRLE_Snapshot sn = new TargaRLE_Snapshot();
        sn.numSamples = numSamples;
        sn.value = (byte[]) value.clone();
        sn.rawPacket = rawPacket;
        sn.pixelSize = pixelSize;
        sn.vindex = vindex;
        return sn;
    }
 
    public int read() throws IOException {
        if (numSamples == 0) {
            int v = in.read();
            if (v == -1) {
                return -1;
            }
            if ((v >> 7) == 1) {
                for (int i = 0; i < value.length; i++) {
                    value[i] = (byte) in.read();
                }
                numSamples = ((v & 0x7F) + 1) * pixelSize;
                rawPacket = false;
            }
            else {
                numSamples = (v + 1) * pixelSize;
                rawPacket = true;
            }
        }
        numSamples--;
        if (rawPacket) {
            return in.read();
        }
        else {
            int b = value[vindex++] & 0xFF;
            if (vindex == pixelSize) {
                vindex = 0;
            }
            return b;
        }
    }

    public static class TargaRLE_Snapshot extends StreamSnapshot {
        private int numSamples;
        private byte[] value;
        private boolean rawPacket;
        private int pixelSize;
        private int vindex;
    }
}
