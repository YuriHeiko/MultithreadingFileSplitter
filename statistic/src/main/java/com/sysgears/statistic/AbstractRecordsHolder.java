package com.sysgears.statistic;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Adds a possibility to store current progress
 *
 * @param <T> The key
 * @param <U> The value
 */
public abstract class AbstractRecordsHolder<T, U> implements IHolder<T, U> {
    /**
     * The current progress
     */
    private final AtomicLong progress = new AtomicLong(0);

    /**
     * Gets current progress
     *
     * @return The current progress
     */
    long getProgress() {
        return progress.get();
    }

    /**
     * Changes current progress
     *
     * @param progress The current progress
     */
    void changeProgress(long progress) {
        this.progress.addAndGet(progress);
    }
}
