package com.imagero.uio.test;

/**
 * Date: 01.03.2008
 *
 * @author Andrey Kuznetsov
 */
public class Assertions {
    protected static void assertEquals(String a, String b) {
        if(!a.equals(b)) {
            throw new RuntimeException(a + " " + b);
        }
    }

    protected static void assertEquals(boolean a, boolean b) {
        if(a != b) {
            throw new RuntimeException(a + " " + b);
        }
    }

    protected static void assertEquals(int a, int b) {
        if(a != b) {
            throw new RuntimeException(a + " " + b);
        }
    }

    protected static void assertEquals(long a, long b) {
        if(a != b) {
            throw new RuntimeException(a + " " + b);
        }
    }

    protected static void assertEquals(float a, float b) {
        if(a != b) {
            throw new RuntimeException(a + " " + b);
        }
    }

    protected static void assertEquals(double a, double b) {
        if(a != b) {
            throw new RuntimeException(a + " " + b);
        }
    }

    protected static void assertEquals(byte [] a, byte [] b) {
        for (int i = 0; i < a.length; i++) {
            if(a[i] != b[i]) {
                throw new RuntimeException(a[i] + " " + b[i] + " at " + i);
            }
        }
    }

    protected static void assertEquals(int offset, byte [] a, byte [] b) {
        for (int i = offset; i < a.length; i++) {
            if(a[i] != b[i]) {
                throw new RuntimeException(a[i] + " " + b[i] + " at " + i);
            }
        }
    }

    protected static void assertEquals(int offset, int length, byte [] a, byte [] b) {
        for (int i = 0; i < length; i++) {
            if(a[offset + i] != b[offset + i]) {
                throw new RuntimeException(a[i] + " " + b[i] + " at " + i);
            }
        }
    }
}
