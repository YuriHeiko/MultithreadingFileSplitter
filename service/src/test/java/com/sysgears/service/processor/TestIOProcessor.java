package com.sysgears.service.processor;

import com.sysgears.io.IOHandler;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.statistic.IHolder;
import javafx.util.Pair;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.testng.annotations.Test;

import java.io.RandomAccessFile;

import static org.easymock.EasyMock.*;

@Test
public class TestIOProcessor extends EasyMockSupport {
    @Mock
    private IOHandler ioHandler;

    @Mock
    private IHolder<Long, Pair<Long, Long>> holder;

    @Mock
    private IProcessable processable;

    @Mock
    private RandomAccessFile source;

    @Mock
    private RandomAccessFile destination;

    @Test
    public void testIOProcessor() throws Exception {
        test(8, 2, 2);
        resetAll();
        test(2, 2, 1024);
        resetAll();
        test(64_003, 9_997, 1_022);
    }

    private void test(final long sourceSize, final long chunkSize, final int bufferSize) throws Exception {
        final int chunksNumber = (int) (sourceSize / chunkSize) + (sourceSize % chunkSize > 0 ? 1 : 0);
        final int expBufferSize = bufferSize > chunkSize ? (int) chunkSize : bufferSize;

        // Create mocks
        EasyMockSupport.injectMocks(this);

        IOProcessor ioProcessor = new IOProcessor(ioHandler, holder, bufferSize);

        // IProcessable mock initialization
        expect(processable.getSource()).andReturn(source).times(chunksNumber);
        for (int i = 0; i < chunksNumber; i++) {
            expect(processable.getSourceOffset()).andReturn(chunkSize * i);
        }
        expect(processable.getDestination()).andReturn(destination).times(chunksNumber);

        expect(processable.getDestinationOffset()).andReturn(0L).times(chunksNumber);
        processable.close();
        expectLastCall().times(chunksNumber);

        // IOHandler and IHolder mock initialization
        for (int i = 0; i < chunksNumber; i++) {
            final long size = chunkSize > sourceSize - chunkSize * i ? sourceSize - chunkSize * i : chunkSize;
            expect(processable.getSize()).andReturn(size);
            long progress = 0;
            Pair<Long, Long> prev = null;
            while (progress < size) {
                int bytes = expBufferSize > size - progress ? (int) (size - progress) : expBufferSize;
                byte[] buffer = new byte[expBufferSize];
                expect(ioHandler.read(eq(source), aryEq(buffer), eq(chunkSize * i + progress))).andReturn(bytes);
                ioHandler.write(eq(destination), aryEq(buffer), eq(progress), eq(bytes));
                progress += bytes;

                Pair<Long, Long> pair = new Pair<>(size, progress);
                expect(holder.add(Thread.currentThread().getId(), pair)).andReturn(prev);
                prev = pair;
            }
        }

        replayAll();

        for (int i = 0; i < chunksNumber; i++) {
            ioProcessor.process(processable);
        }

        verifyAll();
    }
}