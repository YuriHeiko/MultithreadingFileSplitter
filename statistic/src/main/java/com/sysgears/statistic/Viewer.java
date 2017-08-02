package com.sysgears.statistic;


import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Watches statistic kept in the {@link AbstractRecordsHolder} object
 * and prints it to System console
 *
 * @param <T> The thread id
 * @param <U> The record
 */
public class Viewer<T, U extends Pair<Long, Long>> implements Runnable {
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
    private final static Logger log = Logger.getLogger(Viewer.class);

    /**
     * Constructs an object
     *
     * @param holder        The {@code AbstractRecordsHolder} object
     * @param finalProgress The final progress
     * @param outputDelay   The output delay
     */
    public Viewer(AbstractRecordsHolder<T, U> holder, long finalProgress, long outputDelay) {
        this.holder = holder;
        this.finalProgress = finalProgress;
        this.outputDelay = outputDelay;
        log.debug("a new object initialized. finalProgress: " + finalProgress + " | outputDelay: " + outputDelay);
    }

    /**
     * Starts viewing statistics
     */
    @Override
    public void run() {
        log.info("Started statistical viewing");
        final long startTime = System.currentTimeMillis();
        final StringBuilder result = new StringBuilder();

        while (finalProgress > holder.getProgress()) {
            result.append("Total: ").append(getPercent(finalProgress, holder.getProgress()));
            for (Map.Entry<T, U> entry : holder.getAll().entrySet()) {
                U pair = entry.getValue();
                result.append("\tThread ").
                       append(entry.getKey()).
                       append(": ").
                       append(getPercent(pair.getKey(), pair.getValue()));
            }
            result.append("\tTime remaining: ").append(timeRemaining(finalProgress, holder.getProgress(), startTime));

            log.debug("A new statistical message: " + result + " was shown");
            System.out.println(result);
            result.setLength(0);

            try {
                Thread.sleep(outputDelay);
            } catch (InterruptedException e) {
                log.warn("The statistical thread was interrupted");
                return;
            }
        }

        if (finalProgress - holder.getProgress() < 0) {
            log.warn("The job has been overdone. Final progress: " +
                        finalProgress + " | Job progress: " + holder.getProgress());
        }

        log.info("Statistical viewing finished");
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

        return Double.compare(percent, 100.0) == 0 ? "idle\t" : String.format("%.2f%%", percent);
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