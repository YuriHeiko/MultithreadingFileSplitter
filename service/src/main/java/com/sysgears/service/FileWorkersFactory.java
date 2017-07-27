package com.sysgears.service;

import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

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
    private final Iterator<Pair<Long, Long>> chunkIterator;
    /**
     * The {@link IProcessableFactory} instance
     */
    private final IProcessableFactory processableFactory;
    /**
     * The name of the file
     */
    private final String fileName;
    /**
     * The part prefix
     */
    private final String partPrefix;
    /**
     * The {@code RandomAccessFile} object of the source file
     */
    private final RandomAccessFile source;
    /**
     * The {@link FileSystem} instance
     */
    private final FileSystem fileSystem;
    /**
     * The next part number
     */
    private int partNumber;
    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(FileWorkersFactory.class);

    /**
     * Creates an object
     *
     * @param processor          The {@code IProcessableProcessor} instance
     * @param chunkIterator      The {@code IProcessable} instance
     * @param processableFactory The {@code IProcessableFactory} instance
     * @param fileName           The name of the file
     * @param partPrefix         The part prefix
     * @param partNumber         The first part number
     * @param source             The {@code RandomAccessFile} object of the source file
     */
    public FileWorkersFactory(IProcessableProcessor processor,
                              Iterator<Pair<Long, Long>> chunkIterator,
                              IProcessableFactory processableFactory,
                              String fileName,
                              String partPrefix,
                              final int partNumber,
                              RandomAccessFile source) {
        this(processor, chunkIterator, processableFactory, fileName,
                partPrefix, partNumber, source, FileSystems.getDefault());
    }
    /**
     * Creates an object
     *
     * @param processor          The {@code IProcessableProcessor} instance
     * @param chunkIterator      The {@code IProcessable} instance
     * @param processableFactory The {@code IProcessableFactory} instance
     * @param fileName           The name of the file
     * @param partPrefix         The part prefix
     * @param partNumber         The first part number
     * @param source             The {@code RandomAccessFile} object of the source file
     */
    public FileWorkersFactory(IProcessableProcessor processor,
                              Iterator<Pair<Long, Long>> chunkIterator,
                              IProcessableFactory processableFactory,
                              String fileName,
                              String partPrefix,
                              final int partNumber,
                              RandomAccessFile source,
                              FileSystem fileSystem) {
        this.processor = processor;
        this.chunkIterator = chunkIterator;
        this.processableFactory = processableFactory;
        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.partNumber = partNumber;
        this.source = source;
        this.fileSystem = fileSystem;

        log.debug("a new object initialized: fileName: " + fileName + " | partPrefix: " + partPrefix +
                  " | firstPartNumber: " + partNumber);
    }

    /**
     * Creates {@code FileWorkers} collection
     *
     * @return The {@code FileWorkers} collection
     */
    public Collection<Callable<String>> create() {
        Collection<Callable<String>> workers = new ArrayList<>();

        while (chunkIterator.hasNext()) {
            Pair<Long, Long> p = chunkIterator.next();

            RandomAccessFile destination;
            try {
                log.debug("Trying to create the next file.");
                File file = new File(fileSystem.getPath(fileName + partPrefix + partNumber++).toUri());
                destination = new RandomAccessFile(file, "rw");
            } catch (FileNotFoundException e) {
                log.error(fileName + partPrefix + partNumber + " wrong file name.");
                throw new ServiceException(fileName + partPrefix + partNumber + " wrong file name.");
            }
            log.info("Creating the next file chunk.");
            IProcessable nextChunk = processableFactory.create(source, destination, p.getKey(), p.getValue());

            log.info("Creating the new file worker.");
            workers.add(new FileWorker(nextChunk, processor));
        }

        log.debug("Returning the workers collection.");
        return workers;
    }
}