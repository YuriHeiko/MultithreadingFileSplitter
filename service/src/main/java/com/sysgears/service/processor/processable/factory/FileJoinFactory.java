package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;
import org.apache.log4j.Logger;

import java.io.RandomAccessFile;

/**
 * The join factory
 */
public class FileJoinFactory implements IProcessableFactory {
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileJoinFactory.class);

    /**
     * Creates {@code IProcessable} objects to join
     *
     * @param source      The {@code RandomAccessFile} source
     * @return The {@code IProcessable} instance
     */
    @Override
    public IProcessable create(RandomAccessFile source, ChunkProperties properties) {
        log.debug("Create and return a new " + FileChunk.class + " object");
        return new FileChunk(properties.getFile(), source, properties.getSize(), 0, properties.getPointer());
    }
}
