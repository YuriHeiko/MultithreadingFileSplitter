package com.sysgears.ui;

import com.sysgears.ui.commands.CommandSplit;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Test
public class ITestCommandSplit {
    private final String testPath = "/home/yuri/Documents/test/temp/";
    private final String testFile = "test.txt";
    private final String partPrefix = "p";
    private final byte[] buffer = new byte[256];
    {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) i;
        }
    }

    @Test
    public void testCreate() throws Exception {
        setUp(Integer.MAX_VALUE);
        new CommandSplit(testPath, "3", 2, 1, 1000, partPrefix, 1024).execute();
        clean();
    }

    private void setUp(final long fileSize) throws IOException {
        Path file = Files.createFile(Paths.get(testPath + testFile));
        long progress = 0;
        while (fileSize - buffer.length > progress) {
            progress += buffer.length;
            Files.write(file, buffer);
        }

        Files.write(file, new byte[(int) (fileSize - progress)]);
    }

    private void clean() {
        for (File file : new File(testPath).listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
}
