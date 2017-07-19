package com.sysgears.service.chunks;

import com.sysgears.io.IOHandler;
import com.sysgears.service.ServiceException;
import com.sysgears.statistic.StatisticHolder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents a part of a file to do some action with it.
 */
abstract class Chunk implements Runnable {
    /**
     * The {@code IOHandler}
     */
    private final IOHandler IO;
    /**
     * The {@code RandomAccessFile} object to read
     */
    private final RandomAccessFile RAF_READ;
    /**
     * The {@code RandomAccessFile} object to write
     */
    private final RandomAccessFile RAF_WRITE;
    /**
     * The {@code StatisticHolder} object
     */
    private final StatisticHolder HOLDER;
    /**
     * The default size of the buffer to read/write operations
     */
    private int bufferSize;
    /**
     * The file offset pointer
     */
    final long POINTER;
    /**
     * The size of this part
     */
    final long CHUNK_SIZE;

    /**
     * Constructs an object
     *
     * @param io              The {@code IOHandler}
     * @param holder          The {@code StatisticHolder}
     * @param fileToWriteName The file-to-write file name
     * @param fileToReadName  The file-to-read file name
     * @param pointer         The file offset pointer
     * @param chunkSize       The size of this part
     */
    Chunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
          final String fileToReadName, final long pointer, final long chunkSize, final int bufferSize) {

        this.IO = io;
        this.HOLDER = holder;
        this.POINTER = pointer;
        this.CHUNK_SIZE = chunkSize;
        this.bufferSize = bufferSize;

        try {
            RAF_READ = new RandomAccessFile(fileToReadName, "r");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileToReadName + " doesn't exist.");
        }

        try {
            RAF_WRITE = new RandomAccessFile(fileToWriteName, "rw");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileToWriteName + " doesn't exist.");
        }
    }

    /**
     * Does some action with the part of the file
     */
    @Override
    public void run() {
        int buffSize = CHUNK_SIZE < bufferSize ? (int) CHUNK_SIZE : bufferSize;
        long total = CHUNK_SIZE;
        HOLDER.resetProgress(Thread.currentThread(), CHUNK_SIZE);

        while (total > 0) {
            int done = callAction(total, buffSize);

            total -= done;
            // tackles the last part issue
            if (total < buffSize && total > 0) {
                buffSize = (int) total;
            }
        }

        try {
            RAF_READ.close();
            RAF_WRITE.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls an action
     *
     * @param total    The number of bytes to process
     * @param buffSize The size of the buffer
     * @return The number of processed bytes
     */
    abstract int callAction(final long total, final int buffSize);

    /**
     * Performs an action
     *
     * @param readPointer  The file offset to read
     * @param writePointer The file offset to write
     * @param buffSize     The size of the buffer
     * @return The number of processed bytes
     */
    int doAction(final long readPointer, final long writePointer, final int buffSize) {
        byte[] buffer = new byte[buffSize];
        int read = IO.read(RAF_READ, buffer, readPointer);
        IO.write(RAF_WRITE, buffer, writePointer, read);

        HOLDER.setProgress(Thread.currentThread(), read);

        return read;
    }
}