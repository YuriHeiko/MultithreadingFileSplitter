package com.sysgears.processor.ui;

import com.beust.jcommander.JCommander;
import com.sysgears.processor.exceptions.FileProcessorException;
import com.sysgears.processor.exceptions.UIException;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public interface Executor {
    String execute(JCommander jCommander);

    default void startWorkers(final Collection<Runnable> workers, final int threadsNumber) {
        ExecutorService service = Executors.newFixedThreadPool(threadsNumber);
//        ExecutorService service = Executors.newSingleThreadExecutor();

        for (Runnable task : workers) {
            service.submit(task);
        }
        service.shutdown();

        try {
            while (service.awaitTermination(1000, TimeUnit.MILLISECONDS));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new FileProcessorException("Work awaiting was suddenly interrupted.");
        }
    }
}
