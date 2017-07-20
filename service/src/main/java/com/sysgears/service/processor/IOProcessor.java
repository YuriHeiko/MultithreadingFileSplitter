package com.sysgears.service.processor;

import com.sysgears.io.IOHandler;
import com.sysgears.io.IOHandlerException;
import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.statistic.IHolder;
import javafx.util.Pair;

public class IOProcessor implements IProcessableProcessor {
    private final IOHandler io;
    private final IHolder<Long, Pair<Long, Long>> holder;
    private final int bufferSize;

    public IOProcessor(final IOHandler io, final IHolder<Long, Pair<Long, Long>> holder, final int bufferSize) {
        this.io = io;
        this.holder = holder;
        this.bufferSize = bufferSize;
    }

    @Override
    public boolean process(final IProcessable processable) {
        final byte[] buffer = new byte[bufferSize];
        int bytes;
        long regress = processable.getSize();

        while (regress > 0) {
            try {
                bytes = io.read(processable.getSource(), buffer, processable.getSourceOffset());
            } catch (IOHandlerException e) {
                throw new ServiceException("Error during reading");
            }
            try {
                io.write(processable.getDestination(), buffer, processable.getDestinationOffset(), bytes);
            } catch (IOHandlerException e) {
                throw new ServiceException("Error during writing");
            }

            holder.add(Thread.currentThread().getId(), new Pair<>(processable.getSize(), (long) bytes));
            regress -= bytes;
        }

        return true;
    }
}
