package com.sysgears.service.processor.splittable;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileJoiner implements ISplittable<FileChunk> {
    private final long fileSize;
    private final String fileName;
    private final long chunkSize;
    private final String partPrefix;
    private final RandomAccessFile destination;

    private int partNumber;
    private long regress;

    public FileJoiner(final long fileSize,
                      final String fileName,
                      final long chunkSize,
                      final String partPrefix,
                      final int partNumber) {
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.chunkSize = chunkSize;
        this.partPrefix = partPrefix;
        this.partNumber = partNumber;

        try {
            destination = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileName + " doesn't exist.");
        }

        regress = fileSize;
    }

    @Override
    public boolean hasMore() {
        return regress > 0;
    }

    @Override
    public FileChunk nextPart() {

        return null;
    }
}
