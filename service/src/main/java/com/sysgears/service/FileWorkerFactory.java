package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;

import java.util.Iterator;

public class FileWorkerFactory {
    private final Iterator<IProcessable> processable;
    private final IProcessableProcessor processor;

    public FileWorkerFactory(final Iterator<IProcessable> processable, final IProcessableProcessor processor) {
        this.processable = processable;
        this.processor = processor;
    }

    public FileWorker create() {
        return processable.hasNext() ? new FileWorker(processable.next(), processor) : null;
    }
}
