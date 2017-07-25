package com.sysgears.service.processor.processable;

import java.io.RandomAccessFile;

public class FileJoinFactory implements IProcessableFactory {
    @Override
    public IProcessable create(RandomAccessFile source, RandomAccessFile destination, long size, long offset) {
        return new FileChunk(destination, source, size, 0, offset);
    }
}
