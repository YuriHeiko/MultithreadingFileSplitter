package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.service.factories.Factory;
import com.sysgears.processor.service.factories.JoinFactory;
import com.sysgears.processor.statistic.StatisticHolder;
import com.sysgears.processor.ui.FileProcessor;
import com.sysgears.processor.ui.UIException;

import java.util.Collection;

/**
 * A command to glue a parts into one file
 */
@Parameters(commandNames = "join", commandDescription = "Join parts into file")
public class CommandJoin extends Command {
    /**
     * The path to the first chunk
     */
    @Parameter(names = "-p", required = true, description = "The first part absolute file path, i.e. " +
            "'-p C:/DIR/file.example.part1'")
    private String path;
    /**
     * The number of threads
     */
    @Parameter(names = "-t", description = "Number of threads")
    private int threadsNumber = FileProcessor.THREADS_NUMBER;
    /**
     * The statistic output delay
     */
    @Parameter(names = "-d", description = "Statistic output delay(ms)")
    private int delay = FileProcessor.DELAY;
    /**
     * The part prefix name
     */
    @Parameter(names = "-n", description = "Part prefix name")
    private String partPrefix = FileProcessor.PART_PREFIX;
    /**
     * The IO buffer size
     */
    @Parameter(names = "-b", description = "IO buffer size(bytes)")
    private int bufferSize = FileProcessor.BUFFER_SIZE;

    /**
     * Joins parts of a file into a big one
     *
     * @param jCommander The {@code JCommander} object
     * @return The string with the command representation
     */
    @Override
    public String execute(final JCommander jCommander) {
        StatisticHolder holder = new StatisticHolder(delay);

        startTasks(new JoinFactory(path, partPrefix, holder, bufferSize), holder, threadsNumber);

        return "join";
    }
}