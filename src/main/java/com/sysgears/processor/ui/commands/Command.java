package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.sysgears.processor.ui.UIException;

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
    public abstract String execute(JCommander jCommander);

    /**
     * Starts received tasks
     *
     * @param tasks         The collection of tasks
     * @param threadsNumber The number of threads in the thread pool
     */
    public void startTasks(final Collection<Runnable> tasks, final int threadsNumber) {
        ExecutorService service = Executors.newFixedThreadPool(threadsNumber);

        for (Runnable task : tasks) {
            service.submit(task);
        }

        service.shutdown();

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new UIException("Work awaiting was suddenly interrupted.");
        }
    }

}
