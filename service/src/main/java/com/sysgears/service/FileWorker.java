package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * The worker to start processor work
 */
public class FileWorker implements Callable<String> {
    /**
     * The {@code IProcessable} to process
     */
    private final IProcessable processable;
    /**
     * The {@code IProcessableProcessor} to process
     */
    private final IProcessableProcessor processor;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileWorker.class);

    /**
     * Creates an object
     *
     * @param processable The {@code IProcessable} to process
     * @param processor   The {@code IProcessableProcessor} to process
     */
    FileWorker(final IProcessable processable,
               final IProcessableProcessor processor) {
        this.processable = processable;
        this.processor = processor;
        log.debug("a new object initialized");
    }

    /**
     * Runs processing
     */
    @Override
    public String call() {
        String message = "";
        log.info("Start processing: " + processable.toString());
        try {
            processor.process(processable);
        } catch (Throwable e) {
            message = e.getMessage();
        }

        return message;
    }
}