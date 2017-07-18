package com.sysgears.processor.service.factories;

import com.sysgears.processor.io.JoinerIO;
import com.sysgears.processor.service.ServiceException;
import com.sysgears.processor.service.chunks.JoinChunk;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Factory methods to create and return a collections
 * of file chunks to join them into a file
 */
public class JoinFactory extends Factory {
    /**
     * Constructs an object
     *
     * @param fileName    The name of a file
     * @param partPrefix  The prefix of a part
     * @param holder      The {@code StatisticHolder}
     */
    public JoinFactory(final String fileName, final String partPrefix, final StatisticHolder holder, final int bufferSize) {
        super(fileName, partPrefix, holder, 0, bufferSize);
    }

    /**
     * Creates and returns a collection of file chunks
     * to join them into a file
     *
     * @return The collection of the chunks
     */
    @Override
    public Collection<Runnable> createChunks() {
        long parentFileLength = 0;
        String parentName;

        try {
            parentName = fileName.substring(0, fileName.lastIndexOf(partPrefix));
            chunkNumber = Integer.parseInt(fileName.substring(parentName.length() + partPrefix.length()));

        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            throw new ServiceException("The wrong file name of the first part: " + fileName);
        }

        if (isFileExist(new File(parentName))) {
            throw new ServiceException(parentName + " has already existed.");
        }

        List<Runnable> chunks = new ArrayList<>();

        int chunkCounter = 0;
        String nextChunkName = getNextChunkName(parentName);
        for (File file = new File(nextChunkName); isFileExist(file); file = new File(nextChunkName), chunkCounter++) {

            long length = file.length();
            chunks.add(new JoinChunk(new JoinerIO(), holder, parentName, nextChunkName, parentFileLength, length,
                    bufferSize));

            parentFileLength += length;

            nextChunkName = getNextChunkName(parentName);
        }

        holder.setTotal(parentFileLength);

        return chunks;
    }
}