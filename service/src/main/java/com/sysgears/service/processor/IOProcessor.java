package com.sysgears.service.processor;

import com.sysgears.io.IOHandler;
import com.sysgears.io.IOHandlerException;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.statistic.IHolder;
import javafx.util.Pair;
import org.apache.log4j.Logger;

public class IOProcessor implements IProcessableProcessor {
    private final IOHandler io;
    private final IHolder<Long, Pair<Long, Long>> holder;
    private final int bufferSize;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(IOProcessor.class);

    /**
     * Creates an object
     *
     * @param io
     * @param holder
     * @param bufferSize
     */
    public IOProcessor(final IOHandler io, final IHolder<Long, Pair<Long, Long>> holder, final int bufferSize) {
        this.io = io;
        this.holder = holder;
        this.bufferSize = bufferSize;
        log.debug("a new object initialized");
    }

    @Override
    public boolean process(final IProcessable processable) {
        final byte[] buffer = new byte[bufferSize];
        int bytes = 0;
        long progress = 0;
        long size = processable.getSize();

        while (progress < size) {
            try {
                bytes = io.read(processable.getSource(), buffer, processable.getSourceOffset() + progress);
                log.debug("Read from source offset: " + (processable.getSourceOffset() + progress) + " bytes: " + bytes);
            } catch (IOHandlerException e) {
                log.error("Error during reading, source: " + processable.toString());
            }
            try {
                io.write(processable.getDestination(), buffer, processable.getDestinationOffset() + progress, bytes);
                log.debug("Written to destination offset: " + (processable.getDestinationOffset() + progress) +
                        " bytes: " + bytes);
            } catch (IOHandlerException e) {
                log.error("Error during writing, destination: " + processable.toString());
            }
            progress += bytes;

            log.debug("Changing the statistic record");
            holder.add(Thread.currentThread().getId(), new Pair<>(size, progress));
        }

        log.info("finished processing" + processable.toString());
        return true;
    }
}
