package com.sysgears.ui.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sysgears.io.IOHandler;
import com.sysgears.io.SyncWriteIO;
import com.sysgears.service.FileWorkerFactory;
import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.service.processor.splittable.FileJoiner;
import com.sysgears.statistic.ConcurrentRecordsHolder;
import com.sysgears.statistic.IHolder;
import com.sysgears.statistic.StatisticHolder;
import com.sysgears.statistic.Watcher;
import com.sysgears.ui.FileProcessor;
import com.sysgears.ui.IExecutable;
import com.sysgears.ui.ServiceRunner;
import javafx.util.Pair;

import java.io.File;
import java.util.Iterator;

/**
 * A command to glue a parts into one file
 */
@Parameters(commandNames = "join", commandDescription = "Join parts into file")
public class CommandJoin implements IExecutable {
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
     */
    @Override
    public void execute() {
        final long chunkSize = new File(path).length();
        final long fileSize = countFinalFileSize(path);
        final IOHandler syncWriteIO = new SyncWriteIO();
        final Iterator<IProcessable> fileJoiner = new FileJoiner(fileSize, path, chunkSize, partPrefix, 0);
        final StatisticHolder<Long, Pair<Long, Long>> holder = new ConcurrentRecordsHolder<>();
        final Watcher<Long, Pair<Long, Long>> watcher = new Watcher<>(holder, fileSize, delay);
        final IProcessableProcessor processor = new IOProcessor(syncWriteIO, holder, bufferSize);
        final FileWorkerFactory fileWorkerFactory = new FileWorkerFactory(fileJoiner, processor);
        final ServiceRunner serviceRunner = new ServiceRunner(fileWorkerFactory, watcher, threadsNumber);

        serviceRunner.run();
    }

    long countFinalFileSize(final String fileName) {
        int ind = fileName.lastIndexOf(partPrefix) + partPrefix.length();
        final String name = fileName.substring(0, ind);
        int number = Integer.parseInt(fileName.substring(ind));
        long size = 0;

        File file;
        while ((file = new File(name + number++)).exists() && file.isFile()) {
            size += file.length();
        }

        return size;
    }
}