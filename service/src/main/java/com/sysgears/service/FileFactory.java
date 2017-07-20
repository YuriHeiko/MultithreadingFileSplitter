package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.service.processor.splittable.ISplittable;

public class FileFactory {
    private final ISplittable<IProcessable> splittable;
    private final IProcessableProcessor processor;

    public FileFactory(final ISplittable<IProcessable> splittable, final IProcessableProcessor processor) {
        this.splittable = splittable;
        this.processor = processor;
    }

    public FileWorker create() {
        return splittable.hasMore() ? new FileWorker(splittable.nextPart(), processor) : null;
    }
}
