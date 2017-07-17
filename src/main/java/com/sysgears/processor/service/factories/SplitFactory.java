package com.sysgears.processor.service.factories;

import com.sysgears.processor.io.SplitterIO;
import com.sysgears.processor.service.chunks.SplitChunk;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SplitFactory extends Factory {
    public SplitFactory(final String fileName, final String partPrefix, final StatisticHolder holder,
                        final int startNumber, final long chunkSize) {
        super(fileName, partPrefix, holder, startNumber, chunkSize);
    }

    @Override
    public Collection<Runnable> createChunks() {
        long fileSize = new File(fileName).length();

        holder.setTotal(fileSize);
        int chunksNumber = (int) (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);

        List<Runnable> workers = new ArrayList<>(chunksNumber);
        long size = chunkSize;
        for (int i = 0; i < chunksNumber; i++) {
            workers.add(new SplitChunk(new SplitterIO(), holder, getNextChunkName(fileName), fileName,
                    i * chunkSize, size));

            fileSize -= chunkSize;
            if (fileSize < size) {
                size = fileSize;
            }
        }

        return workers;
    }
}