package com.sysgears.service;

import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Spliterators;

import static org.easymock.EasyMock.expect;
import static org.testng.Assert.*;

public class ITTestFileWorkersFactory {


    public EasyMockRule mocks = new EasyMockRule(this);

    @Mock
    private IOProcessor processor;

    @Mock
    private Iterable<ChunkProperties> properties;

    @Mock
    private Iterator<ChunkProperties> iterator;

    @Mock
    private IProcessableFactory factory;

    @Mock
    private ChunkProperties chunkProperties;

    @Mock
    private String source;

    @TestSubject
    private FileWorkersFactory workersFactory;

    @Test
    public void testCreate() {
/*
        expect(properties.spliterator()).andReturn(Spliterators.spliteratorUnknownSize(iterator, 0));
        expect(iterator.hasNext()).andReturn(true).times(3).andReturn(false);
        expect(iterator.next()).andReturn(new ChunkProperties(2, 0, "chunks.part1")).
                                andReturn(new ChunkProperties(2, 2, "chunks.part2")).
                                andReturn(new ChunkProperties(1, 4, "chunks.part3"));
*/

    }
}