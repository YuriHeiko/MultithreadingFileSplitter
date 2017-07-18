package com.sysgears.ui.commands;

import com.beust.jcommander.JCommander;
import com.sysgears.processor.service.factories.Factory;
import com.sysgears.statistic.StatisticHolder;
import com.sysgears.ui.UIException;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The commands' interface
 */
public abstract class Command {
    /**
     * The command to execute
     *
     * @param jCommander The {@code JCommander} object
     * @return The string with the command representation
     */
    public abstract String execute(final JCommander jCommander);

    /**
     * Starts received tasks
     *
     * @param factory       The {@link Factory} to create a collection of
     *                      chunks
     * @param holder        The {@link StatisticHolder} to collect and show
     *                      statistic
     * @param threadsNumber The number of threads in the thread pool
     */
    void startTasks(final Factory factory, final StatisticHolder holder, final int threadsNumber) {
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
