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
    private final Iterator<FilePointerIterator.Trinity> pointerIterator;
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
     * Logger
     */
    private static Logger log = Logger.getLogger(FileWorkersFactory.class);

    /**
     * Creates an object
     *
     * @param processor          The {@code IProcessableProcessor} instance
     * @param pointerIterator      The {@code IProcessable} instance
     * @param processableFactory The {@code IProcessableFactory} instance
     * @param fileName           The name of the file
     * @param partPrefix         The part prefix
     * @param source             The {@code RandomAccessFile} object of the source file
     */
    public FileWorkersFactory(final IProcessableProcessor processor,
                              final Iterator<FilePointerIterator.Trinity> pointerIterator,
                              final IProcessableFactory processableFactory,
                              final String fileName,
                              final String partPrefix,
                              final RandomAccessFile source) {
        this(processor, pointerIterator, processableFactory, fileName,
                partPrefix, source, FileSystems.getDefault());
    }
    /**
     * Creates an object
     *
     * @param processor          The {@code IProcessableProcessor} instance
     * @param pointerIterator      The {@code IProcessable} instance
     * @param processableFactory The {@code IProcessableFactory} instance
     * @param fileName           The name of the file
     * @param partPrefix         The part prefix
     * @param source             The {@code RandomAccessFile} object of the source file
     */
    public FileWorkersFactory(final IProcessableProcessor processor,
                              final Iterator<FilePointerIterator.Trinity> pointerIterator,
                              final IProcessableFactory processableFactory,
                              final String fileName,
                              final String partPrefix,
                              final RandomAccessFile source,
                              final FileSystem fileSystem) {
        this.processor = processor;
        this.pointerIterator = pointerIterator;
        this.processableFactory = processableFactory;
        this.fileName = fileName;
        this.partPrefix = partPrefix;
        this.source = source;
        this.fileSystem = fileSystem;

        log.debug("a new object initialized: fileName: " + fileName + " | partPrefix: " + partPrefix);
    }

    /**
     * Creates {@code FileWorkers} collection
     *
     * @return The {@code FileWorkers} collection
     */
    public Collection<Callable<String>> create() {
        Collection<Callable<String>> workers = new ArrayList<>();

        while (pointerIterator.hasNext()) {
            FilePointerIterator.Trinity next = pointerIterator.next();

            RandomAccessFile destination;
            try {
                log.debug("Trying to create the next file.");
                File file = new File(fileSystem.getPath(fileName + partPrefix + next.getChunkNumber()).toUri());
                destination = new RandomAccessFile(file, "rw");
            } catch (FileNotFoundException e) {
                log.error(fileName + partPrefix + next.getChunkNumber() + " wrong file name.");
                throw new ServiceException(fileName + partPrefix + next.getChunkNumber() + " wrong file name.");
            }
            log.info("Creating the next file chunk.");
            IProcessable nextChunk = processableFactory.create(source, destination, next.getSize(), next.getPointer());

            log.info("Creating the new file worker.");
            workers.add(new FileWorker(nextChunk, processor));
        }

        log.debug("Returning the workers collection.");
        return workers;
    }
}