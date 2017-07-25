package com.sysgears.statistic;

import java.util.Map;

/**
 * Contains common interface for holding some data
 *
 * @param <T> The key
 * @param <U> The value
 */
public interface IHolder<T, U> {
    /**
     * Adds a new record
     *
     * @param key   The key
     * @param value The value
     * @return The previous value
     */
    U add(T key, U value);

    /**
     * Gets a value by a key
     *
     * @param key The key
     * @return The value
     */
    U get(T key);

    /**
     * Removes a value by a key
     *
     * @param key The key
     * @return The previous value
     */
    U remove(T key);

    /**
     * Builds a map of all the records stored in the holder
     *
     * @return The map of all the keys and values
     */
    Map<T, U> getAll();
}