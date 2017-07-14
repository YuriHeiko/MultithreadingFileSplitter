package com.sysgears.processor.statistic;

import com.sysgears.processor.exceptions.StatisticHolderException;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticHolder {
    private final Map<Long, Statistic> map = new ConcurrentSkipListMap<>();
    private long totalToBeDone;
    private AtomicLong totalDone = new AtomicLong(1);
    private long startTime;

    public StatisticHolder() {
    }

    public void setTotalToBeDone(long totalToBeDone) {
        this.totalToBeDone = totalToBeDone;
    }

    public void setThreadDone(final Thread thread, final long total, final long done) {
        if (startTime == 0) {
            throw new StatisticHolderException("The timer has not been started.");
        }
//        System.out.println(thread.getId() + "| total: " + total + " | done: " + done);
        Statistic statistic = map.get(thread.getId());

        if (statistic != null) {
            statistic.total = total;
            statistic.done = done;

        } else {
            map.put(thread.getId(), new Statistic(total, done));
        }

        totalDone.addAndGet(done);
    }

    public void timerStart() {
        if (startTime > 0) {
            throw new StatisticHolderException("An attempt to restart already started timer.");
        }

        startTime = System.currentTimeMillis();
        new Thread(new StatisticHandler(this)).start();
    }

    boolean isJobDone() {
        if ((totalToBeDone + 1)  < totalDone.get()) {
            throw new StatisticHolderException("Done work exceeded set bound." + " totalToBeDone: " + totalToBeDone +
                    " totalDone:" + totalDone);
        }

        return (totalToBeDone +1) == totalDone.get();
    }

    String getPercent(final long total, final long done) {
        long t = (total == 0 ? 1 : total);

        return String.valueOf((done * 10000 / t) / 100.0);
    }

    String timeRemaining() {
        long timeSpent = System.currentTimeMillis() - startTime;
        return String.valueOf((totalToBeDone * timeSpent / totalDone.get() - timeSpent) / 1000);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Total progress: ");
        builder.append(getPercent(totalToBeDone, totalDone.get())).append("%");

        int counter = 0;
        for (Map.Entry<Long, Statistic> entry : map.entrySet()) {
            builder.append(", thread ").append(++counter).append(": ").append(entry.getValue().getPercentDone());
        }

        return builder.append(", time remaining: ").append(timeRemaining()).append("s").toString();
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