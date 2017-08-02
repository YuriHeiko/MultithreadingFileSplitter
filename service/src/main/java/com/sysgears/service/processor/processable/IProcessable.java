package com.sysgears.service.processor.processable;

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
    String getSource();

    /**
     * Returns the destination
     *
     * @return The destination
     */
    String getDestination();

    /**
     * Returns the size
     *
     * @return The size
     */
    long getSize();
}
