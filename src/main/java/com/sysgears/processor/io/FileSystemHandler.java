package com.sysgears.processor.io;

import java.io.RandomAccessFile;

public enum FileSystemHandler implements IOHandler {
    SPLITTER {
        @Override
        public void write(final RandomAccessFile raf, final int buffer, final long position, final int chunkNumber) {
        }

        @Override
        public int read(final RandomAccessFile raf, final long position, final int chunkNumber) {
            synchronized (raf) {
                return 0;
            }
        }
    },
    JOINER {
        @Override
        public void write(final RandomAccessFile raf, final int buffer, final long position, final int chunkNumber) {

            synchronized (raf) {
                super.write0(raf, buffer, position, chunkNumber);
            }
        }

        @Override
        public int read(final RandomAccessFile raf, final long position, final int chunkNumber) {
            return 0;
        }
    };

    private void write0(final RandomAccessFile raf, final int buffer, final long position, final int chunkNumber) {
    }

    private int read0(final RandomAccessFile raf, final long position, final int chunkNumber) {
        return 0;
    }
}
