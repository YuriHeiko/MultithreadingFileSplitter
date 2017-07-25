package com.sysgears.service.processor.factory;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over possible chunks of a file
 */
public class FileChunkIterator implements Iterator<IProcessable> {
    /**
     * The size of the file
     */
    private final long fileSize;
    /**
     * The name of the file
     */
    private final String fileName;
    /**
     * The chunk size
     */
    private final long chunkSize;
    /**
     * The part prefix
     */
    private final String partPrefix;
    /**
     * The {@code RandomAccessFile} object of the source file
     */
    private final RandomAccessFile source;
    /**
     * The {@link PointerIterator} instance
     */
    private final Iterator<Long> iterator;
    /**
     * The {@link IProcessableFactory} instance
     */
    private final IProcessableFactory processableFactory;

    /**
     * The next part number
     */
    private int partNumber;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileChunkIterator.class);

    /**
     * Creates an object
     *
     * @param fileSize           The size of the file
     * @param fileName           The name of the file
     * @param chunkSize          The chunk size
     * @param partPrefix         The part prefix
     * @param partNumber         The first part number
     * @param source             The {@code RandomAccessFile} object of the source file
     * @param processableFactory The {@code IProcessableFactory} instance
     */
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
        this.iterator = new PointerIterator(fileSize, chunkSize);
        this.source = source;
        this.processableFactory = processableFactory;

        log.info("initialized." + " fileName: " + this.fileName + " | fileSize: " + fileSize + " | chunkSize: " +
                chunkSize + " | prefix: " + partPrefix + " | starting number: " + partNumber);
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public IProcessable next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Trying to iterate but there is no more elements");
        }

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
