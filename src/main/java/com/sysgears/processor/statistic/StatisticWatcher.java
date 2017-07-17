package com.sysgears.processor.statistic;

public class StatisticWatcher implements Runnable {
    private StatisticHolder statisticHolder;

    StatisticWatcher(StatisticHolder statisticHolder) {
        this.statisticHolder = statisticHolder;
    }

    @Override
    public void run() {
        statisticHolder.startTime = System.currentTimeMillis();

        while (statisticHolder.total != statisticHolder.progress.get()) {
            System.out.println(statisticHolder.toString());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new StatisticHolderException("The statistic thread has been suddenly interrupted!");
            }
        }

        System.out.println(statisticHolder.toString());
        System.out.println("-----------------------------");
        System.out.println("All the tasks have been done.");
        System.out.println();
    }
}
