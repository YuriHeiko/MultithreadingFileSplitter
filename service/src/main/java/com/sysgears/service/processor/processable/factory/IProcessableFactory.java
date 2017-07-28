package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.IProcessable;

/**
 * The {@code IProcessable} factory
 */
public interface IProcessableFactory {
    /**
     * Creates {@code IProcessable} objects
     *
     * @param source The source name
     * @return The {@code IProcessable} instance
     */
    IProcessable create(String source, ChunkProperties properties);
}
