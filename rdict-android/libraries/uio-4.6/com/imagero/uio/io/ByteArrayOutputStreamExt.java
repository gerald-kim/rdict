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

import com.imagero.java.event.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;

/**
 * ByteArrayOutputStreamExt extends ByteArrayOutputStream with possibility to drain off data into user specified buffer.
 * ActionEvent is fired if buffer is full and going to grow.
 * @author Andrey Kuznetsov
 */
public class ByteArrayOutputStreamExt extends ByteArrayOutputStream {

    private MyEventSource eventSource = new MyEventSource(this);
    public static final String BUFFER_FULL = "buffer full";
    ActionEvent e = new ActionEvent(eventSource, BUFFER_FULL);
    boolean hasListener;

    public ByteArrayOutputStreamExt() {
        this(1024);
    }

    public ByteArrayOutputStreamExt(int size) {
        super(size);
    }

    public ByteArrayOutputStreamExt(ActionListener l) {
        if(l != null) {
            eventSource.addEventListener(l);
            hasListener = true;
        }
    }

    public ByteArrayOutputStreamExt(int size, ActionListener l) {
        super(size);
        if(l != null) {
            eventSource.addEventListener(l);
            hasListener = true;
        }
    }

    public void setActionListener(ActionListener al) {
        if(!hasListener) {
            eventSource.addEventListener(al);
        }
        else {
            throw new RuntimeException("Listener already exists");
        }
    }

    protected void fireBufferFullEvent() {
        drained = false;
        eventSource.post(e);
    }

    /**
     * Fill destination buffer with data which is removed from start of this buffer.
     * @param dest destination buffer
     * @return how much bytes was moved from this buffer into destination buffer.
     */
    public synchronized int drain(byte[] dest) {
        int length = Math.min(dest.length, count);
        if (length > 0) {
            System.arraycopy(buf, 0, dest, 0, length);
            int len = count - length;
            if (len > 0) {
                System.arraycopy(buf, length, buf, 0, len);
            }
            count -= length;
        }
        drained = true;
        return length;
    }

    boolean drained;

    public synchronized void write(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            fireBufferFullEvent();
        }
        super.write(b);
    }

    /**
     * Writes <code>len</code> bytes from given byte array starting at offset off to this buffer.
     * If capacity of buffer is not enough for incoming data,
     * then, at-first, buffer filled with data,
     * then fired "buffer is full" event,
     * thus giving the user possibility to "drain" buffer.
     * If buffer was drained, then rest of data is written to buffer,
     * otherwise buffer capacity is increased before writing.
     * @param b
     * @param off
     * @param len
     */
    public synchronized void write(byte b[], int off, int len) {
        int max = buf.length - count;
        if (max > len) {
            super.write(b, off, len);
        } else {
            super.write(b, off, max);
            fireBufferFullEvent();
            write2(b, off + max, len - max);
        }
    }

    private void write2(byte b[], int off, int len) {
        if (!drained) {
            super.write(b, off, len);
        } else {
            drained = false;
            int max = buf.length - count;
            if (max > len) {
                super.write(b, off, len);
            } else {
                super.write(b, off, max);
                fireBufferFullEvent();
                write2(b, off + max, len - max);
            }
        }
    }

    public void close() throws IOException {
        fireBufferFullEvent();
        super.close();
    }

    /**
     * Retrieve internal buffer.
     * Recommended use - immediately after receiving "buffer full" event.
     * Internal buffer is returned and replaced with new empty buffer.
     */
    public synchronized byte[] drain() {
        byte[] tmp = buf;
        buf = new byte[0];
        count = 0;
        drained = true;
        return tmp;
    }

    public synchronized void writeTo(DataOutput out) throws IOException {
        out.write(buf, 0, count);
    }

    private static class MyEventSource extends EventSource {
        public MyEventSource(Object source) {
            super(source);
        }

        public void post(VEvent e) {
            super.post(e);
        }
    }
}
