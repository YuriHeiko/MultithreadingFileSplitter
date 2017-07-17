package com.sysgears.processor.service.chunks;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

public class SplitChunk extends Chunk {
    public SplitChunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
                      final String fileToReadName, final long pointer, final long chunkSize) {
        super(io, holder, fileToWriteName, fileToReadName, pointer, chunkSize);
    }

    @Override
    public int doAction(final long total, final int buffSize) {
        return doAction(POINTER + CHUNK_SIZE - total, CHUNK_SIZE - total, buffSize);
    }
}