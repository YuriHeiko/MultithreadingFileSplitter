package com.sysgears.processor.io;

import com.sysgears.processor.exceptions.IOHandlerException;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class IOHandler {
    public void write(final RandomAccessFile raf, final byte[] buffer, final long position, final int length) {
        try {
            raf.seek(position);
            raf.write(buffer, 0, length);
        } catch (IOException e) {
            throw new IOHandlerException("IO error during writing");
        }
    }

    public int read(final RandomAccessFile raf, byte[] buffer, final long position) {
        int read;
        try {
            raf.seek(position);
            read = raf.read(buffer);
        } catch (IOException e) {
            throw new IOHandlerException("IO error during reading");
        }

        return read;
    }
}