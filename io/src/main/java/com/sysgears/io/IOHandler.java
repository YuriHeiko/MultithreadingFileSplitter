package com.sysgears.io;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains an interface contract and common IO logic
 */
public abstract class IOHandler {
    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * writes buffer bytes starting from 0 and ending at {@code length}
     *
     * @param raf      The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to write
     * @param length   The number of significant values in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    public void write(final RandomAccessFile raf, final byte[] buffer, final long position, final int length)
            throws IOHandlerException {
        try {
            raf.seek(position);
            raf.write(buffer, 0, length);
        } catch (IOException e) {
            throw new IOHandlerException("IO error during writing");
        }
    }

    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * reads bytes into the {@code buffer}
     *
     * @param raf      The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to read
     * @return The number of significant elements in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    public int read(final RandomAccessFile raf, byte[] buffer, final long position) throws IOHandlerException {
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