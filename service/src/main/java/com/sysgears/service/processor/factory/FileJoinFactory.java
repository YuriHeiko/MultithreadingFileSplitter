package com.sysgears.service.processor.factory;

import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;

import java.io.RandomAccessFile;

/**
 * The join factory
 */
public class FileJoinFactory implements IProcessableFactory {
    /**
     * Creates {@code IProcessable} objects to join
     *
     * @param source      The {@code RandomAccessFile} source
     * @param destination The {@code RandomAccessFile} destination
     * @param size        The size of the file
     * @param offset      The offset of the file
     * @return The {@code IProcessable} instance
     */
    @Override
    public IProcessable create(RandomAccessFile source, RandomAccessFile destination, long size, long offset) {
        return new FileChunk(destination, source, size, 0, offset);
    }
}
