package com.sysgears.ui;

import com.sysgears.service.factories.Factory;
import com.sysgears.statistic.StatisticHolder;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceRunner {
    /**
     * The {@link Factory} to create a collection of chunks
     */
    private final Factory factory;
    /**
     * The {@link StatisticHolder} to collect and show statistic
     */
    private final StatisticHolder holder;
    /**
     * The number of threads in the thread pool
     */
    private final int threadsNumber;

    /**
     * Constructs an object
     *
     * @param factory       The {@link Factory} to create a collection of chunks
     * @param holder        The {@link StatisticHolder} to collect and show statistic
     * @param threadsNumber The number of threads in the thread pool
     */
    public ServiceRunner(Factory factory, StatisticHolder holder, int threadsNumber) {
        this.factory = factory;
        this.holder = holder;
        this.threadsNumber = threadsNumber;
    }

    /**
     * Starts service tasks
     */
    public void run() {
        Collection<Runnable> chunks = factory.createChunks();

        Thread statisticWatcher = holder.getWatcher();
        statisticWatcher.start();

        ExecutorService service = Executors.newFixedThreadPool(threadsNumber);

        for (Runnable chunk : chunks) {
            service.submit(chunk);
        }

        service.shutdown();

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new UIException("Work awaiting has been suddenly interrupted.");
        }

        try {
            statisticWatcher.join();
        } catch (InterruptedException e) {
            throw new UIException("Statistic's awaiting has been suddenly interrupted.");
        }
    }
}
