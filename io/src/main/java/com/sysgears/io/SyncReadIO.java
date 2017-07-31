package com.sysgears.io;

import org.apache.log4j.Logger;

import java.io.RandomAccessFile;

/**
 * Asynchronously writes into and synchronously reads from a
 * {@code RandomAccessFile} stream
 */
public class SyncReadIO extends IOHandler {
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(SyncReadIO.class);

    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * asynchronously writes buffer bytes starting from 0 and ending at
     * {@code length}
     *
     * @param raf      The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to write
     * @param length   The number of significant values in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    @Override
    public void write(final RandomAccessFile raf,
                      final byte[] buffer,
                      final long position,
                      final int length) throws IOHandlerException {
        log.debug("Trying to write");
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
    public synchronized int read(final RandomAccessFile raf,
                                 byte[] buffer,
                                 final long position) throws IOHandlerException {
        log.debug("Trying to synchronously read");
        return super.read(raf, buffer, position);
    }
}