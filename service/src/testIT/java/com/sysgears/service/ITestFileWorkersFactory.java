package com.sysgears.service;

import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.FileChunksSet;
import com.sysgears.service.processor.processable.factory.FileSplitFactory;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.concurrent.Callable;

import static org.easymock.EasyMock.expect;

@Test(suiteName = "ServiceIT", testName = "FileWorkersFactoryTestIT")
public class ITestFileWorkersFactory extends EasyMockSupport {
    private final String testPath = "/home/yuri/Documents/test/temp/";
    private final String sourceName = "source.file";
    private final String partPrefix = ".part";

    @Mock
    private IOProcessor processor;

    @Test(groups = "ITServiceTest")
    public void testCreate() throws Exception {
        EasyMockSupport.injectMocks(this);

        final Iterable<ChunkProperties> properties = new FileChunksSet(6, 2, 1, testPath + sourceName, partPrefix);

        expect(processor.process(new FileChunk(testPath + sourceName,
                                               testPath + sourceName + partPrefix + 1, 2L, 0L, 0L))).andReturn(true);
        expect(processor.process(new FileChunk(testPath + sourceName,
                                               testPath + sourceName + partPrefix + 2, 2L, 2L, 0L))).andReturn(true);
        expect(processor.process(new FileChunk(testPath + sourceName,
                                               testPath + sourceName + partPrefix + 3, 2L, 4L, 0L))).andReturn(true);
        replayAll();

        Collection<Callable<String>> callables = new FileWorkersFactory(processor, properties,
                                                                new FileSplitFactory(), testPath + sourceName).create();
        for (Callable<String> callable : callables) {
            callable.call();
        }

        verifyAll();
    }
}