package com.sysgears.processor.service.chunks;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.service.ServiceException;
import com.sysgears.processor.statistic.StatisticHolder;
import com.sysgears.processor.ui.FileProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 */
abstract class Chunk implements Runnable {
    private final IOHandler IO;
    private final RandomAccessFile RAF_READ;
    private final RandomAccessFile RAF_WRITE;
    private final StatisticHolder HOLDER;
    final long POINTER;
    final long CHUNK_SIZE;

    Chunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
          final String fileToReadName, final long pointer, final long chunkSize) {

        this.IO = io;
        this.HOLDER = holder;
        this.POINTER = pointer;
        this.CHUNK_SIZE = chunkSize;

        try {
            RAF_READ = new RandomAccessFile(fileToReadName, "r");
            RAF_WRITE = new RandomAccessFile(fileToWriteName, "rw");

        } catch (FileNotFoundException e) {
            throw new ServiceException(fileToReadName + " doesn't exist.");
        }
    }

    @Override
    public void run() {
        int buffSize = CHUNK_SIZE < FileProcessor.bufferSize ? (int) CHUNK_SIZE : FileProcessor.bufferSize;
        long total = CHUNK_SIZE;
        HOLDER.resetThread(Thread.currentThread(), CHUNK_SIZE);

        while (total > 0) {
            int done = doAction(total, buffSize);

            total -= done;
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

    abstract int doAction(final long total, final int buffSize);

    int doAction(final long readPointer, final long writePointer, final int buffSize) {
        byte[] buffer = new byte[buffSize];
        int read = IO.read(RAF_READ, buffer, readPointer);
        IO.write(RAF_WRITE, buffer, writePointer, read);

        HOLDER.setThreadProgress(Thread.currentThread(), read);

        return read;
    }
}