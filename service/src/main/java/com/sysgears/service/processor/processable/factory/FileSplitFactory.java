package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

import java.io.RandomAccessFile;

/**
 * The split factory
 */
public class FileSplitFactory implements IProcessableFactory {
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileSplitFactory.class);
    /**
     * Creates {@code IProcessable} objects to split
     *
     * @param source      The {@code RandomAccessFile} source
     * @return The {@code IProcessable} instance
     */
    @Override
    public IProcessable create(RandomAccessFile source, ChunkProperties properties) {
        log.debug("Create and return a new " + FileChunk.class + " object");
        return new FileChunk(source, properties.getFile(), properties.getSize(), properties.getPointer(), 0);
    }
}
