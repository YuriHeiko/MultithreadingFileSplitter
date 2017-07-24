package com.sysgears.statistic;


import javafx.util.Pair;
import java.util.Map;
import java.util.Set;

public class Watcher<T, U extends Pair<Long, Long>> implements Runnable {

    private final IHolder<T, U> holder;
    private final long finalProgress;
    private final long outputDelay;

    public Watcher(IHolder<T, U> holder, long finalProgress, long outputDelay) {
        this.holder = holder;
        this.finalProgress = finalProgress;
        this.outputDelay = outputDelay;
    }

    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder result = new StringBuilder();
        long regress = finalProgress;

        System.out.println("Total: estimating\tTime: estimating");

        while (regress > 0) {
            Set<Map.Entry<T, U>> set = holder.getAll().entrySet();
            for (Map.Entry<T, U> entry : set) {
                U pair = entry.getValue();
                regress -= pair.getValue();
                result.append("\tThread ").
                       append(entry.getKey()).
                       append(": ").
                       append(getPercent(pair.getKey(), pair.getValue()));
            }

            if (regress < 0) {
                break;
            }

            result.append("\tTime remaining: ").
                   append(timeRemaining(finalProgress - regress, startTime)).
                   insert(0, getPercent(finalProgress, finalProgress - regress)).
                   insert(0, "Total: ");

            System.out.println(result);

            try {
                Thread.sleep(outputDelay);
            } catch (InterruptedException e) {
                throw new StatisticException("Statistic thread has been suddenly interrupted.");
            }

            result.setLength(0);
        }

/*
        if (regress < 0) {
            throw new StatisticException("The job has been overdone.");
        }
*/

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
    private String timeRemaining(final long total, final long startTime) {
        String result = "estimating";

        if (total != 0) {
            long timeSpent = System.currentTimeMillis() - startTime;
            result = String.valueOf((total * timeSpent / total - timeSpent) / 1000) + "s";
        }

        return result;
    }
}