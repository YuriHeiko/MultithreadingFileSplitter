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
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChunkProperties that = (ChunkProperties) o;

        return size == that.size && pointer == that.pointer && fileName.equals(that.fileName);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = (int) (size ^ (size >>> 32));
        result = 31 * result + (int) (pointer ^ (pointer >>> 32));
        result = 31 * result + fileName.hashCode();
        return result;
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
