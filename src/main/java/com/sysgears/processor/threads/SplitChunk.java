package com.sysgears.processor.threads;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;
import com.sysgears.processor.ui.FileProcessor;

class SplitChunk extends Chunk {
    SplitChunk(final IOHandler io, final StatisticHolder holder, final String fileToWriteName,
                      final String fileToReadName, final long pointer, final long chunkSize) {
        super(io, holder, fileToWriteName, fileToReadName, pointer, chunkSize);
    }

    @Override
    public void run() {
        int buffSize = chunkSize < FileProcessor.bufferSize ? (int)chunkSize : FileProcessor.bufferSize;
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
