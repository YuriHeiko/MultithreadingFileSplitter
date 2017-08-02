package com.sysgears.service.processor;

import com.sysgears.io.IIO;
import com.sysgears.io.IOHandlerException;
import com.sysgears.service.ServiceException;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.statistic.IHolder;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Processes the {@code IProcessable} object
 */
public class IOProcessor implements IProcessableProcessor {
    /**
     * The {@code IOHandler} instance
     */
    private final IIO io;
    /**
     * The {@code IHolder} instance
     */
    private final IHolder<Long, Pair<Long, Long>> holder;
    /**
     * The buffer size
     */
    private final int bufferSize;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(IOProcessor.class);

    /**
     * Creates an object
     *
     * @param io         The {@code IOHandler} instance
     * @param holder     The {@code IHolder} instance
     * @param bufferSize The buffer size
     */
    public IOProcessor(final IIO io, final IHolder<Long, Pair<Long, Long>> holder, final int bufferSize) {
        this.io = io;
        this.holder = holder;
        this.bufferSize = bufferSize;
        log.debug("a new object initialized. bufferSize: " + bufferSize);
    }

    /**
     * Processes the received {@code IProcessable} object
     *
     * @param processable The object to process
     * @return true if everything is ok
     */
    @Override
    public boolean process(final IProcessable processable) {
        log.debug("Trying to open the source and destination files");
        try (final RandomAccessFile source = new RandomAccessFile(processable.getSource(), "r");
             final RandomAccessFile destination = new RandomAccessFile(processable.getDestination(), "rw")) {

            final long size = processable.getSize();
            final byte[] buffer = new byte[bufferSize > size ? (int) size : bufferSize];
            final long sourceOffset = processable.getSourceOffset();
            final long destinationOffset = processable.getDestinationOffset();
            long progress = 0;

            while (progress < size) {
                int bytes;
                try {
                    bytes = io.read(source, buffer, sourceOffset + progress);
                    log.debug("Read from source offset: " + (sourceOffset + progress) + " bytes: " + bytes);
                } catch (IOHandlerException e) {
                    log.error("IO error during reading, source: " + processable.toString());
                    throw new ServiceException("IO error during reading, source: " + processable.toString());
                }
                try {
                    io.write(destination, buffer, destinationOffset + progress, bytes);
                    log.debug("Written to destination offset: " + (destinationOffset + progress) + " bytes: " + bytes);
                } catch (IOHandlerException e) {
                    log.error("Error during writing, destination: " + processable.toString());
                    throw new ServiceException("IO error during writing, destination: " + processable.toString());
                }
                progress += bytes;

                log.debug("Changing the statistic record");
                holder.add(Thread.currentThread().getId(), new Pair<>(size, progress));
            }
        } catch (IOException e) {
            log.error("IO error during opening the destination file: " + processable.getDestination());
            throw new ServiceException("IO error during opening the source or destination files. Source: " +
                                        processable.getSource() + " | Destination: " + processable.getDestination());
        }

        log.info("finished processing" + processable.toString());

        return true;
    }
}
