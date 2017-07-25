package com.sysgears.service.processor.processable;

import org.apache.log4j.Logger;

import java.io.RandomAccessFile;

/**
 * The processable chunk of a file
 */
public class FileChunk implements IProcessable {
    /**
     * The {@link RandomAccessFile} source
     */
    private final RandomAccessFile source;
    /**
     * The {@link RandomAccessFile} destination
     */
    private final RandomAccessFile destination;
    /**
     * The size of the source
     */
    private final long size;
    /**
     * The source offset
     */
    private final long sourceOffset;
    /**
     * The destination offset
     */
    private final long destinationOffset;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileChunk.class);

    /**
     * Constructs an object
     *
     * @param source            The {@code RandomAccessFile} source
     * @param destination       The {@code RandomAccessFile} destination
     * @param size              The size of the source
     * @param sourceOffset      The source offset
     * @param destinationOffset The destination offset
     */
    public FileChunk(final RandomAccessFile source,
                     final RandomAccessFile destination,
                     final long size,
                     final long sourceOffset,
                     final long destinationOffset) {
        this.source = source;
        this.destination = destination;
        this.size = size;
        this.sourceOffset = sourceOffset;
        this.destinationOffset = destinationOffset;
        log.debug("A new object initialized " + this);
    }

    /**
     * Returns the source offset
     *
     * @return The source offset
     */
    @Override
    public long getSourceOffset() {
        return sourceOffset;
    }

    /**
     * Returns the destination offset
     *
     * @return The destination offset
     */
    @Override
    public long getDestinationOffset() {
        return destinationOffset;
    }

    /**
     * Returns the source
     *
     * @return The source
     */
    @Override
    public RandomAccessFile getSource() {
        return source;
    }

    /**
     * Returns the destination
     *
     * @return The destination
     */
    @Override
    public RandomAccessFile getDestination() {
        return destination;
    }

    /**
     * Returns the size
     *
     * @return The size
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Returns a string representation of this
     *
     * @return the string representation of this
     */
    @Override
    public String toString() {
        return "FileChunk{size=" + size +
                ", sourceOffset=" + sourceOffset +
                ", destinationOffset=" + destinationOffset +
                '}';
    }
}
