package com.sysgears.statistic;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractRecordsHolder<T, U> implements IHolder<T, U> {
    private AtomicLong progress = new AtomicLong(0);

    long getProgress() {
        return progress.get();
    }

    void changeProgress(long progress) {
        this.progress.addAndGet(progress);
    }
}
