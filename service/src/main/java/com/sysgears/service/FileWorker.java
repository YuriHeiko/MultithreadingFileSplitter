package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

public class FileWorker implements Runnable {
    private final IProcessable processable;
    private final IProcessableProcessor processor;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileWorker.class);

    public FileWorker(final IProcessable processable,
                      final IProcessableProcessor processor) {
        this.processable = processable;
        this.processor = processor;
        log.debug("a new object initialized");
    }

    @Override
    public void run() {
        log.info("Start processing: " + processable.toString());
        processor.process(processable);
    }
}