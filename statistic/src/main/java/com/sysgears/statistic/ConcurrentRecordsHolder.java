package com.sysgears.statistic;

import javafx.util.Pair;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentRecordsHolder<T, U extends Pair<Long, Long>> extends StatisticHolder<T, U> {

    private final Map<T, U> records = new ConcurrentSkipListMap<>();

    public U add(T id, U value) {
        long prevValue = 0;
        U prev = records.put(id, value);
        if (prev != null) {
            prevValue = prev.getValue();
        }

        long delta = value.getValue() - prevValue;

        if (delta < 0) {
            changeProgress(value.getValue());
        } else {
            changeProgress(delta);
        }

        return prev;
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