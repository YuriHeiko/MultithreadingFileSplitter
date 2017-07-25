package com.sysgears.ui;

import com.sysgears.service.FileWorkerFactory;
import com.sysgears.service.FileWorker;
import com.sysgears.statistic.Watcher;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Runs a main File Processor service
 */
public class ServiceRunner {
    /**
     * The {@link FileWorkerFactory} to create a collection of chunks
     */
    private final FileWorkerFactory factory;
    /**
     * The {@link Watcher} to collect and show statistic
     */
    private final Watcher<Long, Pair<Long, Long>> watcher;
    /**
     * The number of threads in the thread pool
     */
    private final int threadsNumber;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(ServiceRunner.class);

    /**
     * Constructs an object
     *
     * @param factory       The {@link FileWorkerFactory} to create a collection of chunks
     * @param watcher       The {@link Watcher} to collect and show statistic
     * @param threadsNumber The number of threads in the thread pool
     */
    public ServiceRunner(final FileWorkerFactory factory,
                         final Watcher<Long, Pair<Long, Long>> watcher,
                         final int threadsNumber) {
        this.factory = factory;
        this.watcher = watcher;
        this.threadsNumber = threadsNumber;
        log.debug("object initialized");
    }

    /**
     * Starts service tasks
     */
    public void run() {
        log.debug("Getting new thread pool");
        ExecutorService service = Executors.newFixedThreadPool(threadsNumber + 1);
        log.debug("Starting statistic watcher");
        service.submit(watcher);

        FileWorker fileWorker;

        while ((fileWorker = factory.create()) != null) {
            log.debug("A new worker was created");
            service.submit(fileWorker);
            log.debug("A new worker was submitted to pool");
        }
        log.debug("Service shutting down");
        service.shutdown();

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Service shutting down awaiting has been suddenly interrupted.");
        }

        log.info("Service is shut down");
    }
}
