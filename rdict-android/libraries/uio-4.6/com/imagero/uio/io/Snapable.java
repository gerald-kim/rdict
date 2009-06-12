package com.imagero.uio.io;

/**
 * Date: 25.06.2008
 *
 * @author Andrey Kuznetsov
 */
public interface Snapable {
    /**
     * creates snapshot of internal state of stream
     * @return
     */
    StreamSnapshot makeSnapshot();
}
