package com.sysgears.service.processor.splittable;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileSplitter extends AbstractFileHandler {
    private final String fileName;
    private final String partPrefix;
    private final RandomAccessFile source;

    private int partNumber;

    public FileSplitter(final long fileSize,
                        final String fileName,
                        final long chunkSize,
                        final String partPrefix,
                        final int partNumber) {
        super(fileSize, chunkSize);
        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.partNumber = partNumber;

        try {
            source = new RandomAccessFile(fileName, "r");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileName + " doesn't exist.");
        }
    }

    @Override
    public FileChunk next() {
        computeRegress();

        RandomAccessFile destination;

        try {
            destination = new RandomAccessFile(fileName + partPrefix + ++partNumber, "rw");
        } catch (FileNotFoundException e) {
            throw new ServiceException(fileName + partPrefix + ++partNumber + " wrong file name.");
        }

        return new FileChunk(source, destination, fileSize, fileSize - regress, 0);
    }
}
