package com.sysgears.service.processor.processable;

import com.sysgears.service.ServiceException;
import org.apache.log4j.Logger;

import java.io.IOException;
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
     * The {@code RandomAccessFile} destination
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
     * @param source            The source file name
     * @param destination       The destination file name
     * @param size              The size of the source
     * @param sourceOffset      The source offset
     * @param destinationOffset The destination offset
     */
    public FileChunk(final String source,
                     final String destination,
                     final long size,
                     final long sourceOffset,
                     final long destinationOffset) {
        this.size = size;
        this.sourceOffset = sourceOffset;
        this.destinationOffset = destinationOffset;

        try {
            log.debug("Trying to create the source file.");
            this.source = new RandomAccessFile(source, "rw");
        } catch (IOException e) {
            log.error(source + " wrong file name.");
            throw new ServiceException(source + " wrong file name.");
        }

        try {
            log.debug("Trying to create the source file.");
            this.destination = new RandomAccessFile(destination, "rw");
        } catch (IOException e) {
            log.error(source + " wrong file name.");
            throw new ServiceException(source + " wrong file name.");
        }

        log.debug("A new object initialized. Source: " + source + " | Destination: " + destination + " | " + this);
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

    @Override
    public void close() {
        try {
            log.debug("Trying to close the source file.");
            source.close();
            log.debug("Trying to close the destination file.");
            destination.close();
        } catch (IOException e) {
            log.error(source + "  error while closing file.");
            throw new ServiceException(source + " error while closing file.");
        }
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
