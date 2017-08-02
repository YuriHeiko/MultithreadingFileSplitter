package com.sysgears.io;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Reads and writes from/into specified file position
 */
public class IOHandler implements IIO {
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(IOHandler.class);

    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * writes buffer bytes starting from 0 and ending at {@code length}
     *
     * @param writer   The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to write
     * @param length   The number of significant values in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    @Override
    public void write(final RandomAccessFile writer, final byte[] buffer, final long position, final int length) throws IOHandlerException {
        try {
            log.debug("Trying to set the pointer to the position: " + position);
            writer.seek(position);
            log.debug("Trying to write " + length + " bytes");
            writer.write(buffer, 0, length);
            log.debug("All the bytes were written");
            log.debug("Trying to close the file to write");
        } catch (IOException e) {
            log.error("The IO error during writing. Message: " + e.getMessage());
            throw new IOHandlerException("The IO error during writing. Message: " + e.getMessage());
        }
    }

    /**
     * Sets a {@link RandomAccessFile} pointer into {@code position} than
     * reads bytes into the {@code buffer}
     *
     * @param reader   The {@code RandomAccessFile} object
     * @param buffer   The byte array
     * @param position The position of file from where start to read
     * @return The number of significant elements in the {@code buffer}
     * @throws IOHandlerException When {@code IOException} is arisen
     */
    @Override
    public int read(final RandomAccessFile reader, byte[] buffer, final long position) throws IOHandlerException {
        int read;
        try {
            log.debug("Trying to set the pointer to the position: " + position);
            reader.seek(position);
            log.debug("Trying to read bytes");
            read = reader.read(buffer);
            log.debug(read + " bytes were read");
            log.debug("Trying to close the file to read");
        } catch (IOException e) {
            log.error("The IO error during reading. Message: " + e.getMessage());
            throw new IOHandlerException("The IO error during reading. Message: " + e.getMessage());
        }

        return read;
    }
}