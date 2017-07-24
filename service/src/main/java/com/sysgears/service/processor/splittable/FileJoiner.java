package com.sysgears.service.processor.splittable;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class FileJoiner implements Iterator<IProcessable> {
    private final String fileName;
    private final String partPrefix;
    private final RandomAccessFile source;
    private final long fileSize;
    private final long chunkSize;
    private final Iterator<Long> iterator;

    private int partNumber;

    public FileJoiner(final long fileSize,
                        final String fileName,
                        final long chunkSize,
                        final String partPrefix,
                        final int partNumber) {
        this.fileSize = fileSize;
        this.fileName = fileName.substring(0, fileName.lastIndexOf(partPrefix));
        this.chunkSize = chunkSize;
        this.partPrefix = partPrefix;
        this.partNumber = partNumber;
        this.iterator = new FileIterator(fileSize, chunkSize);

        try {
            source = new RandomAccessFile(this.fileName, "rw");
        } catch (FileNotFoundException e) {
            throw new ServiceException(this.fileName + " doesn't exist.");
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public FileChunk next() {
        RandomAccessFile destination;

        try {
            destination = new RandomAccessFile(fileName + partPrefix + ++partNumber, "rw");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileName + partPrefix + ++partNumber + " wrong file name.");
        }

        long offset = iterator.next();
        long size = fileSize - offset > chunkSize ? chunkSize : fileSize - offset;

        return new FileChunk(destination, source, size, 0, offset);
    }
}
