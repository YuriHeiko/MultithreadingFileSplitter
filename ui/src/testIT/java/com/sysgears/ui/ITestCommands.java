package com.sysgears.ui;

import com.sysgears.ui.commands.CommandSplit;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Test
public class ITestCommands {
    String testPath = "/home/yuri/Documents/test/temp/";
    String testFile = "test.txt";
    String partPrefix = "p";

    @Test
    public void testCreate() throws Exception {
        setUp(Integer.MAX_VALUE);
        new CommandSplit(testPath, "3", 2, 1, 1000, partPrefix, 1024).execute();
        clean();
    }

    private void setUp(final long fileSize) throws IOException {
        int partSize = 64_000;

        Path file = Files.createFile(Paths.get(testPath + testFile));
        long progress = 0;
        while (fileSize - partSize > progress) {
            progress += partSize;
            Files.write(file, new byte[partSize]);
        }

        Files.write(file, new byte[(int) (fileSize - progress)]);
    }

    private void clean() throws IOException {
        for (File file : new File(testPath).listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
}
