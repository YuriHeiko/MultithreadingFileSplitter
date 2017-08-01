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
     * The source file name
     */
    private final String source;
    /**
     * The destination file name
     */
    private final String destination;
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
     * @throws ServiceException if an IO exception is occurred
     */
    public FileChunk(final String source,
                     final String destination,
                     final long size,
                     final long sourceOffset,
                     final long destinationOffset) throws ServiceException {
        this.size = size;
        this.sourceOffset = sourceOffset;
        this.destinationOffset = destinationOffset;
        this.source = source;
        this.destination = destination;

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
     * Returns the source file name
     *
     * @return The source file name
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * Returns the destination file name
     *
     * @return The destination file name
     */
    @Override
    public String getDestination() {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileChunk fileChunk = (FileChunk) o;

        return size == fileChunk.size &&
                    sourceOffset == fileChunk.sourceOffset &&
                        destinationOffset == fileChunk.destinationOffset &&
                            source.equals(fileChunk.source) && destination.equals(fileChunk.destination);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + (int) (size ^ (size >>> 32));
        result = 31 * result + (int) (sourceOffset ^ (sourceOffset >>> 32));
        result = 31 * result + (int) (destinationOffset ^ (destinationOffset >>> 32));
        return result;
    }

    /**
     * Returns a string representation of this
     *
     * @return the string representation of this
     */
    @Override
    public String toString() {
        return "FileChunk{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", size=" + size +
                ", sourceOffset=" + sourceOffset +
                ", destinationOffset=" + destinationOffset +
                '}';
    }
}
