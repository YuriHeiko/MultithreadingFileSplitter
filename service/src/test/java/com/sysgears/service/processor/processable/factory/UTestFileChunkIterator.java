package com.sysgears.service.processor.processable.factory;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.testng.annotations.*;
import org.easymock.*;
import org.testng.*;

import static org.testng.Assert.*;

import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class UTestFileChunkIterator extends EasyMockSupport {
    private final String partPrefix = ".part";
    private final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    private final String fileName = "test.test";
    private final int chunkSize = 2;
    private FileChunkIterator splitIterator;
    private IProcessableFactory joinFactory = new FileJoinFactory();
    private IProcessableFactory splitFactory = new FileSplitFactory();
    private int partNumber = 1;

    @BeforeMethod
    public void setUp() throws Exception {
        Path path = fs.getPath(fileName);
        Files.delete(path);
        Path file = Files.createFile(path);
        Files.write(file, "This is a test string".getBytes(StandardCharsets.UTF_8));

//        splitIterator = new FileChunkIterator(file.toFile().length(), fileName, chunkSize, partPrefix, partNumber,
//                                            new RandomAccessFile(file.toFile(), "rw"), splitFactory, fs);


    }

    @Test
    public void testHasNext() throws Exception {
    }

    @Test
    public void testNext() throws Exception {
    }

}