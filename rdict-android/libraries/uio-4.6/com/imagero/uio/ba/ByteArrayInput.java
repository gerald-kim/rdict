package com.imagero.uio.ba;

import com.imagero.uio.ba.ByteArray;


/**
 * ByteArrayIO - this class gives the possibility to read from and write to given ByteArray.
 * It supports bit offsets and bitwise writing.
 * It does not extends InputStream or OutputStream, but has similar methods.
 *
 * Date: 09.04.2008
 *
 * @author Andrey Kuznetsov
 */
public class ByteArrayInput {

    public static int read(ByteArray ba, Position position) {
        return ba.buffer[position.position++] & 0xFF;
    }

    public static int read(ByteArray ba, Position position, byte b[]) {
        return read(ba, position, b, 0, b.length);
    }

    public static int read(ByteArray ba, Position position, byte b[], int off, int len) {
        if (len <= 0) {
            return 0;
        }
        int c = read(ba, position);
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        for (; i < len; ++i) {
            c = read(ba, position);
            if (c == -1) {
                break;
            }
            b[off + i] = (byte) c;
        }
        return i;
    }
}
