package com.sysgears.processor.io;

import java.io.RandomAccessFile;

public enum FileSystemHandler implements IOHandler {
    SPLITTER {
        @Override
        public int write(byte[] buffer, long position) {
            return 0;
        }

        @Override
        public byte[] read(long position) {
            return new byte[0];
        }
    },
    JOINER {
        @Override
        public int write(byte[] buffer, long position) {
            return 0;
        }

        @Override
        public byte[] read(long position) {

            return new byte[0];
        }
    };

    private RandomAccessFile raf;
    private final byte[] buffer = new byte[2048];

    public void setRaf(RandomAccessFile raf) {
        this.raf = raf;
    }

    private RandomAccessFile getRaf() {
        return raf;
    }
}
