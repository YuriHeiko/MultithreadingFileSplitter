package com.sysgears.processor.service.factories;

import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.Collection;

/**
 * Factory methods to create and return a collections
 * of file chunks to do some action with a file(s)
 */
public abstract class Factory {
    /**
     * A name of a file
     */
    String fileName;
    /**
     * The {@code StatisticHolder}
     */
    final StatisticHolder holder;
    /**
     * A number of a chunk
     */
    int chunkNumber;
    /**
     * A size of a chunk
     */
    long chunkSize;
    /**
     * A prefix of a part
     */
    final String partPrefix;

    /**
     * Constructs an object
     *
     * @param fileName    The name of a file
     * @param partPrefix  The prefix of a part
     * @param holder      The {@code StatisticHolder}
     * @param chunkNumber The number of a chunk
     */
    Factory(final String fileName, final String partPrefix, final StatisticHolder holder, final int chunkNumber) {
        this(fileName, partPrefix, holder, chunkNumber, 0);
    }

    /**
     * Constructs an object
     *
     * @param fileName    The name of a file
     * @param partPrefix  The prefix of a part
     * @param holder      The {@code StatisticHolder}
     * @param chunkNumber The number of a chunk
     * @param chunkSize   The size of the chunk
     */
    Factory(final String fileName, final String partPrefix, final StatisticHolder holder, final int chunkNumber,
            final long chunkSize) {

        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.holder = holder;
        this.chunkNumber = chunkNumber;
        this.chunkSize = chunkSize;
    }

    /**
     * Constructs and returns the next chunk file name
     *
     * @param fileName The parent file name
     * @return The next chunk file name
     */
    String getNextChunkName(final String fileName) {
        return fileName + partPrefix + chunkNumber++;
    }

    /**
     * Checks whether a file exist
     *
     * @param file {@code File} object to check
     * @return true if such file exists
     */
    boolean isFileExist(final File file) {
        return file.exists() && file.isFile();
    }

    /**
     * Creates and returns a collection of file chunks
     * to do some action with a file(s)
     *
     * @return The collection of the chunks
     */
    public abstract Collection<Runnable> createChunks();
}