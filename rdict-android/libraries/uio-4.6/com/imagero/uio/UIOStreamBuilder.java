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

package com.imagero.uio;

import com.imagero.uio.bio.BIOFactory;
import com.imagero.uio.bio.BufferedRandomAccessIO;
import com.imagero.uio.bio.ByteArrayRandomAccessIO;
import com.imagero.uio.bio.IOController;
import com.imagero.uio.bio.VariableSizeByteBuffer;
import com.imagero.uio.bio.content.ByteArrayContent;
import com.imagero.uio.bio.content.CharArrayContent;
import com.imagero.uio.bio.content.Content;
import com.imagero.uio.bio.content.DoubleArrayContent;
import com.imagero.uio.bio.content.FloatArrayContent;
import com.imagero.uio.bio.content.IntArrayContent;
import com.imagero.uio.bio.content.LongArrayContent;
import com.imagero.uio.bio.content.RandomAccessFileContent;
import com.imagero.uio.bio.content.ShortArrayContent;
import com.imagero.uio.bio.content.Span;
import com.imagero.uio.bio.content.SpannedRandomAccessInputContent;
import com.imagero.uio.bio.content.SpannedRandomAccessIOContent;
import com.imagero.uio.impl.RandomAccessFileWrapper;
import com.imagero.uio.impl.RandomAccessFileX;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * <pre>
 * UIOStreamBuilder is a builder pattern implementation and replacement for RandomAccessFactory.
 * Usual process looks like
 * File f = ...;
 * RandomAccessIO ro = new UIOStreamBuilder(f).setByteOrder(RandomAccessIO.LITTLE_ENDIAN).setBuffered(true).create();
 * or
 * RandomAccessIO ra = (RandomAccessIO)new UIOStreamBuilder(f).setMode(UIOStreamBuilder.READ_WRITE).create();
 *
 * Defaul values are:
 * mode - UIOStreamBuilder.READ_ONLY
 * byte order - RandomAccessIO.BIG_ENDIAN
 * buffered - false (however some streams are always buffered)
 *
 * </pre>
 * @see #create
 * @see #setBuffered
 * @see #setByteOrder
 * @see #setBufferSize
 * @see #setCacheFile
 * @see #setMaxBufferCount
 * @see #setMode
 * @see #setStart
 * @see #setLength
 *
 * @author Andrey Kuznetsov
 */
public class UIOStreamBuilder {

    public static final String READ_ONLY = "r";
    public static final String READ_WRITE = "rw";

    String mode = READ_ONLY;

    int byteOrder = Endian.BIG_ENDIAN;

    Long start;
    Long length;

    private boolean buffered = true;

    private Integer maxBufferCount;
    private Integer bufferSize;

    Creator creator;

    File cache;

    public static int DEFAULT_CHUNK_SIZE = 256 * 1024;
    public static int DEFAULT_CHUNK_COUNT = 8;

    public boolean isReadOnly() {
        if (READ_WRITE.equals(mode)) {
            return false;
        }
        return true;
    }

    public UIOStreamBuilder(String filename) {
        this(new File(filename));
    }

    public UIOStreamBuilder(File file) {
        this.creator = new FileCreator(file);
    }

    public UIOStreamBuilder(RandomAccessFile rafSource) {
        this.creator = new RAFCreator(rafSource);
    }

    public UIOStreamBuilder(RandomAccessIO ra) {
        this.creator = new RAIOCreator(ra);
    }

    public UIOStreamBuilder(RandomAccessInput ro) {
        this.creator = new RAICreator(ro);
    }

    public UIOStreamBuilder(RandomAccessInput ro, Span [] spans) {
        this.creator = new SpannedCreator(ro, spans);
    }

    public UIOStreamBuilder(byte[] byteSource) {
        this.creator = new ByteCreator(byteSource);
    }

    public UIOStreamBuilder(byte[][] byteSource) {
        if (byteSource.length == 1) {
            this.creator = new ByteCreator(byteSource[0]);
        } else {
            this.creator = new Byte2DCreator(byteSource);
        }
    }

    public UIOStreamBuilder(short[] shortSource) {
        creator = new ShortCreator(shortSource);
    }

    public UIOStreamBuilder(short[][] shortSource) {
        creator = new ShortCreator(shortSource);
    }

    public UIOStreamBuilder(char[] charSource) {
        creator = new CharCreator(charSource);
    }

    public UIOStreamBuilder(char[][] charSource) {
        creator = new CharCreator(charSource);
    }

    public UIOStreamBuilder(int[] intSource) {
        creator = new IntCreator(intSource);
    }

    public UIOStreamBuilder(int[][] intSource) {
        creator = new IntCreator(intSource);
    }

    public UIOStreamBuilder(long[] longSource) {
        creator = new LongCreator(longSource);
    }

    public UIOStreamBuilder(long[][] longSource) {
        creator = new LongCreator(longSource);
    }

    public UIOStreamBuilder(float[] floatSource) {
        creator = new FloatCreator(floatSource);
    }

    public UIOStreamBuilder(float[][] floatSource) {
        creator = new FloatCreator(floatSource);
    }

    public UIOStreamBuilder(double[] doubleSource) {
        creator = new DoubleCreator(doubleSource);
    }

    public UIOStreamBuilder(double[][] doubleSource) {
        creator = new DoubleCreator(doubleSource);
    }

    static private File getTmpDir() {
        String name = System.getProperty("uio.temp.dir");
        if (name != null && name.length() > 0) {
            File f = new File(name);
            if (!f.exists()) {
                f.mkdirs();
            }
            if (f.isDirectory()) {
                return f;
            }
        }
        return null;
    }

    static File createTempFile(String prefix) {
        File dir = getTmpDir();
        if (dir != null) {
            return new File(dir, prefix + Integer.toHexString(dir.hashCode()));
        }
        return null;
    }

    /**
     * always buffered
     * @param url
     */
    public UIOStreamBuilder(URL url) {
        creator = new URLCreator(url);
    }

    /**
     * always buffered
     * @param in
     */
    public UIOStreamBuilder(InputStream in) {
        creator = new ISCreator(in);
    }

    /**
     * Always buffered.
     * Two things are very important:
     * 1. closing RandomAccessOutput created by this method does not close OutputStream
     * 2. To write data to OutputStream RandomAccessOutput must be closed or flushed.
     *
     * @param out OutputStream
     */
    public UIOStreamBuilder(OutputStream out) {
        creator = new OSCreator(out);
    }

    /**
     * set mode (writeable or read only)
     * @param mode READ_ONLY or READ_WRITE
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setMode(String mode) {
        if (READ_ONLY.equals(mode) || READ_WRITE.equals(mode)) {
            this.mode = mode;
            return this;
        } else {
            throw new IllegalArgumentException(mode);
        }
    }

    /**
     * set byte order (big endian or little endian)
     * @param byteOrder LITTLE_ENDIAN or BIG_ENDIAN (default value - BIG_ENDIAN)
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setByteOrder(int byteOrder) {
        switch (byteOrder) {
            case Endian.LITTLE_ENDIAN:
            case Endian.BIG_ENDIAN:
                this.byteOrder = byteOrder;
                return this;
            default:
                throw new IllegalArgumentException("" + byteOrder);
        }
    }

    /**
     * set start offset
     * @param start start offset of stream (default value - 0L)
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setStart(long start) {
        if (start < 0) {
            throw new IllegalArgumentException(" " + start);
        }
        this.start = new Long(start);
        return this;
    }

    /**
     * set stream length
     * @param length stream length
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setLength(long length) {
        if (length < 0) {
            throw new IllegalArgumentException(" " + length);
        }
        this.length = new Long(length);
        if (start == null) {
            start = new Long(0L);
        }
        return this;
    }

    /**
     * set if stream should be buffered or not (rather a hint because some streams are always buffered)
     * @param buffered true or false (default value - false)
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setBuffered(boolean buffered) {
        this.buffered = buffered;
        return this;
    }

    /**
     * set maxBufferCount for MemoryAccessManager - for unbuffered streams this parameter is ignored.
     * @param max
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setMaxBufferCount(int max) {
        this.maxBufferCount = new Integer(max);
        return this;
    }

    /**
     * set size for memory chunks used by MemoryAccessManager - for unbuffered streams this parameter is ignored.
     * @param bufferSize
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setBufferSize(int bufferSize) {
        this.bufferSize = new Integer(bufferSize);
        return this;
    }

    /**
     * Set file which can be used to cache data (only for Streams)
     * @param f File
     * @return UIOStreamBuilder
     */
    public UIOStreamBuilder setCacheFile(File f) {
        this.cache = f;
        return this;
    }

    /**
     * finally create desired stream
     * @return RandomAccessIinput
     * @throws java.io.IOException
     */
    public RandomAccessInput create() throws IOException {
        if (length != null && length.longValue() == 0) {
            Sys.err.println("Warning: stream length is 0");
        }
        if (buffered) {
            return creator.createBuffered();
        } else {
            return creator.create();
        }
    }

    abstract class Creator {
        abstract RandomAccessInput create() throws IOException;

        abstract RandomAccessInput createBuffered() throws IOException;

        protected int getByteOrder() {
            return byteOrder != 0 ? byteOrder : Endian.BIG_ENDIAN;
        }

        protected int getBufferCount() {
            if (maxBufferCount != null) {
                return maxBufferCount.intValue();
            }
            return DEFAULT_CHUNK_COUNT;
        }

        protected int getBufferSize() {
            if (bufferSize != null) {
                return bufferSize.intValue();
            }
            return DEFAULT_CHUNK_SIZE;
        }
    }

    class FileCreator extends Creator {

        File fileSource;

        public FileCreator(File fileSource) {
            this.fileSource = fileSource;
        }

        public RandomAccessInput create() throws IOException {
            final RandomAccessFileX rafx = new RandomAccessFileX(fileSource, mode);
            if (start == null && length == null) {
                return new RandomAccessFileWrapper(rafx, getByteOrder());
            } else if (length == null) {
                return new RandomAccessFileWrapper(rafx, start.longValue(), getByteOrder());
            } else {
                return new RandomAccessFileWrapper(rafx, start.longValue(), length.longValue(), getByteOrder());
            }
        }

        RandomAccessInput createBuffered() throws IOException {
            Content bc = new RandomAccessFileContent(fileSource, mode);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, bc);
            BufferedRandomAccessIO bio = new BufferedRandomAccessIO(controller);
            bio.setByteOrder(byteOrder);
            return bio;
        }
    }

    class RAFCreator extends Creator {
        RandomAccessFile rafSource;

        public RAFCreator(RandomAccessFile rafSource) {
            this.rafSource = rafSource;
        }

        public RandomAccessInput create() throws IOException {
            if (start == null && length == null) {
                return new RandomAccessFileWrapper(rafSource, getByteOrder());
            } else if (length == null) {
                return new RandomAccessFileWrapper(rafSource, start.longValue(), getByteOrder());
            } else {
                return new RandomAccessFileWrapper(rafSource, start.longValue(), length.longValue(), getByteOrder());
            }
        }

        RandomAccessInput createBuffered() throws IOException {
            Content bc = new RandomAccessFileContent(rafSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, bc);
            return new BufferedRandomAccessIO(controller);
        }
    }

    class RAICreator extends Creator {
        RandomAccessInput roSource;

        public RAICreator(RandomAccessInput roSource) {
            this.roSource = roSource;
        }

        protected int getByteOrder() {
            return byteOrder != 0 ? byteOrder : roSource.getByteOrder();
        }

        public RandomAccessInput create() throws IOException {
            return roSource.createInputChild(start != null ? start.longValue() : 0L, 0, byteOrder, false);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class RAIOCreator extends Creator {
        RandomAccessIO raSource;

        public RAIOCreator(RandomAccessIO raSource) {
            this.raSource = raSource;
        }

        protected int getByteOrder() {
            return byteOrder != 0 ? byteOrder : raSource.getByteOrder();
        }

        public RandomAccessInput create() throws IOException {
            return raSource.createIOChild(start != null ? start.longValue() : 0L, 0, byteOrder, true);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class Byte2DCreator extends Creator {

        byte[][] byteSource;

        public Byte2DCreator(byte[][] byteSource) {
            this.byteSource = byteSource;
        }

        RandomAccessInput create() throws IOException {
            return createBuffered();
        }

        RandomAccessInput createBuffered() throws IOException {
            Content content = new ByteArrayContent(byteSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }
    }

    class ByteCreator extends Creator {

        byte[] byteSource;

        public ByteCreator(byte[] byteSource) {
            this.byteSource = byteSource;
        }

        RandomAccessInput create() throws IOException {
            if (start != null) {
                VariableSizeByteBuffer vsb;
                vsb = new VariableSizeByteBuffer(byteSource);
                return new ByteArrayRandomAccessIO(start.intValue(), length != null? length.intValue(): 0, vsb);
            }
            return new ByteArrayRandomAccessIO(byteSource);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class ShortCreator extends Creator {
        short[][] shortSource;

        public ShortCreator(short[] shortSource) {
            this(new short[][]{shortSource});
        }

        public ShortCreator(short[][] shortSource) {
            this.shortSource = shortSource;
        }

        RandomAccessInput create() throws IOException {
            Content content = new ShortArrayContent(shortSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class CharCreator extends Creator {
        char[][] charSource;

        public CharCreator(char[] charSource) {
            this(new char[][]{charSource});
        }

        public CharCreator(char[][] charSource) {
            this.charSource = charSource;
        }

        RandomAccessInput create() throws IOException {
            Content content = new CharArrayContent(charSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class IntCreator extends Creator {
        int[][] intSource;

        public IntCreator(int[] intSource) {
            this(new int[][]{intSource});
        }

        public IntCreator(int[][] intSource) {
            this.intSource = intSource;
        }

        RandomAccessInput create() throws IOException {
            Content content = new IntArrayContent(intSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class SpannedCreator extends Creator {
        Span [] spans;
        RandomAccessInput raiSource;

        public SpannedCreator(RandomAccessInput raiSource, Span[] spans) {
            this.raiSource = raiSource;
            this.spans = spans;
        }

        RandomAccessInput create() throws IOException {
            Content content;
            if(mode == READ_ONLY) {
                RandomAccessInput inputChild = raiSource.createInputChild(0, 0, getByteOrder(), false);
                content = new SpannedRandomAccessInputContent(inputChild, spans);
            }
            else {
                if(raiSource instanceof RandomAccessIO) {
                    RandomAccessIO inputChild = ((RandomAccessIO)raiSource).createIOChild(0, 0, getByteOrder(), false);
                    content = new SpannedRandomAccessIOContent(inputChild, spans);
                }
                else {
                    RandomAccessInput inputChild = raiSource.createInputChild(0, 0, getByteOrder(), false);
                    content = new SpannedRandomAccessInputContent(inputChild, spans);
                }
            }
            IOController controller = new IOController(Math.min(DEFAULT_CHUNK_SIZE, Math.max(1024, getLength())) , content);
            return new BufferedRandomAccessIO(controller, 0L);
        }

        private int getLength() {
            int length = 0;
            for (int i = 0; i < spans.length; i++) {
                Span span = spans[i];
                length += span.length;
            }
            return length;
        }

        protected int getByteOrder() {
            return byteOrder != 0 ? byteOrder : raiSource.getByteOrder();
        }


        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class LongCreator extends Creator {
        long[][] longSource;

        public LongCreator(long[] longSource) {
            this(new long[][]{longSource});
        }

        public LongCreator(long[][] longSource) {
            this.longSource = longSource;
        }

        RandomAccessInput create() throws IOException {
            Content content = new LongArrayContent(longSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class FloatCreator extends Creator {
        float[][] floatSource;

        public FloatCreator(float[] floatSource) {
            this(new float[][]{floatSource});
        }

        public FloatCreator(float[][] floatSource) {
            this.floatSource = floatSource;
        }

        RandomAccessInput create() throws IOException {
            Content content = new FloatArrayContent(floatSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class DoubleCreator extends Creator {
        double[][] doubleSource;

        public DoubleCreator(double[] doubleSource) {
            this(new double[][]{doubleSource});
        }

        public DoubleCreator(double[][] doubleSource) {
            this.doubleSource = doubleSource;
        }

        RandomAccessInput create() throws IOException {
            Content content = new DoubleArrayContent(doubleSource);
            IOController controller = new IOController(DEFAULT_CHUNK_SIZE, content);
            return new BufferedRandomAccessIO(controller, start != null ? start.longValue() : 0L);
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class URLCreator extends Creator {
        URL url;
        FileCreator fileCreator;

        public URLCreator(URL url) {
            this.url = url;
            final String protocol = url.getProtocol();
            if ("file".equalsIgnoreCase(protocol)) {
                File f = new File(url.getFile());
                fileCreator = new FileCreator(f);
            }
        }

        RandomAccessInput create() throws IOException {
            if (fileCreator == null) {
                return create0();
            } else {
                return fileCreator.create();
            }
        }

        RandomAccessInput createBuffered() throws IOException {
            if (fileCreator == null) {
                return create0();
            } else {
                return fileCreator.createBuffered();
            }
        }

        private RandomAccessInput create0() {
            if (cache == null) {
                cache = createTempFile("urc");
            }
            IOController controller = BIOFactory.createIOController(url, cache, getBufferSize());
            BufferedRandomAccessIO rio = new BufferedRandomAccessIO(controller);
            rio.setByteOrder(byteOrder);
            return rio;
        }
    }

    class ISCreator extends Creator {
        InputStream inputStreamSource;

        public ISCreator(InputStream inputStreamSource) {
            this.inputStreamSource = inputStreamSource;
        }

        RandomAccessInput create() throws IOException {
            if (inputStreamSource instanceof ByteArrayInputStream) {
                return new BaisWrapper((ByteArrayInputStream) inputStreamSource);
            } else {
                if (cache == null) {
                    cache = createTempFile("isc");
                }
                IOController controller = BIOFactory.createIOController(inputStreamSource, cache, getBufferSize());
                BufferedRandomAccessIO bio = new BufferedRandomAccessIO(controller);
                bio.setByteOrder(byteOrder);
                return bio;
            }
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }

    class OSCreator extends Creator {
        OutputStream outputStreamSource;

        public OSCreator(OutputStream outputStreamSource) {
            this.outputStreamSource = outputStreamSource;
            setMode(READ_WRITE);
        }

        RandomAccessInput create() throws IOException {
            final BufferedRandomAccessIO bio = BIOFactory.create(outputStreamSource);
            bio.setByteOrder(byteOrder);
            return bio;
        }

        RandomAccessInput createBuffered() throws IOException {
            return create();
        }
    }
}
