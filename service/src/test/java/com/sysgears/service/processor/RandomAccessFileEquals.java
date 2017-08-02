package com.sysgears.service.processor;

import org.easymock.IArgumentMatcher;

import java.io.RandomAccessFile;

public class RandomAccessFileEquals implements IArgumentMatcher {
    private final RandomAccessFile expected;

    public RandomAccessFileEquals(RandomAccessFile expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object argument) {
        return argument instanceof RandomAccessFile;
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append("eqRAF(");
        buffer.append(expected.getClass().getName());
        buffer.append(")");
    }
}