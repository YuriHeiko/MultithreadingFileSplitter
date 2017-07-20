package com.sysgears.statistic;

import javafx.util.Pair;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentRecordsHolder<T, U extends Pair<Long, Long>> implements IHoldable<T, U> {

    private final Map<T, U> records = new ConcurrentSkipListMap<>();

    public U add(T id, U value) {
        return records.put(id, value);
    }

    public U get(T id) {
        return records.get(id);
    }

    public U remove(T id) {
        return records.remove(id);
    }

    public Map<T, U> getAll() {
        return new TreeMap<>(records);
    }
}