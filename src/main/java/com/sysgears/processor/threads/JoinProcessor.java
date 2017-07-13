package com.sysgears.processor.threads;

import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.statistic.StatisticHolder;

import java.util.Collection;

public class JoinProcessor extends Processor {
    public JoinProcessor(String fileName, String partPrefix, StatisticHolder holder, int startNumber) {
        super(fileName, partPrefix, holder, startNumber);
    }

    @Override
    public String getParentFileName() {
        return getParentFileName(super.getParentFileName());
    }

    private String getParentFileName(final String chunkName) {
        return chunkName.substring(0, chunkName.lastIndexOf(partPrefix));
    }

    @Override
    public Collection<Runnable> getWorkers() {
        return null;
    }

    class JoinChunk extends Chunk {
        public JoinChunk(IOHandler io, StatisticHolder holder, String fileToWriteName, String fileToReadName, int chunkNumber, long chunkSize) {
            super(io, holder, fileToWriteName, fileToReadName, chunkNumber, chunkSize);
        }

        @Override
        public void run() {
            int counter = 0;
            while (counter < chunkSize) {
                doAction(counter, chunkNumber * chunkSize + counter, ++counter);
            }
        }
    }

}