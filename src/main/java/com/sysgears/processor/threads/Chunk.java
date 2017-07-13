package com.sysgears.processor.threads;

import com.sysgears.processor.exceptions.ThreadsException;
import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

abstract class Chunk implements Runnable {
    private final IOHandler io;
    private RandomAccessFile rafRead;
    private RandomAccessFile rafWrite;
    private final StatisticHolder holder;
    final int chunkNumber;
    final long chunkSize;

    Chunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
          final String fileToReadName, final int chunkNumber, final long chunkSize) {

        this.io = io;
        this.holder = holder;
        this.chunkNumber = chunkNumber;
        this.chunkSize = chunkSize;

        try {
            rafRead = new RandomAccessFile(fileToReadName, "r");
            rafWrite = new RandomAccessFile(fileToWriteName, "rw");

        } catch (FileNotFoundException e) {
            throw new ThreadsException(fileToReadName + " doesn't exist.");
        }
    }

    void doAction(final long readPointer, final long writePointer, final long counter) {
        int buffer = io.read(rafRead, readPointer, chunkNumber);
        io.write(rafWrite, buffer, writePointer, chunkNumber);
        holder.setThreadDone(Thread.currentThread(), chunkSize, counter);
    }

}
