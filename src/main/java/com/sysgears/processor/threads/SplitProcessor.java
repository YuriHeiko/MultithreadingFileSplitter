package com.sysgears.processor.threads;

import com.sysgears.processor.io.FileSystemHandler;
import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

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

        holder.setTotalToBeDone(fileSize);
        int chunksNumber = (int) (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);

        List<Runnable> workers = new ArrayList<>(chunksNumber);
        long size = chunkSize;
        for (int i = 0; i < chunksNumber; i++) {
            workers.add(new SplitChunk(FileSystemHandler.SPLITTER, holder, getNextChunkName(), getParentFileName(),
                    i * chunkSize, size));

            fileSize -= chunkSize;
            if (fileSize < size) {
                size = fileSize;
            }
        }

        return workers;
    }

    class SplitChunk extends Chunk {
        public SplitChunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
                          final String fileToReadName, final long pointer, final long chunkSize) {
            super(io, holder, fileToWriteName, fileToReadName, pointer, chunkSize);
        }

        @Override
        public void run() {
            int buffSize = chunkSize < 1024 ? (int)chunkSize : 1024;
            long toDo = chunkSize;
            holder.addNewWorker(Thread.currentThread(), chunkSize);

            while (toDo > 0) {
                int done = doAction(pointer + chunkSize - toDo, chunkSize - toDo, buffSize);

                toDo -= done;
                if (toDo < buffSize && toDo > 0) {
                    buffSize = (int) toDo;
                }
            }

            close();
        }
    }
}