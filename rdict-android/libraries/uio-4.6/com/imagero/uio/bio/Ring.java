package com.imagero.uio.bio;

/**
 * Ring - minimalistic Ring implementation.
 *
 * Date: 29.08.2007
 * @author Andrey Kuznetsov
 */
class Ring {

    Object[] elements;

    int size;
    int index;

    public Ring(int size) {
        this.size = size;
        this.elements = new Object[size];
    }

    /**
     * add Object to ring
     * @param o Object
     * @return Object removed from ring (replaced by new Object) or null
     */
    public Object add(Object o) {
        Object tmp = elements[index];
        elements[index] = o;
        index = (index + 1) % size;
        return tmp;
    }
}
