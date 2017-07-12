package com.sysgears.processor.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Holder {
    private final Map<Long, Statistic> map = new ConcurrentSkipListMap<>();



    private class Statistic {
        private long total;
        private long done;
    }

}