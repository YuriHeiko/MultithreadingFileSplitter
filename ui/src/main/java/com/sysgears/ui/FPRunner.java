package com.sysgears.ui;


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
        // split -p /home/yuri/Documents/test/idea.tar.gz -s 10MB -d 100 -t
        // join -p /home/yuri/Documents/test/idea.tar.gz.part1 -d 100 -t
        // split -p d.zip -s 100MB -d 1000
        // join -p d.zip.part1 -d 1000
        // split -p r.md -s 400
        // join -p r.md.part1
        // split -p /home/yuri/Documents/test/r.md -s 100 -d 1000
        // join -p /home/yuri/Documents/test/r.md.part1
        // split -p /home/yuri/Documents/test/a.txt -s 100 -d 1000
        // join -p /home/yuri/Documents/test/a.txt.part1
        new FileProcessor().run();
    }
}