package com.sysgears.service.processor;

import com.sysgears.io.IOHandler;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.statistic.IHolder;
import javafx.util.Pair;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import static org.easymock.EasyMock.expect;

@Test
@PrepareForTest({})
public class TestIOProcessor {
    private String testPath = "/home/yuri/Documents/test/temp/";

    @Mock
    private IOHandler ioHandler;

    @Mock
    private IHolder<Long, Pair<Long, Long>> holder;

    @Mock
    private IProcessable processable;

    @Test
    public void TestProcess() throws Exception {
        EasyMockSupport.injectMocks(this);
        IOProcessor ioProcessor = new IOProcessor(ioHandler, holder, 1024);
        RandomAccessFile source = new RandomAccessFile(testPath + "source.file", "rw");
        expect(processable.getDestination()).andReturn(null).times(5);
        expect(processable.getDestinationOffset()).andReturn(0L).andReturn(2L).andReturn(4L);
        expect(processable.getSource()).andReturn(null);
        expect(ioHandler.read(null, null, 0)).andReturn(0);
    }
}