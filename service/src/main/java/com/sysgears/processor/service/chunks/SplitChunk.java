package com.sysgears.processor.service.chunks;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.statistic.StatisticHolder;

/**
 * Represents a part of a file to split
 */
public class SplitChunk extends Chunk {
    /**
     * Constructs an object
     *
     * @param io              The {@code IOHandler}
     * @param holder          The {@code StatisticHolder}
     * @param fileToWriteName The file-to-write file name
     * @param fileToReadName  The file-to-read file name
     * @param pointer         The file offset pointer
     * @param chunkSize       The size of this part
     */
    public SplitChunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
                      final String fileToReadName, final long pointer, final long chunkSize, final int bufferSize) {
        super(io, holder, fileToWriteName, fileToReadName, pointer, chunkSize, bufferSize);
    }

    /**
     * Calls the action to take a part from a file
     *
     * @param total    The number of bytes to process
     * @param buffSize The size of the buffer
     * @return The number of processed bytes
     */
    @Override
    public int callAction(final long total, final int buffSize) {
        return doAction(POINTER + CHUNK_SIZE - total, CHUNK_SIZE - total, buffSize);
    }
}