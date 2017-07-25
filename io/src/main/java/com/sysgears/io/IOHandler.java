package com.sysgears.io;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains an interface contract and common IO logic
 */
public abstract class IOHandler {
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(IOHandler.class);

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
            log.debug("Trying to set a pointer to position: " + position);
            raf.seek(position);
            log.debug("Trying to write data. length: " + length);
            raf.write(buffer, 0, length);
        } catch (IOException e) {
            log.error("IO error during writing");
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
            log.debug("Trying to set a pointer to position: " + position);
            raf.seek(position);
            log.debug("Trying to read data");
            read = raf.read(buffer);
        } catch (IOException e) {
            log.warn("IO error during reading");
            throw new IOHandlerException("IO error during reading");
        }

        return read;
    }
}