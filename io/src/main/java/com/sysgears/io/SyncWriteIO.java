package com.sysgears.io;

import java.io.RandomAccessFile;

/**
 * Synchronously writes into and asynchronously reads from a
 * {@code RandomAccessFile} stream
 */
public class SyncWriteIO extends IOHandler {
    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * synchronously writes buffer bytes starting from 0 and ending at
     * {@code length}
     *
     * @param raf      The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to write
     * @param length   The number of significant values in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    @Override
    public synchronized void write(final RandomAccessFile raf, final byte[] buffer, final long position, final int length) {
        super.write(raf, buffer, position, length);
    }

    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * synchronously reads bytes into the {@code buffer}
     *
     * @param raf      The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to read
     * @return The number of significant elements in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    @Override
    public int read(final RandomAccessFile raf, byte[] buffer, final long position) {
        return super.read(raf, buffer, position);
    }
}
