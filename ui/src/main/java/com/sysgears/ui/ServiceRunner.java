package com.sysgears.ui;

import com.sysgears.service.FileWorkerFactory;
import com.sysgears.service.FileWorker;
import com.sysgears.statistic.Watcher;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    }

    /**
     * Starts service tasks
     */
    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(threadsNumber + 1);
        service.submit(watcher);

        FileWorker fileWorker;
        while ((fileWorker = factory.create()) != null) {
            service.submit(fileWorker);
        }
        service.shutdown();

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new UIException("Work awaiting has been suddenly interrupted.");
        }
    }
}
