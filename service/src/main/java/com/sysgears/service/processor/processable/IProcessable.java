package com.sysgears.service.processor.processable;

import java.io.RandomAccessFile;

public interface IProcessable {
    long getSourceOffset();

    long getDestinationOffset();

    RandomAccessFile getSource();

    RandomAccessFile getDestination();

    long getSize();
}
