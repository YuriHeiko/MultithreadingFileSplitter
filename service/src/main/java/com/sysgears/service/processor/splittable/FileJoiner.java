package com.sysgears.service.processor.splittable;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

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
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileJoiner.class);

    public FileJoiner(final long fileSize,
                      final String fileName,
                      final long chunkSize,
                      final String partPrefix,
                      final int partNumber) {

        try {
            this.fileSize = fileSize;
            this.fileName = fileName.substring(0, fileName.lastIndexOf(partPrefix));
            this.chunkSize = chunkSize;
            this.partPrefix = partPrefix;
            this.partNumber = partNumber;
            this.iterator = new FileIterator(fileSize, chunkSize);
        } catch (StringIndexOutOfBoundsException e) {
            log.error(fileName + " wrong file name.");
            throw new ServiceException(fileName + " wrong file name.");
        }
        
        try {
            source = new RandomAccessFile(this.fileName, "rw");
        } catch (FileNotFoundException e) {
            log.error(this.fileName + " doesn't exist.");
            throw new ServiceException(this.fileName + " doesn't exist.");
        }

        log.info("initialized." + " fileName: " + this.fileName + " | fileSize: " + fileSize + " | chunkSize: " +
                chunkSize + " | prefix: " + partPrefix + " | starting number: " + partNumber);
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
            log.error(fileName + partPrefix + ++partNumber + " wrong file name.");
            throw new ServiceException(fileName + partPrefix + ++partNumber + " wrong file name.");
        }

        long offset = iterator.next();
        long size = fileSize - offset > chunkSize ? chunkSize : fileSize - offset;
        
        log.debug("Creating a new " + FileChunk.class.getSimpleName() + " object");
        return new FileChunk(destination, source, size, 0, offset);
    }
}
