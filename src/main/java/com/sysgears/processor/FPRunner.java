package com.sysgears.processor;

import com.sysgears.processor.io.FileSystemHandler;
import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.ui.FileProcessor;

public class FPRunner {
    public static void main(String[] args) {
        IOHandler io = FileSystemHandler.SPLITTER;
        new FileProcessor(io).run();
    }
}
