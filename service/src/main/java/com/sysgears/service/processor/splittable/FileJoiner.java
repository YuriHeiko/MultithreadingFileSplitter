package com.sysgears.service.processor.splittable;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileJoiner {
    private final String fileName;
    private final String partPrefix;
    private final RandomAccessFile destination;

    private int partNumber;

    public FileJoiner(final long fileSize,
                        final String fileName,
                        final long chunkSize,
                        final String partPrefix,
                        final int partNumber) {
        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.partNumber = partNumber;

        try {
            destination = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileName + " doesn't exist.");
        }

//        regress = fileSize;
    }

    public FileChunk next() {

        return null;
    }
}
