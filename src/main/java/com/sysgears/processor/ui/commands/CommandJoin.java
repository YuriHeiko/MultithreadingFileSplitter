package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.exceptions.UIException;
import com.sysgears.processor.statistic.StatisticHolder;
import com.sysgears.processor.threads.JoinFactory;
import com.sysgears.processor.threads.Factory;
import com.sysgears.processor.ui.Executable;
import com.sysgears.processor.ui.FileProcessor;

import java.util.Collection;

@Parameters(commandNames = "join", commandDescription = "Join parts into file")
public class CommandJoin implements Executable {
    @Parameter(names = "-p", required = true, description = "The first part absolute file path, i.e. '-p C:/DIR/file.example.part1'")
    private String path;

    @Parameter(names = "-t", description = "Number of threads")
    private int threadsNumber = 4;

    @Override
    public String execute(JCommander jCommander) {

        StatisticHolder holder = new StatisticHolder();

        Factory factory = new JoinFactory(path, FileProcessor.partPrefix, holder, 0);

        Collection<Runnable> workers = factory.createChunks();
        Thread statisticHandler = holder.startWatching();

        startWorkers(workers, threadsNumber);

        try {
            statisticHandler.join();
        } catch (InterruptedException e) {
            throw new UIException("Statistic's process has been suddenly interrupted.");
        }

        return "done";
    }
}