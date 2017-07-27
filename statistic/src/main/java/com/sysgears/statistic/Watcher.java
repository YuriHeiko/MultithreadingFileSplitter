package com.sysgears.statistic;


import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * Watches statistic kept in the {@link AbstractRecordsHolder} object
 * and prints it to System console
 *
 * @param <T> The thread id
 * @param <U> The record
 */
public class Watcher<T, U extends Pair<Long, Long>> implements Runnable {
    /**
     * The {@code AbstractRecordsHolder} object
     */
    private final AbstractRecordsHolder<T, U> holder;
    /**
     * The final progress
     */
    private final long finalProgress;
    /**
     * The output delay
     */
    private final long outputDelay;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(Watcher.class);

    /**
     * Constructs an object
     *
     * @param holder        The {@code AbstractRecordsHolder} object
     * @param finalProgress The final progress
     * @param outputDelay   The output delay
     */
    public Watcher(AbstractRecordsHolder<T, U> holder, long finalProgress, long outputDelay) {
        this.holder = holder;
        this.finalProgress = finalProgress;
        this.outputDelay = outputDelay;
        log.debug("a new object initialized");
    }

    /**
     * Starts watching
     */
    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();
        log.info("Started statistic watching");
        final StringBuilder result = new StringBuilder();

        while (finalProgress > holder.getProgress()) {
            Set<Map.Entry<T, U>> set = holder.getAll().entrySet();
            for (Map.Entry<T, U> entry : set) {
                U pair = entry.getValue();
                result.append("\tThread ").
                       append(entry.getKey()).
                       append(": ").
                       append(getPercent(pair.getKey(), pair.getValue()));
            }

            result.append("\tTime remaining: ").
                   append(timeRemaining(finalProgress, holder.getProgress(), startTime)).
                   insert(0, getPercent(finalProgress, holder.getProgress())).
                   insert(0, "Total: ");

            log.debug("A new statistic message: " + result);
            System.out.println(result);

            try {
                Thread.sleep(outputDelay);
            } catch (InterruptedException e) {
                log.error("Statistic thread has been suddenly interrupted.");
            }

            result.setLength(0);
        }

        if (finalProgress - holder.getProgress() < 0) {
            log.warn("The job has been overdone. Final progress: " + finalProgress + " job progress: " + holder.getProgress());
        }

        log.info("Statistic finished");
        System.out.println("Total: 100.00%\tTime remaining: 0s");
        System.out.println("-----------------------------");
        System.out.println("All the tasks have been done.");
        System.out.println();
    }

    /**
     * Returns a string contains a percentage ratio {@code done}
     * to {@code total}
     *
     * @param total The final number
     * @param done  The progress changing
     * @return The string contains a percentage ratio
     */
    private String getPercent(final long total, final long done) {
        long t = (total == 0 ? 1 : total);
        double percent = done * 100.0 / t;

        return Double.compare(percent, 100.0) == 0 ? "idle" : String.format("%.2f%%", percent);
    }

    /**
     * Computes and returns how much time all the threads need
     * to perform their actions
     *
     * @return The string contains estimated time in seconds
     */
    private String timeRemaining(final long total, final long done, final long startTime) {
        String result = "estimating";

        if (done != 0) {
            long timeSpent = System.currentTimeMillis() - startTime;
            result = String.valueOf((total * timeSpent / done - timeSpent) / 1000) + "s";
        }

        return result;
    }
}