package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunksSet;
import org.easymock.EasyMockSupport;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UTestFileChunksSet extends EasyMockSupport {

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

    @Test(expectedExceptions = ServiceException.class)
    public void testNext4() throws Exception {
        FileChunksSet iterator = new FileChunksSet(0, 0, 0, "", "");
        iterator.next();
    }

    @Test
    public void testHasNext5() throws Exception {
        assertTrue(new FileChunksSet(1, 1, 0, "", "").hasNext());
    }

    @Test
    public void testHasNext6() throws Exception {
        assertTrue(new FileChunksSet(2, 1, 0, "", "").hasNext());
    }

    @Test
    public void testNext7() throws Exception {
        FileChunksSet iterator = new FileChunksSet(6, 2, 0, "a", ".b");

        assertTrue(iterator.hasNext());
        ChunkProperties next = iterator.next();
        assertTrue(next.equals(new ChunkProperties(2, 0, "a.b0")));

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next.equals(new ChunkProperties(2, 2, "a.b1")));

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next.equals(new ChunkProperties(2, 4, "a.b2")));

        assertFalse(iterator.hasNext());
    }
}