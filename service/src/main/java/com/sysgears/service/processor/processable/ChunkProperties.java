package com.sysgears.service.processor.processable;

import org.apache.log4j.Logger;

/**
 * Contains chunk properties
 */
public class ChunkProperties {
    /**
     * The chunk size
     */
    private final long size;
    /**
     * The chunk pointer
     */
    private final long pointer;
    /**
     * The file name
     */
    private final String fileName;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileChunksSet.class);

    /**
     * Creates an object
     *
     * @param size     The chunk size
     * @param pointer  The chunk pointer
     * @param fileName The file name
     */
    public ChunkProperties(final long size, final long pointer, final String fileName) {
        this.size = size;
        this.pointer = pointer;
        this.fileName = fileName;
        log.debug("object initialized: " + this);
    }

    /**
     * Returns the chunk size
     *
     * @return the chunk size
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns the chunk pointer
     *
     * @return the chunk pointer
     */
    public long getPointer() {
        return pointer;
    }

    /**
     * Returns the file name
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return  a string representation of the object.
     */
    @Override
    public String toString() {
        return "size=" + size +
                ", pointer=" + pointer +
                ", fileName='" + fileName;
    }
}
