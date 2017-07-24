package com.sysgears.statistic;


import javafx.util.Pair;
import java.util.Map;
import java.util.Set;

public class Watcher<T, U extends Pair<Long, Long>> implements Runnable {

    private final StatisticHolder<T, U> holder;
    private final long finalProgress;
    private final long outputDelay;

    public Watcher(StatisticHolder<T, U> holder, long finalProgress, long outputDelay) {
        this.holder = holder;
        this.finalProgress = finalProgress;
        this.outputDelay = outputDelay;
    }

    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder result = new StringBuilder();

//        System.out.println("Total: estimating\tTime: estimating");

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

            System.out.println(result);

            try {
                Thread.sleep(outputDelay);
            } catch (InterruptedException e) {
                throw new StatisticException("Statistic thread has been suddenly interrupted.");
            }

            result.setLength(0);
        }

        if (finalProgress - holder.getProgress() < 0) {
            throw new StatisticException("The job has been overdone.");
        }

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
    private String timeRemaining(final long total, final long done,final long startTime) {
        String result = "estimating";

        if (done != 0) {
            long timeSpent = System.currentTimeMillis() - startTime;
            result = String.valueOf((total * timeSpent / done - timeSpent) / 1000) + "s";
        }

        return result;
    }
}