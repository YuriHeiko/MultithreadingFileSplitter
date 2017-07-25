package com.sysgears.ui.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.sysgears.io.IOHandler;
import com.sysgears.io.SyncReadIO;
import com.sysgears.io.SyncWriteIO;
import com.sysgears.service.FileWorkerFactory;
import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.IProcessable;
import com.sysgears.service.processor.splittable.FileJoiner;
import com.sysgears.statistic.ConcurrentRecordsHolder;
import com.sysgears.statistic.AbstractRecordsHolder;
import com.sysgears.statistic.Watcher;
import com.sysgears.ui.FileProcessor;
import com.sysgears.ui.IExecutable;
import com.sysgears.ui.ServiceRunner;
import javafx.util.Pair;
import org.apache.log4j.Logger;

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
     * Logger
     */
    private final static Logger log = Logger.getLogger(CommandJoin.class);

    /**
     * Joins parts of a file into a big one
     */
    @Override
    public void execute() {
        log.info("Starting a join command execution");
        final long chunkSize = new File(path).length();
        final long fileSize = countFinalFileSize(path);
        log.info("File: " + path + "The size of the part: " + chunkSize + " The size of the joined file: " + fileSize);
        log.info("Creating IO handler: " + SyncWriteIO.class.getSimpleName() + " object");
        final IOHandler syncWriteIO = new SyncWriteIO();
        log.info("Creating " + FileJoiner.class.getSimpleName() + " object");
        final Iterator<IProcessable> fileJoiner = new FileJoiner(fileSize, path, chunkSize, partPrefix, 0);
        log.info("Creating statistic holder: " + ConcurrentRecordsHolder.class.getSimpleName() + " object");
        final AbstractRecordsHolder<Long, Pair<Long, Long>> holder = new ConcurrentRecordsHolder<>();
        log.info("Creating statistic watcher: " + Watcher.class.getSimpleName() + " object");
        final Watcher<Long, Pair<Long, Long>> watcher = new Watcher<>(holder, fileSize, delay);
        log.info("Creating processor: " + IOProcessor.class.getSimpleName() + " object");
        final IProcessableProcessor processor = new IOProcessor(syncWriteIO, holder, bufferSize);
        log.info("Creating workers factory" + FileWorkerFactory.class.getSimpleName() + " object");
        final FileWorkerFactory fileWorkerFactory = new FileWorkerFactory(fileJoiner, processor);
        log.info("Creating the execution service: " + ServiceRunner.class.getSimpleName() + " object");
        final ServiceRunner serviceRunner = new ServiceRunner(fileWorkerFactory, watcher, threadsNumber);

        log.info("Running a service");
        serviceRunner.run();
    }

    /**
     * Calculates the size of the joined file
     *
     * @param fileName The first part name
     * @return The size of the joined file
     */
    long countFinalFileSize(final String fileName) {
        long size = 0;
        try {
            int ind = fileName.lastIndexOf(partPrefix) + partPrefix.length();
            final String name = fileName.substring(0, ind);
            int number = Integer.parseInt(fileName.substring(ind));

            File file;
            while ((file = new File(name + number++)).exists() && file.isFile()) {
                size += file.length();
            }

        } catch (StringIndexOutOfBoundsException e) {
            throw new ParameterException("You've entered the wrong file name");
        }

        return size;
    }

    @Override
    public String toString() {
        return "CommandJoin{" +
                "path='" + path + '\'' +
                ", threadsNumber=" + threadsNumber +
                ", delay=" + delay +
                ", partPrefix='" + partPrefix + '\'' +
                ", bufferSize=" + bufferSize +
                '}';
    }
}