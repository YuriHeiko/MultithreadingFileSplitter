package com.sysgears.service.processor.splittable;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;

public abstract class AbstractFileHandler implements ISplittable<IProcessable> {
    final long fileSize;
    final long chunkSize;

    long regress;

    public AbstractFileHandler(final long fileSize, final long chunkSize) {
        this.fileSize = fileSize;
        this.chunkSize = chunkSize;

        regress = fileSize;
    }

    @Override
    public boolean hasMore() {
        return regress > 0;
    }

    void computeRegress() {
        regress -= regress > chunkSize ? chunkSize : regress;
    }
}
