package com.sysgears.processor.threads;

import com.sysgears.processor.exceptions.ThreadsException;
import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

abstract class Chunk implements Runnable {
    private final IOHandler io;
    private RandomAccessFile rafRead;
    private RandomAccessFile rafWrite;
    private final StatisticHolder holder;
    final long pointer;
    final long chunkSize;

    Chunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
          final String fileToReadName, final long pointer, final long chunkSize) {

        this.io = io;
        this.holder = holder;
        this.pointer = pointer;
        this.chunkSize = chunkSize;

        try {
            rafRead = new RandomAccessFile(fileToReadName, "r");
            rafWrite = new RandomAccessFile(fileToWriteName, "rw");

        } catch (FileNotFoundException e) {
            throw new ThreadsException(fileToReadName + " doesn't exist.");
        }
    }

    void doAction(final long readPointer, final long writePointer, final int buffSize) {
        byte[] buffer = new byte[buffSize];
        int read = io.read(rafRead, buffer, readPointer);
        io.write(rafWrite, buffer, writePointer);
        // TODO buffer.length isn't right!!!
        holder.setThreadDone(Thread.currentThread(), chunkSize, read);
    }

    void close() {
        try {
            rafRead.close();
            rafWrite.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}