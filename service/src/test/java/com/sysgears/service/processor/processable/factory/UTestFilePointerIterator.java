package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.FilePointerIterator;
import com.sysgears.service.ServiceException;
import org.easymock.EasyMockSupport;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UTestFilePointerIterator extends EasyMockSupport {
/*
    private final String partPrefix = ".part";
    private final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    private final String fileName = "test.test";
    private final int chunkSize = 2;
    private FilePointerIterator splitIterator;
    private IProcessableFactory joinFactory = new FileJoinFactory();
    private IProcessableFactory splitFactory = new FileSplitFactory();
    private int partNumber = 1;

    @BeforeMethod
    public void setUp() throws Exception {
        Path path = fs.getPath(fileName);
        Files.delete(path);
        Path file = Files.createFile(path);
        Files.write(file, "This is a test string".getBytes(StandardCharsets.UTF_8));

//        splitIterator = new FilePointerIterator(file.toFile().length(), fileName, chunkSize, partPrefix, partNumber,
//                                            new RandomAccessFile(file.toFile(), "rw"), splitFactory, fs);


    }
*/

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNext1() throws Exception {
        new FilePointerIterator(1, 2, 0).hasNext();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNext2() throws Exception {
        new FilePointerIterator(1, 0, 0).hasNext();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNext3() throws Exception {
        new FilePointerIterator(-1, 0, 0).hasNext();
    }

    @Test
    public void testHasNext4() throws Exception {
        assertTrue(new FilePointerIterator(1, 1, 0).hasNext());
    }

    @Test
    public void testHasNext5() throws Exception {
        assertTrue(new FilePointerIterator(2, 1, 0).hasNext());
    }

    @Test
    public void testNext0() throws Exception {
        FilePointerIterator iterator = new FilePointerIterator(6, 2, 0);

        assertTrue(iterator.hasNext());
        FilePointerIterator.Trinity next = iterator.next();
        assertTrue(next.equals(iterator.new Trinity(2, 0, 0)));

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next.equals(iterator.new Trinity(2, 2, 1)));

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next.equals(iterator.new Trinity(2, 4, 2)));

        assertFalse(iterator.hasNext());
    }
}