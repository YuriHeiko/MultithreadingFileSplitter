package com.sysgears.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Holds statistical data of performing threads
 */
public class StatisticHolder {
    /**
     * The {@code ConcurrentSkipListMap} contains performing threads
     * and their progress.
     */
    private final Map<Long, ThreadStatistic> statisticMap = new ConcurrentSkipListMap<>();
    /**
     * The thread of the {@link StatisticWatcher} that shows statistical data
     */
    private Thread watcher;
    /**
     * The final number of threads work.
     */
    long total;
    /**
     * The currents progress
     */
    AtomicLong progress = new AtomicLong(0);
    /**
     * The starting time
     */
    long startTime;
    /**
     * The delay
     */
    int delay;

    /**
     * Constructs an object
     *
     * @param delay The delay of statistic output
     */
    public StatisticHolder(int delay) {
        this.delay = delay;
    }

    /**
     * Sets a final value of threads work.
     *
     * @param total The final value of threads work
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * Creates and returns a {@code StatisticWatcher} object.
     *
     * @return The {@code StatisticWatcher} object
     */
    public Thread getWatcher() {
        if (watcher == null) {
            watcher = new Thread(new StatisticWatcher(this));
        }

        return watcher;
    }

    /**
     * Resets a progress of a thread. Creates a new element of
     * {@code statisticMap}, if it is a new one.
     *
     * @param thread The thread to reset the progress
     * @param total  The final number of the thread work
     */
    public void resetProgress(final Thread thread, final long total) {
        ThreadStatistic chunk = new ThreadStatistic(total);
        if (statisticMap.replace(thread.getId(), chunk) == null) {
            statisticMap.put(thread.getId(), chunk);
        }
    }

    /**
     * Sets a current progress of a thread.
     *
     * @param thread The thread
     * @param done   The progress changing
     */
    public void setProgress(final Thread thread, final long done) {
        statisticMap.get(thread.getId()).done += done;
        progress.addAndGet(done);
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
    private String timeRemaining() {
        String result = "estimating";

        if (progress.get() != 0) {
            long timeSpent = System.currentTimeMillis() - startTime;
            result = String.valueOf((total * timeSpent / progress.get() - timeSpent) / 1000) + "s";
        }

        return result;
    }

    /**
     * Builds and returns a string representation of statistical information
     *
     * @return The statistical string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Total progress: ");
        builder.append(getPercent(total, progress.get()));

        int counter = 0;
        for (Map.Entry<Long, ThreadStatistic> entry : statisticMap.entrySet()) {
            builder.append("\t thread ").append(++counter).append(": ").append(entry.getValue().getPercentDone());
        }

        return builder.append("\t time remaining: ").append(timeRemaining()).toString();
    }

    /**
     * Contains a statistical pair
     */
    private class ThreadStatistic {
        /**
         * The final number of the thread work
         */
        private long total;
        /**
         * The progress changing
         */
        private long done;

        /**
         * Constructs an object
         *
         * @param total The final number of the thread work
         */
        ThreadStatistic(final long total) {
            this.total = total;
        }

        /**
         * Returns a string contains a percentage ratio {@code done}
         * to {@code total}
         *
         * @return The string contains a percentage ratio
         */
        String getPercentDone() {
            return getPercent(total, done);
        }
    }
}