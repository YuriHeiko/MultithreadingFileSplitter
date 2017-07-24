package com.sysgears.service.processor;

import com.sysgears.io.IOHandler;
import com.sysgears.io.IOHandlerException;
import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.statistic.IHolder;
import javafx.util.Pair;
import org.apache.log4j.Logger;

public class IOProcessor implements IProcessableProcessor {
    private final IOHandler io;
    private final IHolder<Long, Pair<Long, Long>> holder;
    private final int bufferSize;
    private final static Logger logger = Logger.getLogger(IOProcessor.class);

    public IOProcessor(final IOHandler io, final IHolder<Long, Pair<Long, Long>> holder, final int bufferSize) {
        this.io = io;
        this.holder = holder;
        this.bufferSize = bufferSize;
    }

    @Override
    public boolean process(final IProcessable processable) {
        final byte[] buffer = new byte[bufferSize];
        int bytes;
        long progress = 0;
        long size = processable.getSize();

        while (progress < size) {
            try {
                bytes = io.read(processable.getSource(), buffer, processable.getSourceOffset() + progress);
            } catch (IOHandlerException e) {
                throw new ServiceException("Error during reading");
            }
            try {
                io.write(processable.getDestination(), buffer, processable.getDestinationOffset() + progress, bytes);
            } catch (IOHandlerException e) {
                throw new ServiceException("Error during writing");
            }
            progress += bytes;

//            logger.info(Thread.currentThread().getId() + " size: " + size + " progress: " + progress);
            holder.add(Thread.currentThread().getId(), new Pair<>(size, progress));
        }

        return true;
    }
}
