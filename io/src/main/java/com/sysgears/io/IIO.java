package com.sysgears.io;

import java.io.RandomAccessFile;

/**
 * Contains the IO handling interface contract
 */
public interface IIO {
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
    void write(RandomAccessFile writer, byte[] buffer, long position, int length) throws IOHandlerException;

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
    int read(RandomAccessFile reader, byte[] buffer, long position) throws IOHandlerException;
}
