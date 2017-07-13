package com.sysgears.processor.threads;

import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.Collection;

public abstract class Processor {
    private String fileName;
    final StatisticHolder holder;
    private int startNumber;
    private int chunkCounter;
    long chunkSize;
    final String partPrefix;

    Processor(final String fileName, final String partPrefix, final StatisticHolder holder, final int startNumber) {
        this(fileName, partPrefix, holder, startNumber, 0);
    }

    Processor(final String fileName, final String partPrefix, final StatisticHolder holder, final int startNumber,
              final long chunkSize) {

        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.holder = holder;
        this.startNumber = startNumber;
        this.chunkSize = chunkSize;
    }

    protected String getParentFileName() {
        return fileName;
    }

    protected String getNextChunkName() {
        return fileName + partPrefix + startNumber++;
    }

    protected long getFileSize() {
        return new File(getParentFileName()).length();
    }

    public abstract Collection<Runnable> getWorkers();
}