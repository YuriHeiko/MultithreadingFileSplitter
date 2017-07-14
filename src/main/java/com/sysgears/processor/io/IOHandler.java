package com.sysgears.processor.io;

import java.io.RandomAccessFile;

public interface IOHandler {
    void write(RandomAccessFile raf, byte[] buffer, long position);

    int read(RandomAccessFile raf, byte[] buffer, long position);
}
