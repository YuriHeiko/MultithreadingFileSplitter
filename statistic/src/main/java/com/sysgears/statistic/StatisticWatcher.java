package com.sysgears.statistic;

/**
 * Watches and shows statistical data that contains in a
 * {@link StatisticHolder} object
 */
public class StatisticWatcher implements Runnable {
    /**
     * The {@code StatisticHolder} object
     */
    private StatisticHolder statisticHolder;

    /**
     * Constructs an object
     *
     * @param statisticHolder The {@code StatisticHolder} object
     */
    StatisticWatcher(final StatisticHolder statisticHolder) {
        this.statisticHolder = statisticHolder;
    }

    /**
     * Shows the statistic
     */
    @Override
    public void run() {
        statisticHolder.startTime = System.currentTimeMillis();

        while (statisticHolder.total != statisticHolder.progress.get()) {
            System.out.println(statisticHolder.toString());

            try {
                Thread.sleep(statisticHolder.delay);
            } catch (InterruptedException e) {
                throw new StatisticHolderException("The statistical thread has been suddenly interrupted!");
            }
        }

        System.out.println(statisticHolder.toString());
        System.out.println("-----------------------------");
        System.out.println("All the tasks have been done.");
        System.out.println();
    }
}
