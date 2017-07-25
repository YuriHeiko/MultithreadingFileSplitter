package com.sysgears.service.processor.splittable;

import org.apache.log4j.Logger;

import java.util.Iterator;

public class FileIterator implements Iterator<Long> {
    private final long fileSize;
    private final long chunkSize;

    private long regress;

    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileIterator.class);

    public FileIterator(final long fileSize, final long chunkSize) {
        this.fileSize = fileSize;
        this.chunkSize = chunkSize;

        regress = fileSize;
        log.debug("a new object initialized, file size: " + fileSize + " chunk size: " + chunkSize);
    }

    @Override
    public boolean hasNext() {
        log.debug("hasNext: " + (regress > 0));
        return regress > 0;
    }

    @Override
    public Long next() {
        long result = fileSize - regress;
        regress -= regress > chunkSize ? chunkSize : this.regress;
        log.debug("next result: " + result);

        return result;
    }
}
