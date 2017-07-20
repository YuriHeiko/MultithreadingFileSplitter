package com.sysgears.service.processor.processable;

import java.io.RandomAccessFile;

public class FileChunk implements IProcessable {
    private final RandomAccessFile source;
    private final RandomAccessFile destination;
    private final long size;
    private final long sourceOffset;
    private final long destinationOffset;

    public FileChunk(final RandomAccessFile source,
                     final RandomAccessFile destination,
                     final long size,
                     final long sourceOffset,
                     final long destinationOffset) {
        this.source = source;
        this.destination = destination;
        this.size = size;
        this.sourceOffset = sourceOffset;
        this.destinationOffset = destinationOffset;
    }

    @Override
    public long getSourceOffset() {
        return sourceOffset;
    }

    @Override
    public long getDestinationOffset() {
        return destinationOffset;
    }

    @Override
    public RandomAccessFile getSource() {
        return source;
    }

    @Override
    public RandomAccessFile getDestination() {
        return destination;
    }

    @Override
    public long getSize() {
        return size;
    }
}
