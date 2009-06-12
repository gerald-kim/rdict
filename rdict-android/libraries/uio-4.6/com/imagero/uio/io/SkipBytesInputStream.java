package com.imagero.uio.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 30.07.2007
 *
 * @author Andrey Kuznetsov
 */
public class SkipBytesInputStream extends FilterInputStream {

    static long[] msk = createMask();

    private static long[] createMask() {
        long[] m = new long[64];
        m[0] = 1;
        for (int i = 1; i < m.length; i++) {
            m[i] = m[i - 1] << 1;
        }
        return m;
    }

    public SkipBytesInputStream(InputStream in, long mask, int mod) {
        super(in);
        this.mask = mask;
        this.mod = mod;
    }

    int fp;
    long mask;
    int mod;

    public int read() throws IOException {
        if (mask == 0) {
            return super.read();
        } else {
            int a = in.read();
            long k = msk[(fp++ % mod)] & mask;
            if (k != 0) {
                return a;
            } else {
                return read();
            }
        }
    }

    public int read(byte b[], int off, int len) throws IOException {
        int k = in.read(b, off, len);
        return read0(b, off, k);
    }

    private int read0(byte[] b, int off, int len) {
        if (mask == 0) {
            return len;
        }
        int len0 = 0;
        int p = off;
        for (int i = 0; i < len; i++) {
            if ((msk[(fp++ % mod)] & mask) != 0) {
                b[p++] = b[i];
                len0++;
            }
        }
        return len0;
    }
}
