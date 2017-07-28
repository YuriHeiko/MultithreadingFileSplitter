package com.sysgears.service.processor.processable;

import com.sysgears.service.ServiceException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

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
     * The name of the file
     */
    private final String fileName;
    /**
     * The chunk file
     */
    private final RandomAccessFile file;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileChunksSet.class);

    /**
     * Creates an object
     *
     * @param size       The chunk size
     * @param pointer    The chunk pointer
     * @param fileSystem The {@code FileSystem}
     */
    public ChunkProperties(final long size, final long pointer, final String fileName, final FileSystem fileSystem) {
        this.size = size;
        this.pointer = pointer;
        this.fileName = fileName;

        try {
            log.debug("Trying to create the next chunk file.");
            file = new RandomAccessFile(Files.createFile(fileSystem.getPath(fileName)).toFile(), "rw");
        } catch (IOException e) {
            log.error(fileName + " wrong file name.");
            throw new ServiceException(fileName + " wrong file name.");
        }
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
     * Returns the chunk file
     *
     * @return the chunk file
     */
    public RandomAccessFile getFile() {
        return file;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
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
}
