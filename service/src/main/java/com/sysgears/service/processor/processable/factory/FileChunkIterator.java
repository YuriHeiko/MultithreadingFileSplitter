package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.processor.processable.FileChunk;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over possible chunks of a file
 */
public class FileChunkIterator implements Iterator<Pair<Long, Long>> {
    /**
     * The size of the file
     */
    private final long fileSize;
    /**
     * The chunk size
     */
    private final long chunkSize;
    /**
     * The regress of the iteration
     */
    private long regress;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileChunkIterator.class);

    /**
     * Creates an object
     *
     * @param fileSize           The size of the file
     * @param chunkSize          The chunk size
     */
    public FileChunkIterator(final long fileSize, final long chunkSize) {
        this.fileSize = fileSize;
        this.chunkSize = chunkSize;
        regress = fileSize;

        log.info("initialized." + " | fileSize: " + fileSize + " | chunkSize: " + chunkSize);
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
        log.debug("hasNext: " + (regress > 0));
        return regress > 0;
    }

    /**
     * Returns the next chunk properties
     *
     * @return the next chunk properties
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Pair<Long, Long> next() {
        if (!hasNext()) {
            log.warn("Trying to iterate but there is no more elements");
            throw new NoSuchElementException("Trying to iterate but there is no more elements");
        }
        long pointer = fileSize - regress;
        log.debug("next pointer: " + pointer);
        long size = fileSize - (pointer > chunkSize ? chunkSize : regress);
        log.debug("next chunk size: " + regress);

        regress -=  chunkSize;

        log.debug("Returning a new " + Pair.class.getSimpleName() + " object with file properties");
        return new Pair<>(pointer, size);
    }

}