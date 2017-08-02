package com.sysgears.ui;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(suiteName = "UI", testName = "InitTest")
public class ITestDataProvider {
    @DataProvider(name = "SplitCommand")
    public static Object[][] getIOProcessor() {
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
                new Object[]{10, 1, 1, 10000, ".part", 1024, 26, 8}};
    }
}