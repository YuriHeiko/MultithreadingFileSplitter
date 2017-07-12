package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.exceptions.FileProcessorException;
import com.sysgears.processor.io.IOHandler;

@Parameters(commandNames = "split", commandDescription = "Break file into parts")
public class CommandSplit implements Executor {
    @Parameter(names = "-p", required = true, description = "The absolute file path, i.e. '-p C:/DIR/file.example'")
    private String path;

    @Parameter(names = "-s", required = true, description = "Chunks' size NUMBER[GB|MB|kB] (2GB is the MAX number)")
    private String chunk = "10MB";

    @Parameter(names = "-t", description = "Number of threads")
    private int threadsNumber = 4;

    private IOHandler io;

    public CommandSplit(IOHandler io) {
        this.io = io;
    }


    @Override
    public String execute(JCommander jCommander) {
        long chunkSize = convertSizeToNumber(chunk);


        return "split";
    }

    long convertSizeToNumber(final String chunk) {
        long chunkSize;
        String[] split = chunk.split("(?=(MB)|(GB)|(kB))");

        try {
            chunkSize = Long.valueOf(split[0]);
        } catch (NumberFormatException e) {
            throw new FileProcessorException("You've entered the wrong chunk size, it should be a positive number or " +
                    "it can have next format NUMBER[GB|MB|kB] (2GB is MAX number)");
        }

        if (split.length > 1) {
            switch (split[1]) {
                case "GB":
                    chunkSize *= 1024;
                case "MB":
                    chunkSize *= 1024;
                case "kB":
                    chunkSize *= 1024;
            }
        }

        if (chunkSize <= 0 || chunkSize > Integer.MAX_VALUE + 1L) {
            throw new FileProcessorException("You've entered the wrong chunk size, it must be a positive number " +
                    "greater than 0 and it cannot exceed 2GB");
        }

        return chunkSize;
    }
}