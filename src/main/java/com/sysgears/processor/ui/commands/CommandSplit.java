package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.ui.UIException;
import com.sysgears.processor.statistic.StatisticHolder;
import com.sysgears.processor.service.factories.Factory;
import com.sysgears.processor.service.factories.SplitFactory;
import com.sysgears.processor.ui.Executable;
import com.sysgears.processor.ui.FileProcessor;

@Parameters(commandNames = "split", commandDescription = "Break file into parts")
public class CommandSplit implements Executable {
    @Parameter(names = "-p", required = true, description = "The absolute file path, i.e. '-p C:/DIR/file.example'")
    private String path;

    @Parameter(names = "-s", required = true, description = "Chunks' size NUMBER[kB|MB|GB]")
    private String chunk = "10MB";

    @Parameter(names = "-t", description = "Number of threads")
    private int threadsNumber = 4;

    private final int startNumber = 1;

    private final String BYTES_STRING = "kB|MB|GB";

    private final String BYTES_REGEX = "(?=(" + BYTES_STRING + "))";


    @Override
    public String execute(JCommander jCommander) {
        long chunkSize = convertSizeToNumber(chunk);
        StatisticHolder holder = new StatisticHolder();

        Factory factory = new SplitFactory(path, FileProcessor.PART_PREFIX, holder, startNumber, chunkSize);
        Thread statisticWatcher = holder.getWatcher();

        statisticWatcher.start();
        startWorkers(factory.createChunks(), threadsNumber);

        try {
            statisticWatcher.join();
        } catch (InterruptedException e) {
            throw new UIException("Statistic's process has been suddenly interrupted.");
        }

        return "done";
    }

    long convertSizeToNumber(final String chunk) {
        long chunkSize;
        String[] split = chunk.split(BYTES_REGEX);

        try {
            chunkSize = Long.valueOf(split[0]);
        } catch (NumberFormatException e) {
            throw new UIException("You've entered the wrong chunk size, it should be a positive number or " +
                    "it can have next format NUMBER" + BYTES_STRING);
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

        if (chunkSize <= 0) {
            throw new UIException("You've entered the wrong chunk size, it must be a positive number greater than 0");
        }

        return chunkSize;
    }
}