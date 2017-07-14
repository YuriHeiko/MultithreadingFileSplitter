package com.sysgears.processor.io;

import java.io.RandomAccessFile;

public class JoinerIO extends IOHandler {
    @Override
    public void write(final RandomAccessFile raf, final byte[] buffer, final long position, final int length) {
        synchronized (raf) {
            super.write(raf, buffer, position, length);
        }
    }

    @Override
    public int read(final RandomAccessFile raf, byte[] buffer, final long position) {
        return super.read(raf, buffer, position);
    }
}
