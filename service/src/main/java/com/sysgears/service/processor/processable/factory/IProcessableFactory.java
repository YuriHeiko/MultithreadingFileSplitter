package com.sysgears.service.processor.processable.factory;

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
     * @param destination The {@code RandomAccessFile} destination
     * @param size        The size of the file
     * @param offset      The offset of the file
     * @return The {@code IProcessable} instance
     */
    IProcessable create(RandomAccessFile source, RandomAccessFile destination, long size, long offset);
}
