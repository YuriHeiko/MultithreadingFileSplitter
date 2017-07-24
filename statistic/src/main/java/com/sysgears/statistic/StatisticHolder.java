package com.sysgears.statistic;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticHolder<T, U> implements IHolder<T, U> {
    private AtomicLong progress = new AtomicLong(0);

    long getProgress() {
        return progress.get();
    }

    void changeProgress(long progress) {
        this.progress.addAndGet(progress);
    }

    @Override
    public U add(T id, U value) {
        return null;
    }

    @Override
    public U get(T id) {
        return null;
    }

    @Override
    public U remove(T id) {
        return null;
    }

    @Override
    public Map<T, U> getAll() {
        return null;
    }
}
