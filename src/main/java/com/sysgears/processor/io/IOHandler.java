package com.sysgears.processor.io;

public interface IOHandler {
    int write(byte[] buffer, long position);

    byte[] read(long position);
}
