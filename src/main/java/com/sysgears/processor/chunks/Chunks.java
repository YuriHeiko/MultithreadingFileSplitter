package com.sysgears.processor.chunks;

import java.util.LinkedList;
import java.util.List;

public class Chunks {
    private final List<Chunk> chunks = new LinkedList<>();
    final private String name;
    final private String chunkPrefix;
    final private long chunkSize;

    private int chunksNumber;

    public Chunks(String name, String chunkPrefix, long chunkSize) {
        this.name = name;
        this.chunkPrefix = chunkPrefix;
        this.chunkSize = chunkSize;
    }

    public int getChunksNumber() {
        return chunksNumber;
    }

    private class Chunk {
        final private String number;
        final private long pointer;
        final private long size;

        public Chunk(String number, long pointer, long size) {
            this.number = number;
            this.pointer = pointer;
            this.size = size;
        }

        public String getNumber() {
            return number;
        }

        public long getPointer() {
            return pointer;
        }

        public long getSize() {
            return size;
        }
    }
}
