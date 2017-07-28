package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import org.apache.log4j.Logger;

import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Creates {@code FileWorker} objects
 */
public class FileWorkersFactory {
    /**
     * The {@code IProcessableProcessor} instance
     */
    private final IProcessableProcessor processor;
    /**
     * The {@code IProcessable} instance
     */
    private final Iterable<ChunkProperties> chunksProperties;
    /**
     * The {@link IProcessableFactory} instance
     */
    private final IProcessableFactory processableFactory;
    /**
     * The {@code RandomAccessFile} object of the source file
     */
    private final RandomAccessFile source;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileWorkersFactory.class);

    /**
     * Creates an object
     *
     * @param processor          The {@code IProcessableProcessor} instance
     * @param chunksProperties   The {@code IProcessable} instance
     * @param processableFactory The {@code IProcessableFactory} instance
     * @param source             The {@code RandomAccessFile} object of the source file
     */
    public FileWorkersFactory(final IProcessableProcessor processor,
                              final Iterable<ChunkProperties> chunksProperties,
                              final IProcessableFactory processableFactory,
                              final RandomAccessFile source) {
        this.processor = processor;
        this.chunksProperties = chunksProperties;
        this.processableFactory = processableFactory;
        this.source = source;

        log.debug("a new object initialized.");
    }

    /**
     * Creates {@code FileWorkers} collection
     *
     * @return The {@code FileWorkers} collection
     */
    public Collection<Callable<String>> create() {
        return StreamSupport.stream(chunksProperties.spliterator(), false).
                                map(properties -> processableFactory.create(source, properties)).
                                    map(chunk -> new FileWorker(chunk, processor)).
                                        collect(Collectors.toList());
    }
}