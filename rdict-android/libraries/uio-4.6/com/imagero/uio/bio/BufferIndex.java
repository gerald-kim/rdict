package com.imagero.uio.bio;

/**
 * Index of Object in 2D array
 * Date: 14.12.2007
 *
 * @author Andrey Kuznetsov
 */
class BufferIndex {
    /**
     * index of array in 2D array
     */
    int arrayIndex;
    /**
     * index of object in 1D array
     */
    int index;

    /**
     * @param arrayIndex index of array
     * @param index index of object
     */
    public BufferIndex(int arrayIndex, int index) {
        this.arrayIndex = arrayIndex;
        this.index = index;
    }

    public boolean equals(Object obj) {
        if(obj != null && obj instanceof BufferIndex) {
            BufferIndex bi = (BufferIndex) obj;
            return bi.arrayIndex == arrayIndex && bi.index == index;
        }
        return false;
    }
}
