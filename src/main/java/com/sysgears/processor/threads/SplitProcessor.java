package com.sysgears.processor.threads;

import com.sysgears.processor.io.FileSystemHandler;
import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHandler;
import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SplitProcessor extends Processor {
    public SplitProcessor(String fileName, String partPrefix, StatisticHolder holder, int startNumber, long chunkSize) {
        super(fileName, partPrefix, holder, startNumber, chunkSize);
    }

    @Override
    public Collection<Runnable> getWorkers() {
        long fileSize = getFileSize();
        int chunksNumber = (int) (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);

        List<Runnable> workers = new ArrayList<>(chunksNumber);
        for (int i = 0; i < chunksNumber; i++) {
            workers.add(new SplitChunk(FileSystemHandler.SPLITTER, holder, getNextChunkName(), getParentFileName(), i,
                    chunkSize));
        }

        holder.setTotalToBeDone(fileSize);
        holder.timerStart();

        return workers;
    }

    class SplitChunk extends Chunk {
        public SplitChunk(IOHandler io, StatisticHolder holder, String fileToWriteName, String fileToReadName, int chunkNumber, long chunkSize) {
            super(io, holder, fileToWriteName, fileToReadName, chunkNumber, chunkSize);
        }

        @Override
        public void run() {
            int counter = 0;
            while (counter < chunkSize) {
                doAction(chunkNumber * chunkSize + counter, counter, ++counter);
            }
        }
    }
}