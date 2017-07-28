package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.IProcessable;

import java.io.RandomAccessFile;

/**
 * The {@code IProcessable} factory
 */
public interface IProcessableFactory {
    /**
     * Creates {@code IProcessable} objects
     *
     * @param source      The {@code RandomAccessFile} source
     * @return The {@code IProcessable} instance
     */
    IProcessable create(RandomAccessFile source, ChunkProperties properties);
}
