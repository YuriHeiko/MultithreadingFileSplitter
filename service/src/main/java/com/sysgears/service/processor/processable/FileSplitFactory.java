package com.sysgears.service.processor.processable;

import java.io.RandomAccessFile;

public class FileSplitFactory implements IProcessableFactory {
    @Override
    public IProcessable create(RandomAccessFile source, RandomAccessFile destination, long size, long offset) {
        return new FileChunk(source, destination, size, offset, 0);
    }
}
