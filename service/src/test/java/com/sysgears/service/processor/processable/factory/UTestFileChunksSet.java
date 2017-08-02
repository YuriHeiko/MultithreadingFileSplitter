package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.UTestDataProvider;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunksSet;
import org.easymock.EasyMockSupport;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.testng.Assert.assertTrue;

@Test(suiteName = "Service", testName = "FileChunkSetTest")
public class UTestFileChunksSet extends EasyMockSupport {

    @Test(expectedExceptions = ServiceException.class, groups = "hasNext")
    public void testHasNextChunkGreaterFile() throws Exception {
        new FileChunksSet(1, 2, 0, "", "").hasNext();
    }

    @Test(expectedExceptions = ServiceException.class, groups = "hasNext")
    public void testHasNextZeroChunkSize() throws Exception {
        new FileChunksSet(1, 0, 0, "", "").hasNext();
    }

    @Test(expectedExceptions = ServiceException.class, groups = "hasNext")
    public void testHasNextNegativeFileSize() throws Exception {
        new FileChunksSet(-1, 1, 0, "", "").hasNext();
    }

    @Test(dependsOnGroups = "hasNext", groups = "next")
    public void testHasNextOk() throws Exception {
        FileChunksSet prop = new FileChunksSet(3, 1, 0, "", "");
        assertTrue(prop.hasNext());
        prop.next();
        assertTrue(prop.hasNext());
        prop.next();
        assertTrue(prop.hasNext());
        prop.next();
    }

    @Test(dataProvider = "nextNoMoreElements", dataProviderClass = UTestDataProvider.class,
            expectedExceptions = NoSuchElementException.class, dependsOnGroups = "hasNext", groups = "next")
    public void testNextNoMoreElements(final int fileSize, final int chunkSize, final String fileName,
                                       final String partPrefix) throws Exception {
        FileChunksSet iterator = new FileChunksSet(fileSize, chunkSize, 0, fileName, partPrefix);
        iterator.next();
        iterator.next();
    }

    @Test(dataProvider = "nextOk", dataProviderClass = UTestDataProvider.class, dependsOnGroups = "hasNext",
            dependsOnMethods = {"testHasNextOk", "testNextNoMoreElements"}, groups = "next")
    public void testNextOk(final int fileSize, final int chunkSize, final String fileName, final String partPrefix) throws Exception {
        final int chunksNumber = (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);

        final FileChunksSet iterator = new FileChunksSet(fileSize, chunkSize, 0, fileName, partPrefix);

        for (int i = 0; i < chunksNumber; i++) {
            assertTrue(iterator.hasNext());
            ChunkProperties next = iterator.next();
            assertTrue(next.getFileName().equals(fileName + partPrefix + i));
            assertTrue(next.getPointer() == chunkSize * i);
            assertTrue(next.getSize() == (fileSize - chunkSize * i > chunkSize ? chunkSize : fileSize - chunkSize * i));
            assertTrue(next.equals(new ChunkProperties((fileSize - chunkSize * i > chunkSize ? chunkSize :
                                                    fileSize - chunkSize * i),chunkSize * i, fileName + partPrefix + i)));
        }
    }
}