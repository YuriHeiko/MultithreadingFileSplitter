package com.sysgears.statistic;

public interface IWatchable<T, U> extends Runnable {
    IHoldable<T,U> getHolder();
}