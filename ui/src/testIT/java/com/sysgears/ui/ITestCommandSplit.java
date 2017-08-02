package com.sysgears.ui;

import com.sysgears.ui.commands.CommandJoin;
import com.sysgears.ui.commands.CommandSplit;
import org.testng.annotations.Test;

import java.io.*;
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
    private final String testPath = "./temp/";
    private final String testFile = "test.txt";
    private byte[] buffer;

    @Test(dataProvider = "SplitCommand", dataProviderClass = ITestDataProvider.class, groups = "UI")
    public void testSplitCommand(final long chunkSize,
                                 final int threadsNumber,
                                 final int startNumber,
                                 final int delay,
                                 final String partPrefix,
                                 final int bufferSize,
                                 final long fileSize,
                                 final int arraySize) throws Exception {
        try {
            buffer = populateBuffer(arraySize, arraySize);

            setUp(fileSize, "", 0);

            // Do a command
            PrintStream stream = System.out;
            System.setOut(new PrintStream(new ByteArrayOutputStream(1000)));
            new CommandSplit(testPath + testFile, String.valueOf(chunkSize),
                                threadsNumber, startNumber, delay, partPrefix, bufferSize).execute();
            System.setOut(stream);

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

            checkResultingFiles(new SequenceInputStream(vector.elements()),
                               (int) (fileSize / chunkSize + (fileSize % chunkSize > 0 ? 1 : 0)), arraySize);
        } finally {
            clean(new File(testPath));
        }
    }

    @Test(dataProvider = "JoinCommand", dataProviderClass = ITestDataProvider.class, groups = "UI")
    public void testJoinCommand(final long chunkSize,
                                final int threadsNumber,
                                final int delay,
                                final String partPrefix,
                                final int bufferSize,
                                final long fileSize,
                                final int arraySize) throws Exception {
        try {
            buffer = populateBuffer(arraySize, arraySize);

            long progress = 0;
            int counter = 0;
            int prevPointer = 0;
            while (fileSize > progress) {
                setUp(fileSize - progress > chunkSize ? chunkSize : fileSize - progress, partPrefix + ++counter,
                        prevPointer);
                progress += chunkSize;
                prevPointer = (int) (progress % arraySize);
            }

            // Do a command
            PrintStream stream = System.out;
            System.setOut(new PrintStream(new ByteArrayOutputStream(1000)));
            new CommandJoin(testPath + testFile + partPrefix + 1, threadsNumber, delay, partPrefix, bufferSize).execute();
            System.setOut(stream);

            // Check results
            checkResultingFiles(new FileInputStream(testPath + testFile),
                               (int) (fileSize / chunkSize + (fileSize % chunkSize > 0 ? 1 : 0)), arraySize);
        } finally {
            clean(new File(testPath));
        }
    }

    private void checkResultingFiles(InputStream stream, int iterations, int arraySize) throws IOException {
        for (int i = 0; i < iterations; i++) {
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
        stream.close();
    }

    private byte[] populateBuffer(final int arraySize, final int pointer) {
        byte[] result = new byte[arraySize];
        for (int i = 0; i < pointer; i++) {
            result[i] = (byte) i;
        }
        return result;
    }

    private void setUp(final long fileSize, final String postFix, final int startFrom) throws IOException {
        Files.createDirectories(Paths.get(testPath));
        Path file = Files.createFile(Paths.get(testPath + testFile + postFix));
        long progress = 0;

        if (startFrom > 0) {
            int bytesToWrite = buffer.length - startFrom > fileSize ? (int) (startFrom + fileSize) : buffer.length;
            Files.write(file, Arrays.copyOfRange(buffer, startFrom, bytesToWrite));
            progress = bytesToWrite - startFrom;
        }

        while (fileSize - buffer.length > progress) {
            progress += buffer.length;
            Files.write(file, buffer, StandardOpenOption.APPEND);
        }

        if (fileSize > progress) {
            Files.write(file, Arrays.copyOf(buffer, (int) (fileSize - progress)), StandardOpenOption.APPEND);
        }
    }

    private boolean clean(File dir) {
        if (dir.isDirectory()) {
            for (String child : dir.list()) {
                clean(new File(dir, child));
            }
        }
        return dir.delete();
    }
}