package com.sysgears.service.processor.processable;

import com.sysgears.service.ServiceException;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class FileChunkIterator implements Iterator<IProcessable> {
    private final String fileName;
    private final String partPrefix;
    private final RandomAccessFile source;
    private final long fileSize;
    private final long chunkSize;
    private final Iterator<Long> iterator;
    private final IProcessableFactory processableFactory;

    private int partNumber;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileChunkIterator.class);

    public FileChunkIterator(final long fileSize,
                             final String fileName,
                             final long chunkSize,
                             final String partPrefix,
                             final int partNumber,
                             final RandomAccessFile source,
                             final IProcessableFactory processableFactory) {
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.chunkSize = chunkSize;
        this.partPrefix = partPrefix;
        this.partNumber = partNumber;
        this.iterator = new FilePointerIterator(fileSize, chunkSize);
        this.source = source;
        this.processableFactory = processableFactory;

        log.info("initialized." + " fileName: " + this.fileName + " | fileSize: " + fileSize + " | chunkSize: " +
                chunkSize + " | prefix: " + partPrefix + " | starting number: " + partNumber);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public IProcessable next() {
        RandomAccessFile destination;

        try {
            destination = new RandomAccessFile(fileName + partPrefix + partNumber++, "rw");
        } catch (FileNotFoundException e) {
            log.error(fileName + partPrefix + partNumber + " wrong file name.");
            throw new ServiceException(fileName + partPrefix + partNumber + " wrong file name.");
        }

        long offset = iterator.next();
        long size = fileSize - offset > chunkSize ? chunkSize : fileSize - offset;

        log.debug("Creating a new " + FileChunk.class.getSimpleName() + " object");
        return processableFactory.create(source, destination, size, offset);
    }
}
