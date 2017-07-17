package com.sysgears.processor.service.chunks;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

/**
 * Represents a part of a file to join
 */
public class JoinChunk extends Chunk {
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
    public JoinChunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
                     final String fileToReadName, final long pointer, final long chunkSize) {
        super(io, holder, fileToWriteName, fileToReadName, pointer, chunkSize);
    }

    /**
     * Calls the action to join a part to a resulting file
     *
     * @param total    The number of bytes to process
     * @param buffSize The size of the buffer
     * @return The number of processed bytes
     */
    @Override
    public int callAction(final long total, final int buffSize) {
        return doAction(CHUNK_SIZE - total, POINTER + CHUNK_SIZE - total, buffSize);
    }
}
