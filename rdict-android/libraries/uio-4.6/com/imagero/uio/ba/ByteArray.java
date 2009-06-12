package com.imagero.uio.ba;

import com.imagero.uio.io.IOutils;
import com.imagero.uio.io.ByteArrayOutputStream2;

import java.io.*;

/**
 * This class encapsulates buffer(byte array) and buffer position (byte and bit offset)
 *
 * @author Andrey Kuznetsov
 */
public class ByteArray implements Cloneable {

    public static final int COPY = 1;
    public static final int REFERENCE = 2;
    public static final int FRESH = 3;

    public static int DEFAULT = COPY;

    public byte[] buffer;

    public static final int[] N_MASK = {0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    public static final int[] K_MASK = {0x00, 0x01, 0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF};
    public static final int[] I_MASK = {0x00, 0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF};

    public ByteArray(byte[] buffer) {
        this.buffer = buffer;
    }

    public ByteArray(ByteArray barray, int options) {
        switch (options) {
            case COPY:
                buffer = new byte[barray.buffer.length];
                System.arraycopy(barray.buffer, 0, buffer, 0, buffer.length);
                break;
            case REFERENCE:
                buffer = barray.buffer;
                break;
            case FRESH:
                buffer = new byte[barray.buffer.length];
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return new ByteArray(this, DEFAULT);
    }

    public byte [] copyBuffer(byte [] dest) {
        if(dest == null || dest.length < buffer.length) {
            dest = new byte[buffer.length];
        }
        System.arraycopy(buffer, 0, dest, 0, buffer.length);
        return dest;
    }

    public void copyBuffer(ByteArray dest) {
        System.arraycopy(buffer, 0, dest.buffer, 0, buffer.length);
    }

    public Position createPosition() {
        return new Position(buffer.length);
    }

    public void saveBuffer(OutputStream out) throws IOException {
        out.write(buffer);
    }

    public void saveBuffer(DataOutput out) throws IOException {
        out.write(buffer);
    }

    public void readBuffer(InputStream in) throws IOException {
        IOutils.readFully(in, buffer);
    }

    public void readBuffer(DataInput in) throws IOException {
        in.readFully(buffer);
    }

    public OutputStream getOutputStream() {
        return new ByteArrayOutputStream2(buffer);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public BitBufferIn createBitwiseInput() {
        return BitBufferIn.create(buffer);
    }

    public BitBufferOut createBitwiseOutput() {
        return new BitBufferOut.BitBufferByte(buffer);
    }
}
