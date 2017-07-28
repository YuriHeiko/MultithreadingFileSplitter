package com.sysgears.service.processor.processable.factory;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunksSet;
import org.easymock.EasyMockSupport;
import org.testng.annotations.Test;

import java.nio.file.FileSystem;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UTestFileChunkIterator extends EasyMockSupport {
    private final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());

/*
    private final String partPrefix = ".part";
    private final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    private final String fileName = "test.test";
    private final int chunkSize = 2;
    private FileChunksSet splitIterator;
    private IProcessableFactory joinFactory = new FileJoinFactory();
    private IProcessableFactory splitFactory = new FileSplitFactory();
    private int partNumber = 1;

    @BeforeMethod
    public void setUp() throws Exception {
        Path path = fs.getPath(fileName);
        Files.delete(path);
        Path file = Files.createFile(path);
        Files.write(file, "This is a test string".getBytes(StandardCharsets.UTF_8));

//        splitIterator = new FileChunksSet(file.toFile().length(), fileName, chunkSize, partPrefix, partNumber,
//                                            new RandomAccessFile(file.toFile(), "rw"), splitFactory, fs);


    }
*/

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNext1() throws Exception {
        new FileChunksSet(1, 2, 0, "", "").hasNext();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNext2() throws Exception {
        new FileChunksSet(1, 0, 0, "", "").hasNext();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNext3() throws Exception {
        new FileChunksSet(-1, 0, 0, "", "").hasNext();
    }

    @Test
    public void testHasNext4() throws Exception {
        assertTrue(new FileChunksSet(1, 1, 0, "", "").hasNext());
    }

    @Test
    public void testHasNext5() throws Exception {
        assertTrue(new FileChunksSet(2, 1, 0, "", "").hasNext());
    }

    @Test
    public void testNext0() throws Exception {
        FileChunksSet iterator = new FileChunksSet(6, 2, 0, "a", ".b", fs);

        assertTrue(iterator.hasNext());
        ChunkProperties next = iterator.next();
        assertTrue(next.equals(new ChunkProperties(2, 0, "a.b1", fs)));

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next.equals(new ChunkProperties(2, 2, "a.b2", fs)));

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next.equals(new ChunkProperties(2, 4, "a.b3", fs)));

        assertFalse(iterator.hasNext());
    }
}