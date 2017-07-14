package com.sysgears.processor.statistic;

import com.sysgears.processor.exceptions.StatisticHolderException;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticHolder {
    private final Map<Long, Statistic> map = new ConcurrentSkipListMap<>();
    private long totalToBeDone;
    private AtomicLong totalDone = new AtomicLong(0);
    private long startTime;

    public StatisticHolder() {
    }

    public void setTotalToBeDone(long totalToBeDone) {
        this.totalToBeDone = totalToBeDone;
    }

    public void addNewWorker(final Thread thread, final long total) {
        Statistic worker = new Statistic(total, 0);
        if (map.replace(thread.getId(), worker) == null) {
            map.put(thread.getId(), worker);
        }
    }

    public void setWorkerDone(final Thread thread, final long done) {
        map.get(thread.getId()).done += done;

        totalDone.addAndGet(done);
    }

    public Thread startWatching() {
        if (startTime > 0) {
            throw new StatisticHolderException("An attempt to restart already started timer.");
        }

        startTime = System.currentTimeMillis();
        Thread statisticHandler = new Thread(new StatisticHandler(this));
        statisticHandler.start();

        return statisticHandler;
    }

    boolean isJobDone() {
        if (totalToBeDone < totalDone.get()) {
            throw new StatisticHolderException("Done work exceeded set bound." + " totalToBeDone: " + totalToBeDone +
                    " totalDone:" + totalDone);
        }

        return totalToBeDone == totalDone.get();
    }

    String getPercent(final long total, final long done) {
        long t = (total == 0 ? 1 : total);

        return String.format("%.2f", done * 100.0 / t);
    }

    private String timeRemaining() {
        String result = "estimating";

        if (totalDone.get() != 0) {
            long timeSpent = System.currentTimeMillis() - startTime;
            result = String.valueOf((totalToBeDone * timeSpent / totalDone.get() - timeSpent) / 1000) + "s";
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Total progress: ");
        builder.append(getPercent(totalToBeDone, totalDone.get())).append("%");

        int counter = 0;
        for (Map.Entry<Long, Statistic> entry : map.entrySet()) {
            builder.append("\t thread ").append(++counter).
                    append(": ").append(entry.getValue().getPercentDone()).append("%");
        }

        return builder.append("\t time remaining: ").append(timeRemaining()).toString();
    }

    private class Statistic {
        private long total;
        private long done;

        Statistic(final long total, final long done) {
            this.total = total;
            this.done = done;
        }

        String getPercentDone() {
            return getPercent(total, done);
        }
    }
}