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
public class ByteArrayOutput {

    /**
     * Writes byte to this ByteArray buffer.
     * @param b int which should be written
     */
    public static void write(ByteArray ba, Position position, int b) {
        ba.buffer[position.position++] = (byte) b;
    }
}
