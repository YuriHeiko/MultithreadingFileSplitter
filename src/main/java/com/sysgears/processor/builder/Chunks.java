package com.sysgears.processor.builder;

import java.util.LinkedList;
import java.util.Queue;

public class Chunks {
    private final Queue<Chunk> chunks = new LinkedList<>();
    private String name;
    private String chunkPrefix;
    private int chunkMaxSize;

    private class Chunk {
        private String suffix;
    }
}
