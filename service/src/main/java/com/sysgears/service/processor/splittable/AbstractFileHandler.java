package com.sysgears.service.processor.splittable;

import com.sysgears.service.processor.processable.IProcessable;

import java.util.Iterator;

public abstract class AbstractFileHandler implements Iterator<IProcessable> {
    final long fileSize;
    private final long chunkSize;

    long regress;

    AbstractFileHandler(final long fileSize, final long chunkSize) {
        this.fileSize = fileSize;
        this.chunkSize = chunkSize;

        regress = fileSize;
    }

    @Override
    public boolean hasNext() {
        return regress > 0;
    }

    void computeRegress() {
        regress -= regress > chunkSize ? chunkSize : regress;
    }
}
