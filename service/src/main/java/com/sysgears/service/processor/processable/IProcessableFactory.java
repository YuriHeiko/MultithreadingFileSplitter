package com.sysgears.service.processor.processable;

import java.io.RandomAccessFile;

public interface IProcessableFactory {
    IProcessable create(RandomAccessFile source, RandomAccessFile destination, long size, long offset);
}
