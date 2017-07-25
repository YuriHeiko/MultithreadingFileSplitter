package com.sysgears.service.processor.processable.factory;

import com.sysgears.service.processor.processable.FileChunk;
import com.sysgears.service.processor.processable.IProcessable;

import java.io.RandomAccessFile;

/**
 * The split factory
 */
public class FileSplitFactory implements IProcessableFactory {
    /**
     * Creates {@code IProcessable} objects to split
     *
     * @param source      The {@code RandomAccessFile} source
     * @param destination The {@code RandomAccessFile} destination
     * @param size        The size of the file
     * @param offset      The offset of the file
     * @return The {@code IProcessable} instance
     */
    @Override
    public IProcessable create(RandomAccessFile source, RandomAccessFile destination, long size, long offset) {
        return new FileChunk(source, destination, size, offset, 0);
    }
}
