package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;

public class FileWorker implements Runnable {
    private final IProcessable processable;
    private final IProcessableProcessor processor;

    public FileWorker(final IProcessable processable,
                      final IProcessableProcessor processor) {
        this.processable = processable;
        this.processor = processor;
    }

    @Override
    public void run() {
        processor.process(processable);
    }
}