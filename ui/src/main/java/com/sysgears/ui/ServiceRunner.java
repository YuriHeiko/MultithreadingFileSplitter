package com.sysgears.ui;

import com.google.common.util.concurrent.UncaughtExceptionHandlers;
import com.sysgears.service.FileWorkersFactory;
import com.sysgears.statistic.Viewer;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.*;

/**
 * Runs a main File Processor service
 */
public class ServiceRunner {
    /**
     * The {@link FileWorkersFactory} to create a collection of chunks
     */
    private final FileWorkersFactory factory;
    /**
     * The {@link Viewer} to collect and show statistic
     */
    private final Viewer<Long, Pair<Long, Long>> viewer;
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
     * @param factory       The {@link FileWorkersFactory} to create a collection of chunks
     * @param viewer       The {@link Viewer} to show statistic
     * @param threadsNumber The number of threads in the thread pool
     */
    public ServiceRunner(final FileWorkersFactory factory,
                         final Viewer<Long, Pair<Long, Long>> viewer,
                         final int threadsNumber) {
        this.factory = factory;
        this.viewer = viewer;
        this.threadsNumber = threadsNumber;
        log.debug("object initialized.");
    }

    /**
     * Starts service tasks
     */
    public void run() {
        try {
            log.debug("Getting new thread pool.");
            ExecutorService service = Executors.newFixedThreadPool(threadsNumber);
            log.debug("Starting statistic viewer.");
            Thread viewerThread = new Thread(viewer);
            viewerThread.start();

            log.debug("Create and invoke workers.");
            List<Future<String>> futures = service.invokeAll(factory.create());

            service.shutdown();
            log.debug("Waiting for service shutting down.");
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

            for (Future<String> response : futures) {
                try {
                    String s = response.get();
                    if (!s.isEmpty()) {
                        log.error("An error has occurred during operations: " + s);
                        throw new UIException("An error has occurred during operations: " + s);
                    }
                } catch (ExecutionException e) {
                    viewerThread.interrupt();
                    log.error("A some thread work has been canceled: " + e.getMessage());
                    throw new UIException("A some thread work has been canceled: " + e.getMessage());
                }
            }

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Service shutting down awaiting has been suddenly interrupted: " + e.getMessage());
            throw new UIException("Service shutting down awaiting has been suddenly interrupted. Work has been halted: " + e.getMessage());
        }

        log.info("The work has been done successfully. Service is shut down.");
    }
}