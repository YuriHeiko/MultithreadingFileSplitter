package com.sysgears.service;

import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over possible chunks of a file
 */
public class FilePointerIterator implements Iterator<FilePointerIterator.Trinity> {
    /**
     * The size of the file
     */
    private final long fileSize;
    /**
     * The chunk size
     */
    private final long chunkSize;
    /**
     * The next part number
     */
    private int chunkNumber;
    /**
     * The regress of the iteration
     */
    private long regress;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FilePointerIterator.class);

    /**
     * Creates an object
     *
     * @param fileSize         The size of the file
     * @param chunkSize        The chunk size
     * @param firstChunkNumber The first part number
     */
    public FilePointerIterator(final long fileSize, final long chunkSize, final int firstChunkNumber) {
        if (fileSize <= 0 || chunkSize <= 0) {
            log.error("fileSize: " + fileSize + " | chunkSize: " + chunkSize);
            throw new ServiceException("The file and chunk size can be neither equal to 0 nor lesser than 0.");
        } else if (chunkSize > fileSize) {
            log.error("fileSize: " + fileSize + " | chunkSize: " + chunkSize);
            throw new ServiceException("The chunk size cannot be greater than the file size.");
        }

        this.fileSize = fileSize;
        this.chunkSize = chunkSize;
        chunkNumber = firstChunkNumber;
        regress = fileSize;

        log.info("initialized." + " | fileSize: " + fileSize +
                    " | chunkSize: " + chunkSize + " | firstChunkNumber: " + firstChunkNumber);
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
    public Trinity next() {
        if (!hasNext()) {
            log.warn("Trying to iterate but there is no more elements");
            throw new NoSuchElementException("Trying to iterate but there is no more elements");
        }
        long pointer = fileSize - regress;
        log.debug("next pointer: " + pointer);
        long size = regress > chunkSize ? chunkSize : regress;
        log.debug("next chunk size: " + regress);

        regress -= chunkSize;

        log.debug("Returning a new " + Pair.class.getSimpleName() + " object with file properties");
        return new Trinity(size, pointer, chunkNumber++);
    }

    public class Trinity {
        private final long size;
        private final long pointer;
        private final int chunkNumber;

        public Trinity(long size, long pointer, int chunkNumber) {
            this.size = size;
            this.pointer = pointer;
            this.chunkNumber = chunkNumber;
        }

        public long getSize() {
            return size;
        }

        long getPointer() {
            return pointer;
        }

        int getChunkNumber() {
            return chunkNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Trinity trinity = (Trinity) o;

            return size == trinity.size && pointer == trinity.pointer && chunkNumber == trinity.chunkNumber;
        }

        @Override
        public int hashCode() {
            int result = (int) (size ^ (size >>> 32));
            result = 31 * result + (int) (pointer ^ (pointer >>> 32));
            result = 31 * result + chunkNumber;
            return result;
        }
    }
}