package com.sysgears.ui;

import com.sysgears.ui.commands.CommandSplit;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import static org.testng.Assert.assertTrue;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

@Test(suiteName = "UI", testName = "CommandsTest")
public class ITestCommandSplit {
    private final String testPath = "/home/yuri/Documents/test/temp/";
    private final String testFile = "test.txt";
    private byte[] buffer;

    @Test(dataProvider = "SplitCommand", dataProviderClass = ITestDataProvider.class, groups = "UI")
    public void testCreate(final long chunkSize,
                           final int threadsNumber,
                           final int startNumber,
                           final int delay,
                           final String partPrefix,
                           final int bufferSize,
                           final long fileSize,
                           final int arraySize) throws Exception {
        try {
            buffer = populateBuffer(arraySize, arraySize);

            setUp(fileSize, "");

            // Do a command
            new CommandSplit(testPath + testFile, String.valueOf(chunkSize),
                                    threadsNumber, startNumber, delay, partPrefix, bufferSize).execute();

            // Check results
            Map<Integer, File> map = new TreeMap<>();
            for (File file : new File(testPath).listFiles()) {
                String name = file.getName();
                int ind = name.lastIndexOf(partPrefix) + partPrefix.length();
                if (file.isFile() && name.substring(0, ind).equals(testFile + partPrefix)) {
                    map.put(Integer.parseInt(name.substring(ind)), file);
                }
            }

            Vector<FileInputStream> vector = new Vector<>();
            int counter = 0;
            for (File file : map.values()) {
                long length = file.length();
                assertTrue(length == chunkSize || length == fileSize - chunkSize * counter,
                                file.getName() + " size: " + length + " should be equal to " +
                                        chunkSize + " or " + (fileSize - chunkSize * counter));
                vector.add(new FileInputStream(file));
                counter++;
            }

            SequenceInputStream stream = new SequenceInputStream(vector.elements());

            for (int i = 0; i < (fileSize / chunkSize + (fileSize % chunkSize > 0 ? 1 : 0)); i++) {
                byte[] readBuffer = new byte[arraySize];
                int j;
                for (j = 0; j < arraySize; j++) {
                    int b = stream.read();
                    if (b != -1) {
                        readBuffer[j] = (byte) b;
                    } else {
                        break;
                    }
                }
                byte[] expected = j == arraySize ? buffer : populateBuffer(arraySize, j);
                assertArrayEquals("Possibly, the wrong part: " + i, readBuffer, expected);
            }
        } finally {
            clean();
        }
    }

    private byte[] populateBuffer(int arraySize, int pointer) {
        byte[] result = new byte[arraySize];
        for (int i = 0; i < pointer; i++) {
            result[i] = (byte) i;
        }
        return result;
    }

    private void setUp(final long fileSize, final String postFix) throws IOException {
        Path file = Files.createFile(Paths.get(testPath + testFile + postFix));
        long progress = 0;
        while (fileSize - buffer.length > progress) {
            progress += buffer.length;
            Files.write(file, buffer, StandardOpenOption.APPEND);
        }

        Files.write(file, Arrays.copyOf(buffer, (int) (fileSize - progress)), StandardOpenOption.APPEND);
    }

    private void clean() {
        for (File file : new File(testPath).listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
}
