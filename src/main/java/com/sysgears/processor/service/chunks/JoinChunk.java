package com.sysgears.processor.service.chunks;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

public class JoinChunk extends Chunk {
    public JoinChunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
                     final String fileToReadName, final long pointer, final long chunkSize) {
        super(io, holder, fileToWriteName, fileToReadName, pointer, chunkSize);
    }

    @Override
    public int doAction(final long total, final int buffSize) {
        return doAction(CHUNK_SIZE - total, POINTER + CHUNK_SIZE - total, buffSize);
    }
}
