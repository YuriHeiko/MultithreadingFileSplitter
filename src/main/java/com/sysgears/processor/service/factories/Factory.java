package com.sysgears.processor.service.factories;

import com.sysgears.processor.statistic.StatisticHolder;

import java.io.File;
import java.util.Collection;

public abstract class Factory {
    String fileName;
    final StatisticHolder holder;
    int chunkNumber;
    long chunkSize;
    final String partPrefix;

    Factory(final String fileName, final String partPrefix, final StatisticHolder holder, final int chunkNumber) {
        this(fileName, partPrefix, holder, chunkNumber, 0);
    }

    Factory(final String fileName, final String partPrefix, final StatisticHolder holder, final int chunkNumber,
            final long chunkSize) {

        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.holder = holder;
        this.chunkNumber = chunkNumber;
        this.chunkSize = chunkSize;
    }

    String getNextChunkName(final String fileName) {
        return fileName + partPrefix + chunkNumber++;
    }

    boolean isFileExist(final File file) {
        return file.exists() && file.isFile();
    }

    public abstract Collection<Runnable> createChunks();
}