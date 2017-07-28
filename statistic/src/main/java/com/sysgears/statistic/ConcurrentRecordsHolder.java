package com.sysgears.statistic;

import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Keeps the threads statistic and the total progress. Threadsafe.
 *
 * @param <T> The thread id
 * @param <U> The {@code Pair} of the total thread progress and
 *            the current progress
 */
public class ConcurrentRecordsHolder<T, U extends Pair<Long, Long>> extends AbstractRecordsHolder<T, U> {
    /**
     * The records holder
     */
    private final Map<T, U> records = new ConcurrentSkipListMap<>();
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(ConcurrentRecordsHolder.class);

    /**
     * Adds a new record
     *
     * @param key   The thread id
     * @param value The record
     * @return The previous value
     */
    public U add(T key, U value) {
        long prevValue = 0;
        U prev = records.put(key, value);
        if (prev != null) {
            prevValue = prev.getValue();
        }

        long delta = value.getValue() - prevValue;

        log.debug("Changing the total progress");
        if (delta <= 0) {
            changeProgress(value.getValue());
        } else {
            changeProgress(delta);
        }

        log.debug("a new record was added. Thread: " + key + " thread progress: " + value.getValue() +
                ". Total progress: " + getProgress());
        return prev;
    }

    /**
     * Gets a record by a thread id
     *
     * @param key The thread id
     * @return The record
     */
    public U get(T key) {
        return records.get(key);
    }

    /**
     * Removes a record by a thread id
     *
     * @param key The thread id
     * @return The previous value
     */
    public U remove(T key) {
        return records.remove(key);
    }

    /**
     * Builds a map of all the records stored in the holder
     *
     * @return The map of all the keys and values
     */
    public Map<T, U> getAll() {
        return new TreeMap<>(records);
    }
}