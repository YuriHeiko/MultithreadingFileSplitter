package com.sysgears.processor.ui;

import com.beust.jcommander.JCommander;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Executor {
    String execute(JCommander jCommander);

    default void startWorkers(final Collection<Runnable> workers, final int threadsNumber) {
//        ExecutorService service = Executors.newFixedThreadPool(threadsNumber);
        ExecutorService service = Executors.newSingleThreadExecutor();

        for (Runnable task : workers) {
            service.submit(task);
        }

        service.shutdown();

        while (service.isTerminated());
    }
}
