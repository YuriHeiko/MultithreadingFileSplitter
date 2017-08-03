package com.sysgears.ui;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(suiteName = "UI", testName = "InitTest")
public class ITestDataProvider {
    @DataProvider(name = "SplitCommand")
    public static Object[][] getSplitArguments() {
/*
        final long chunkSize,
        final int threadsNumber,
        final int startNumber,
        final int delay,
        final String partPrefix,
        final int bufferSize,
        final long fileSize
        final int arraySize
*/
        return new Object[][]{
                new Object[]{10, 1, 1, 1000, ".part", 1024, 26, 8},
                new Object[]{15, 2, 1, 1000, ".part", 1024, 260, 80},
                new Object[]{15000, 1, 1, 100, ".t", 1024, 260000, 8000}
        };
    }

    @DataProvider(name = "JoinCommand")
    public static Object[][] getJoinArguments() {
/*
        final long chunkSize,
        final int threadsNumber,
        final int delay,
        final String partPrefix,
        final int bufferSize,
        final long fileSize,
        final int arraySize
*/
        return new Object[][]{
                new Object[]{10, 1, 1000, ".part", 1024, 26, 8},
                new Object[]{15, 2, 1000, ".part", 1024, 260, 80},
                new Object[]{15000, 4, 100, ".p", 1024, 260000, 80000}
        };
    }
}