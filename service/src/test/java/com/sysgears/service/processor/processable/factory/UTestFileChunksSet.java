package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunksSet;
import org.easymock.EasyMockSupport;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test(suiteName = "Service", testName = "InitTest")
public class UTestFileChunksSet extends EasyMockSupport {

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNextChunkGreaterFile() throws Exception {
        new FileChunksSet(1, 2, 0, "", "").hasNext();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNextZeroChunkSize() throws Exception {
        new FileChunksSet(1, 0, 0, "", "").hasNext();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testHasNextNegativeFileSize() throws Exception {
        new FileChunksSet(-1, 1, 0, "", "").hasNext();
    }

    @Test
    public void testHasNextOk() throws Exception {
        assertTrue(new FileChunksSet(1, 1, 0, "", "").hasNext());
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNextNoMoreElements() throws Exception {
        FileChunksSet iterator = new FileChunksSet(2, 2, 0, "", "");
        iterator.next();
        iterator.next();
    }
    @Test
    public void testNextOk() throws Exception {
        next(12, 5, "a", ".b");
        next(2, 1, "a", ".b");
        next(1, 1, "a", ".b");
        next(4, 2, "a", ".b");
    }

    private void next(final int fileSize, final int chunkSize, final String fileName, final String partPrefix) throws Exception {
        final int chunksNumber = (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);

        final FileChunksSet iterator = new FileChunksSet(fileSize, chunkSize, 0, fileName, partPrefix);

        for (int i = 0; i < chunksNumber; i++) {
            assertTrue(iterator.hasNext());
            ChunkProperties next = iterator.next();
            assertTrue(next.getFileName().equals(fileName + partPrefix + i));
            assertTrue(next.getPointer() == chunkSize * i);
            assertTrue(next.getSize() == (fileSize - chunkSize * i > chunkSize ? chunkSize : fileSize - chunkSize * i));
            assertTrue(next.equals(new ChunkProperties((fileSize - chunkSize * i > chunkSize ? chunkSize : fileSize - chunkSize * i),chunkSize * i, fileName + partPrefix + i)));
        }
    }
}