package com.sysgears.processor.io;

import java.io.RandomAccessFile;

public interface IOHandler {
    void write(RandomAccessFile raf, int buffer, long position, int chunkNumber);

    int read(RandomAccessFile raf, long position, int chunkNumber);
}
