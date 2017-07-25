package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * Creates {@code FileWorker} objects
 */
public class FileWorkerFactory {
    /**
     * The {@code IProcessableProcessor} instance
     */
    private final IProcessableProcessor processor;
    /**
     * The {@code IProcessable} instance
     */
    private final Iterator<IProcessable> processable;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileWorkerFactory.class);

    /**
     * Creates an object
     *
     * @param processor   The {@code IProcessableProcessor} instance
     * @param processable The {@code IProcessable} instance
     */
    public FileWorkerFactory(final IProcessableProcessor processor, final Iterator<IProcessable> processable) {
        this.processor = processor;
        this.processable = processable;
        log.debug("a new object initialized");
    }

    /**
     * Creates {@code FileWorker} object
     *
     * @return The {@code FileWorker} object
     */
    public FileWorker create() {
        log.info("Creating a new file worker.");
        return processable.hasNext() ? new FileWorker(processable.next(), processor) : null;
    }
}
