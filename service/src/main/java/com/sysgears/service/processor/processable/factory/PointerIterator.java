package com.sysgears.service.processor.processable.factory;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates a pointer
 */
public class PointerIterator implements Iterator<Long> {
    /**
     * The size of an object to iterate
     */
    private final long totalSize;
    /**
     * The size of one chunk
     */
    private final long chunkSize;
    /**
     * The regress of the iteration
     */
    private long regress;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(PointerIterator.class);

    /**
     * Constructs an object
     *
     * @param totalSize The size of an object to iterate
     * @param chunkSize The size of one chunk
     */
    PointerIterator(final long totalSize, final long chunkSize) {
        this.totalSize = totalSize;
        this.chunkSize = chunkSize;

        regress = totalSize;
        log.debug("a new object initialized, file size: " + totalSize + " chunk size: " + chunkSize);
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
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Long next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Trying to iterate but there is no more elements");
        }

        long result = totalSize - regress;
        regress -= regress > chunkSize ? chunkSize : this.regress;
        log.debug("next result: " + result);

        return result;
    }
}
