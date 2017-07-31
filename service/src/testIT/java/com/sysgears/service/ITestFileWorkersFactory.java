package com.sysgears.service;

import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.factory.FileSplitFactory;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;

import static org.easymock.EasyMock.expect;

@Test
@PrepareForTest({IOProcessor.class, Iterable.class, Iterator.class, IProcessableFactory.class, ChunkProperties.class})
public class ITestFileWorkersFactory extends EasyMockSupport {

    @Mock
    private IOProcessor processor;

    @Mock
    private Iterable<ChunkProperties> properties;

    @Mock
    private Iterator<ChunkProperties> iterator;

    @Test
    public void testCreate() {
        EasyMockSupport.injectMocks(this);
        FileWorkersFactory workersFactory = new FileWorkersFactory(processor, properties, new FileSplitFactory(), "chunks");
        expect(properties.spliterator()).andReturn(Spliterators.spliteratorUnknownSize(iterator, 0));
        expect(iterator.hasNext()).andReturn(true).times(3).andReturn(false);
        expect(iterator.next()).andReturn(new ChunkProperties(2, 0, "chunks.part1")).
                                andReturn(new ChunkProperties(2, 2, "chunks.part2")).
                                andReturn(new ChunkProperties(1, 4, "chunks.part3"));
        Collection<Callable<String>> callables = workersFactory.create();

    }
}