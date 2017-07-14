package com.sysgears.processor.io;

import com.sysgears.processor.exceptions.IOHandlerException;

import java.io.IOException;
import java.io.RandomAccessFile;

public enum FileSystemHandler implements IOHandler {
    SPLITTER {
        @Override
        public void write(final RandomAccessFile raf, final byte[] buffer, final long position, final int length) {
            super.write0(raf, buffer, position, length);
        }

        @Override
        public int read(final RandomAccessFile raf, byte[] buffer, final long position) {
            synchronized (raf) {
                return super.read0(raf, buffer, position);
            }
        }
    },
    JOINER {
        @Override
        public void write(final RandomAccessFile raf, final byte[] buffer, final long position, final int length) {

            synchronized (raf) {
                super.write0(raf, buffer, position, length);
            }
        }

        @Override
        public int read(final RandomAccessFile raf, byte[] buffer, final long position) {
            return super.read0(raf, buffer, position);
        }
    };


    private void write0(final RandomAccessFile raf, final byte[] buffer, final long position, final int length) {
        try {
            raf.seek(position);
            raf.write(buffer, 0, length);
        } catch (IOException e) {
            throw new IOHandlerException("IO error during writing");
        }
    }

    private int read0(final RandomAccessFile raf, byte[] buffer, final long position) {
        int read;
        try {
            raf.seek(position);
            read = raf.read(buffer);
//            System.out.println(read + " - " + buffer.length);
        } catch (IOException e) {
            throw new IOHandlerException("IO error during writing");
        }

        return read;
    }
}
