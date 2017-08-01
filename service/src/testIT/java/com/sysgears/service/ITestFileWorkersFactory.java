package com.sysgears.service;

import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.FileChunksSet;
import com.sysgears.service.processor.processable.factory.FileSplitFactory;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.easymock.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;

import static org.easymock.EasyMock.expect;

@Test
public class ITestFileWorkersFactory extends EasyMockSupport {
    private final String testPath = "/home/yuri/Documents/test/temp/";
    private final String sourceName = "source.file";
    private final String partPrefix = ".part";

    @Mock
    private IOProcessor processor;

/*
    @Mock
    IProcessableFactory factory;
*/

    @Test
    public void testCreate() throws Exception {
        EasyMockSupport.injectMocks(this);

        Iterable<ChunkProperties> properties = new FileChunksSet(6, 2, 1, testPath + sourceName, partPrefix);
        expect(processor.process(new FileChunk(testPath + sourceName, testPath + sourceName + partPrefix + 1, 2L, 0L, 0L))).andReturn(true);
//        expect(processor.process(new FileChunk(testPath + sourceName, testPath + sourceName + partPrefix + 2, 2L, 2L, 0L))).andReturn(true);
//        expect(processor.process(new FileChunk(testPath + sourceName, testPath + sourceName + partPrefix + 3, 2L, 4L, 0L))).andReturn(true);

        replayAll();

        Collection<Callable<String>> callables = new FileWorkersFactory(processor, properties, new FileSplitFactory(), testPath + sourceName).create();

        for (Callable<String> callable : callables) {
            callable.call();
        }

        verifyAll();
    }

}