package com.sysgears.service.processor.splittable;

import java.util.Iterator;

public class FileIterator implements Iterator<Long> {
    private final long fileSize;
    private final long chunkSize;

    private long regress;

    public FileIterator(final long fileSize, final long chunkSize) {
        this.fileSize = fileSize;
        this.chunkSize = chunkSize;

        regress = fileSize;
    }

    @Override
    public boolean hasNext() {
        return regress > 0;
    }

    @Override
    public Long next() {
        long result = fileSize - regress;

        regress -= regress > chunkSize ? chunkSize : this.regress;

        return result;
    }
}
