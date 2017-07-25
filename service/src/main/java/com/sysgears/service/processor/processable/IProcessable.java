package com.sysgears.service.processor.processable;

import java.io.RandomAccessFile;

/**
 * Contains the common interface for the processable objects
 */
public interface IProcessable {
    /**
     * Returns the source offset
     *
     * @return The source offset
     */
    long getSourceOffset();

    /**
     * Returns the destination offset
     *
     * @return The destination offset
     */
    long getDestinationOffset();

    /**
     * Returns the source
     *
     * @return The source
     */
    RandomAccessFile getSource();

    /**
     * Returns the destination
     *
     * @return The destination
     */
    RandomAccessFile getDestination();

    /**
     * Returns the size
     *
     * @return The size
     */
    long getSize();
}
