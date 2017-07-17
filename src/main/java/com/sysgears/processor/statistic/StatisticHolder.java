package com.sysgears.processor.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticHolder {
    private final Map<Long, ThreadStatistic> map = new ConcurrentSkipListMap<>();
    private Thread watcher;
    long total;
    AtomicLong progress = new AtomicLong(0);
    long startTime;

    public void setTotal(long total) {
        this.total = total;
    }

    public Thread getWatcher() {
        if (watcher == null) {
            watcher = new Thread(new StatisticWatcher(this));
        }

        return watcher;
    }

    public void resetThread(final Thread thread, final long total) {
        ThreadStatistic chunk = new ThreadStatistic(total);
        if (map.replace(thread.getId(), chunk) == null) {
            map.put(thread.getId(), chunk);
        }
    }

    public void setThreadProgress(final Thread thread, final long done) {
        map.get(thread.getId()).done += done;

        progress.addAndGet(done);
    }

    private String getPercent(final long total, final long done) {

        long t = (total == 0 ? 1 : total);
        double percent = done * 100.0 / t;

        return Double.compare(percent, 100.0) == 0 ? "idle" : String.format("%.2f%%", percent);
    }

    private String timeRemaining() {
        String result = "estimating";

        if (progress.get() != 0) {
            long timeSpent = System.currentTimeMillis() - startTime;
            result = String.valueOf((total * timeSpent / progress.get() - timeSpent) / 1000) + "s";
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Total progress: ");
        builder.append(getPercent(total, progress.get()));

        int counter = 0;
        for (Map.Entry<Long, ThreadStatistic> entry : map.entrySet()) {
            builder.append("\t thread ").append(++counter).append(": ").append(entry.getValue().getPercentDone());
        }

        return builder.append("\t time remaining: ").append(timeRemaining()).toString();
    }

    private class ThreadStatistic {
        private long total;
        private long done;

        ThreadStatistic(final long total) {
            this.total = total;
        }

        String getPercentDone() {
            return getPercent(total, done);
        }
    }
}