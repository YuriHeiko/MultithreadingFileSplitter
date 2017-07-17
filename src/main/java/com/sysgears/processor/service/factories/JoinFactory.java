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
     * @param chunkNumber The number of a chunk
     */
    public JoinFactory(final String fileName, final String partPrefix, final StatisticHolder holder,
                       final int chunkNumber) {
        super(fileName, partPrefix, holder, chunkNumber);
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
            throw new ServiceException("The wrong file name of the first part." + fileName);
        }

        if (isFileExist(new File(parentName))) {
            throw new ServiceException("The joined file has already existed(" + parentName + ").");
        }

        List<Runnable> workers = new ArrayList<>();

        int chunkCounter = 0;
        for (File file = new File(getNextChunkName(parentName)); isFileExist(file);
             file = new File(getNextChunkName(parentName)), chunkCounter++) {

            long length = file.length();
            workers.add(new JoinChunk(new JoinerIO(), holder, parentName, file.getName(), parentFileLength, length));

            parentFileLength += length;
        }

        holder.setTotal(parentFileLength);

        return workers;
    }
}