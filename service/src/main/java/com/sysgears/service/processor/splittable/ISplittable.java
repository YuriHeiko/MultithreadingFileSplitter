package com.sysgears.service.processor.splittable;

public interface ISplittable<T> {
    boolean hasMore();

    T nextPart();
}