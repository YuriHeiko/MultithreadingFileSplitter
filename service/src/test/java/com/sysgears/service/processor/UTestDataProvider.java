package com.sysgears.service.processor;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(suiteName = "Service", testName = "InitTest")
public class UTestDataProvider {
    @DataProvider(name = "nextNoMoreElements")
    public static Object[][] getNextNoMoreElements() {
        return new Object[][]{
                new Object[]{1, 1, "a", ".b"}};
    }

    @DataProvider(name = "nextOk")
    public static Object[][] getNextOk() {
        return new Object[][]{
                new Object[]{1, 1, "a", ".b"},
                new Object[]{2, 1, "a", ".b"},
                new Object[]{3, 2, "a", ".b"},
                new Object[]{4, 2, "a", ".b"}};
    }

    @DataProvider(name = "IOProcessor")
    public static Object[][] getIOProcessor() {
        return new Object[][]{
                new Object[]{8, 2, 2},
                new Object[]{2, 2, 1024},
                new Object[]{64_003, 9_997, 1_022}};
    }
}