package com.sysgears.processor.threads;

import com.sysgears.processor.exceptions.ThreadsException;
import com.sysgears.processor.io.JoinerIO;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JoinFactory extends Factory {
    public JoinFactory(final String fileName, final String partPrefix, final StatisticHolder holder,
                       final int startNumber) {
        super(fileName, partPrefix, holder, startNumber);
    }

    @Override
    public Collection<Runnable> getWorkers() {
        long parentFileLength = 0;
        String parentName = fileName.substring(0, fileName.lastIndexOf(partPrefix));

        try {
            chunkNumber = Integer.parseInt(fileName.substring(parentName.length() + partPrefix.length()));
        } catch (NumberFormatException e) {
            throw new ThreadsException("The wrong file name of the first part.");
        }

        List<Runnable> workers = new ArrayList<>();

        int chunkCounter = 0;
        for (File file = new File(getNextChunkName(parentName)); isFileExist(file);
             file = new File(getNextChunkName(parentName)), chunkCounter++) {

            long length = file.length();
            workers.add(new JoinChunk(new JoinerIO(), holder, parentName, file.getName(),
                    parentFileLength, length));

            parentFileLength += length;
        }

        holder.setTotalToBeDone(parentFileLength);

        return workers;
    }
}