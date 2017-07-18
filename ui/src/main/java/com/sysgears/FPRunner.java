package com.sysgears;


import com.sysgears.ui.FileProcessor;

/**
 * Runs a console file splitter
 */
public class FPRunner {
    /**
     * Runs a console file splitter
     *
     * @param args The command line parameters
     */
    public static void main(String[] args) {
        new FileProcessor().run();
    }
}