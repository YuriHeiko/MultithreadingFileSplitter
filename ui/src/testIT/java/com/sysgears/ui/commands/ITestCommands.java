package com.sysgears.ui.commands;

import com.sysgears.ui.IExecutable;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.Test;

import java.lang.reflect.Executable;

@Test
public class ITestCommands {
    String testPath = "/home/yuri/Documents/test/temp/";
    String testFile = "test.txt";
    String partPrefix = "p";

    @Test
    public void testCreate() {
        IExecutable command = new CommandSplit(testPath, "3", 2, 1, 1000, partPrefix, 1024);

        command.execute();
    }

    void prepareFile(final int size) {

    }
}
