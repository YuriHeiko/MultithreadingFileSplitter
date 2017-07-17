package com.sysgears.processor.service.factories;

import com.sysgears.processor.io.SplitterIO;
import com.sysgears.processor.service.ServiceException;
import com.sysgears.processor.service.chunks.SplitChunk;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Factory methods to create and return a collections
 * of file chunks to break a file into parts
 */
public class SplitFactory extends Factory {
    /**
     * Constructs an object
     *
     * @param fileName    The name of a file
     * @param partPrefix  The prefix of a part
     * @param holder      The {@code StatisticHolder}
     * @param chunkNumber The number of a chunk
     * @param chunkSize   The size of the chunk
     */
    public SplitFactory(final String fileName, final String partPrefix, final StatisticHolder holder,
                        final int chunkNumber, final long chunkSize) {
        super(fileName, partPrefix, holder, chunkNumber, chunkSize);
    }

    /**
     * Creates and returns a collection of file chunks
     * to break a file into parts
     *
     * @return The collection of the chunks
     */
    @Override
    public Collection<Runnable> createChunks() {
        if (!isFileExist(new File(fileName))) {
            throw new ServiceException("The file does not exist (" + fileName + ").");
        }

        long fileSize = new File(fileName).length();
        int chunksNumber = (int) (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);
        holder.setTotal(fileSize);

        List<Runnable> workers = new ArrayList<>(chunksNumber);
        long size = chunkSize;
        for (int i = 0; i < chunksNumber; i++) {
            String nextChunkName = getNextChunkName(fileName);

            if (isFileExist(new File(nextChunkName))) {
                throw new ServiceException("The file has already existed(" + nextChunkName + ").");
            }

            workers.add(new SplitChunk(new SplitterIO(), holder, nextChunkName, fileName, i * chunkSize, size));

            fileSize -= chunkSize;
            // tackles the last part issue
            if (fileSize < size) {
                size = fileSize;
            }
        }

        return workers;
    }
}