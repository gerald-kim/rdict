package com.imagero.uio.io;

import java.io.FilterOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Date: 02.08.2007
 *
 * @author Andrey Kuznetsov
 */
public class PackBitsOutputStream extends FilterOutputStream {

    private RunBuffer runs;
    int width;
    int cnt;

    public PackBitsOutputStream(OutputStream out, int width) {
        super(out);
        runs = new RunBuffer(out);
        this.width = width;
    }

    public void write(int b) throws IOException {
        put(b);
    }

    private void put(int b) throws IOException {
        runs.put(b);
        if (cnt++ == width) {
            flush();
            cnt = 0;
        }
    }

    public void write(byte b[]) throws IOException {
        for (int i = 0; i < b.length; i++) {
            put(b[i] & 0xFF);
        }
    }

    public void write(byte b[], int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            put(b[off + i] & 0xFF);
        }
    }

    public void flush() throws IOException {
        runs.flush();
    }

    public void close() throws IOException {
        runs.flush();
    }

    static class RunOutputStream extends FilterOutputStream {
        int maxRun;
        int rep = -2;

        int count;

        public RunOutputStream(OutputStream out, int maxRun) {
            super(out);
            this.maxRun = maxRun;
        }

        public void write(int b) throws IOException {
            if (b != rep) {
                throw new IOException("Rep");
            }
            if (count++ == maxRun) {
                out.write(count - 1);
                out.write(rep);
                count = 0;
            }
        }

        public void set(int rep) {
            this.rep = rep;
            count = 2;
        }

        public void flush() throws IOException {
            if (count != 0) {
                out.write(count - 1);
                out.write(rep);
                count = 0;
            }
        }
    }

    static class RunBuffer {
        int rep;
        int count;

        OutputStream out;
        RunOutputStream ros;
        private LiteralBuffer lbuf;

        public RunBuffer(OutputStream out) {
            this.out = out;
            ros = new RunOutputStream(out, 128);
            this.lbuf = new LiteralBuffer(out);
        }

        public void put(int k) throws IOException {
            if (count == 0) {
                rep = k;
                count = 1;
            } else {
                if (rep == k) {
                    if (count++ == 2) {
                        ros.set(rep);
                        lbuf.flush();
                    } else if (count > 2) {
                        ros.write(rep);
                    }
                } else {
                    if (count > 2) {
                        ros.flush();
                    } else {
                        for (int i = 0; i < count; i++) {
                            lbuf.put(rep);
                        }
                    }
                    rep = k;
                    count = 1;
                }
            }
        }

        void flush() throws IOException {
            if (count > 0) {
                ros.flush();
                for (int i = 0; i < count; i++) {
                    lbuf.put(rep);
                }
                count = 0;
            }
            lbuf.flush();
        }

        void write() throws IOException {
            lbuf.flush();
            out.write(-(count - 1));
            out.write(rep);
            count = 0;
        }
    }

    static class LiteralBuffer {
        byte[] buffer = new byte[129];
        int count;

        OutputStream out;

        public LiteralBuffer(OutputStream out) {
            this.out = out;
        }

        public void put(int k) throws IOException {
            buffer[count++] = (byte) k;
            if (count == 128) {
                write();
            }
        }

        void write() throws IOException {
            out.write(count - 1);
            out.write(buffer, 0, count);
            count = 0;
        }

        void flush() throws IOException {
            if (count > 0) {
                write();
            }
        }
    }

    static class PeekInputStream extends ByteArrayInputStream {
        int offset;

        public PeekInputStream(byte buf[]) {
            super(buf);
        }

        public PeekInputStream(byte buf[], int offset, int length) {
            super(buf, offset, length);
            this.offset = offset;
        }

        public int peek(int offset) {
            return buf[pos + offset];
        }

        public int getPosition() {
            return pos - offset;
        }

        public void inc() {
            pos++;
        }
    }

    public static class PackBitsApache extends FilterOutputStream {

        int bytesPerRow;
        int inMax;
        int inMaxMinus1;

        PeekInputStream input;
        byte[] tmp = new byte[128];

        int wpos = 0;
        byte[] row;

        public PackBitsApache(OutputStream out, int bytesPerRow) {
            super(out);
            this.bytesPerRow = bytesPerRow;
            inMax = bytesPerRow - 1;
            inMaxMinus1 = inMax - 1;
            this.row = new byte[bytesPerRow];
        }

        public void write(int b) throws IOException {
            row[wpos++] = (byte) b;
            if (wpos == bytesPerRow) {
                wpos = 0;
                input = new PeekInputStream(row);
                packBits();
            }
        }

        public void write(byte b[]) throws IOException {
            write(b, 0, b.length);
        }

        public void write(byte b[], int off, int len) throws IOException {
            for (int i = 0; i < len; i++) {
                write(b[off + i] & 0xFF);
            }
        }

        public void flush() throws IOException {
            if (wpos > 0) {
                input = new PeekInputStream(row, 0, wpos);
                packBits();
            }
        }

        private void packBits() throws IOException {
            while (input.getPosition() <= inMax) {
                doRunLoop();
                doLiterLoop();
            }
        }

        private void doLiterLoop() throws IOException {
            int run = 0;
            while (run < 128 &&
                    ((input.getPosition() < inMax) && (input.peek(0) != input.peek(1))
                    || ((input.getPosition() < inMaxMinus1) && (input.peek(0) != input.peek(2))))) {
                tmp[run++] = (byte) input.read();
            }

            if (input.getPosition() == inMax && (run > 0 && run < 128)) {
                tmp[run++] = (byte) input.read();
            }

            if (run > 0) {
                out.write(run - 1);
                out.write(tmp, 0, run);
            } else if (input.getPosition() == inMax) {
                out.write(0);
                out.write(input.read() & 0xFF);
            }
        }

        private void doRunLoop() throws IOException {
            int run = 1;
            byte replicate = (byte) input.peek(0);
            while (run < 127 && input.getPosition() < inMax && input.peek(0) == input.peek(1)) {
                run++;
                input.inc();
            }
            if (run > 1) {
                input.inc();
                out.write(-(run - 1));
                out.write(replicate);
            }
        }
    }

    static class PBApache {
        static int compressPackBits(byte[] data, int numRows, int bytesPerRow, byte[] compData) {
            int inOffset = 0;
            int outOffset = 0;
            byte[] tmp = new byte[128];
            for (int i = 0; i < numRows; i++) {
                outOffset = packBits(data, inOffset, bytesPerRow, compData, outOffset, tmp);
                inOffset += bytesPerRow;
            }
            return outOffset;
        }

        private static int packBits(byte[] input, int inOffset, int inCount, byte[] output, int outOffset, byte[] tmp) {
            int inMax = inOffset + inCount - 1;
            int inMaxMinus1 = inMax - 1;
            while (inOffset <= inMax) {
                int run = 1;
                byte replicate = input[inOffset];
                while (run < 127 && inOffset < inMax && input[inOffset] == input[inOffset + 1]) {
                    run++;
                    inOffset++;
                }
                if (run > 1) {
                    inOffset++;
                    output[outOffset++] = (byte) (-(run - 1));
                    output[outOffset++] = replicate;
                }
                run = 0;
                while (run < 128 && ((inOffset < inMax && input[inOffset] != input[inOffset + 1]) || (inOffset < inMaxMinus1 && input[inOffset] != input[inOffset + 2]))) {
                    tmp[run++] = input[inOffset++];
                }
                if (inOffset == inMax && (run > 0 && run < 128)) {
                    tmp[run++] = input[inOffset++];
                }
                if (run > 0) {
                    output[outOffset++] = (byte) (run - 1);
                    for (int i = 0; i < run; i++) {
                        output[outOffset++] = tmp[i];
                    }
                } else if (inOffset == inMax) {
                    output[outOffset++] = (byte) 0;
                    output[outOffset++] = input[inOffset++];
                }
            }
            return outOffset;
        }
    }

    static interface Writer {
        boolean next(byte b) throws IOException;

        byte get();

        void flush() throws IOException;

        Writer nextWriter();
    }

    static class Control extends FilterOutputStream {
        Run run;
        Liter liter;

        Writer current;
        Writer last;

        int count;
        int max;

        public Control(OutputStream out, int max) {
            super(out);
            this.max = max;
            run = new Run(out);
            liter = new Liter(out);
            run.nextWriter = liter;
            liter.nextWriter = run;
            current = run;
        }

        public void write(int b) throws IOException {
            count++;
            if (!current.next((byte) b)) {
                last = current;
                current = current.nextWriter();
                byte n;
                while ((n = last.get()) != -1) {
                    current.next(n);
                }
            }
            if (count == max) {
                count = 0;
                current.flush();
            }
        }

        public void write(byte b[], int off, int len) throws IOException {
            for (int i = 0; i < b.length; i++) {
                write(b[i] & 0xFF);
            }
        }
    }

    static class Run implements Writer {
        int run;
        int count;
        int w;

        int rep;

        byte last;

        Writer nextWriter;

        OutputStream out;

        public Run(OutputStream out) {
            this.out = out;
        }

        public Writer nextWriter() {
            return nextWriter;
        }

        void init(byte rep, int count, int run) {
            this.rep = rep;
            this.run = run;
            this.count = count;
        }

        public byte get() {
            byte tmp = last;
            last = -1;
            return tmp;
        }

        public boolean next(byte b) throws IOException {
            last = b;
            if (run == 0) {
                rep = b & 0xFF;
                run++;
                return true;
            }
            if (b == rep) {
                run++;
                count++;
                if (run == 128) {
                    write();
                }
                return true;
            } else {
                if (run > 1) {
                    write();
                }
                return false;
            }
        }

        private void write() throws IOException {
            out.write(-(run - 1));
            out.write(rep);
            run = 0;
        }

        public void flush() throws IOException {
            if (run > 0) {
                write();
            }
        }
    }

    static class Liter implements Writer {
        int b0 = -1;
        int b1 = -1;
        int b2 = -1;
        int run;
        int count;
        int max;

        int pos;
        byte[] buffer = new byte[128];

        Writer nextWriter;

        OutputStream out;

        public Liter(OutputStream out) {
            this.out = out;
        }

        public Writer nextWriter() {
            return nextWriter;
        }

        public boolean next(byte a) throws IOException {
            b2 = a & 0xFF;
            if (run > 0) {
                if (b0 == b1 && b0 == b2) {
                    write();
                    return false;
                }
            }
            shift();
            if (run == 128) {
                write();
            }
            return true;
        }

        void shift() {
            if (b0 != -1) {
                add(b0);
            }
            b0 = b1;
            b1 = b2;
            b2 = -1;
        }

        public byte get() {
            byte b = (byte) b0;
            b0 = b1;
            b1 = -1;
            return b;
        }

        private void add(int b) {
            buffer[run++] = (byte) b;
            count++;
        }

        public void flush() throws IOException {
            if (run > 0 && run < 128) {
                shift();
                write();
            } else if (run == 0) {
                shift();
                out.write(0);
                out.write(buffer[0] & 0xFF);
            }
        }

        private void write() throws IOException {
            if (run > 0) {
                out.write(run - 1);
                out.write(buffer, 0, run);
            }
            run = 0;
        }
    }
}
