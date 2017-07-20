package com.sysgears.io;

public interface IHandler<T> {
    boolean seek(T t, long pointer);

    int read(T t, byte[] buffer);

    boolean write(T t, byte[] buffer, int length);
}
