package com.sysgears.service.processor.processable;

import com.sysgears.service.ServiceException;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a set of file chunks of a file
 */
public class FileChunksSet implements Iterator<ChunkProperties>, Iterable<ChunkProperties> {
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
     * The name of the file
     */
    private final String fileName;
    /**
     * The {@link FileSystem} instance
     */
    private final FileSystem fileSystem;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileChunksSet.class);

    /**
     * Creates an object with the default {@code FileSystem}
     *
     * @param fileSize         The size of the file
     * @param chunkSize        The chunk size
     * @param firstChunkNumber The first part number
     * @param fileName         The name of the file
     * @param partPrefix       The part prefix
     */
    public FileChunksSet(final long fileSize,
                         final long chunkSize,
                         final int firstChunkNumber,
                         final String fileName,
                         final String partPrefix) {
        this(fileSize, chunkSize, firstChunkNumber, fileName, partPrefix, FileSystems.getDefault());
    }

    /**
     * Creates an object
     *
     * @param fileSize         The size of the file
     * @param chunkSize        The chunk size
     * @param firstChunkNumber The first part number
     * @param fileName         The name of the file
     * @param partPrefix       The part prefix
     * @param fileSystem       The {@code FileSystem}
     */
    public FileChunksSet(final long fileSize,
                         final long chunkSize,
                         final int firstChunkNumber,
                         final String fileName,
                         final String partPrefix,
                         final FileSystem fileSystem) {
        if (fileSize <= 0 || chunkSize <= 0) {
            log.error("fileSize: " + fileSize + " | chunkSize: " + chunkSize);
            throw new ServiceException("The file and chunk size can be neither equal to 0 nor lesser than 0.");
        } else if (chunkSize > fileSize) {
            log.error("fileSize: " + fileSize + " | chunkSize: " + chunkSize);
            throw new ServiceException("The chunk size cannot be greater than the file size.");
        }

        this.fileSize = fileSize;
        this.chunkSize = chunkSize;
        this.fileName = fileName + partPrefix;
        this.fileSystem = fileSystem;
        chunkNumber = firstChunkNumber;
        regress = fileSize;

        log.info("initialized." + " | fileSize: " + fileSize + " | fileName: " + fileName + " | partPrefix: " +
                partPrefix + " | chunkSize: " + chunkSize + " | firstChunkNumber: " + firstChunkNumber);
    }

    /**
     * Returns this as Iterator
     *
     * @return this
     */
    @Override
    public Iterator<ChunkProperties> iterator() {
        return this;
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
    public ChunkProperties next() {
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
        return new ChunkProperties(size, pointer, fileName + chunkNumber++, fileSystem);
    }
}