package com.sysgears.ui.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.sysgears.io.IOHandler;
import com.sysgears.io.SyncReadIO;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunksSet;
import com.sysgears.service.FileWorkersFactory;
import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.factory.FileSplitFactory;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import com.sysgears.statistic.AbstractRecordsHolder;
import com.sysgears.statistic.ConcurrentRecordsHolder;
import com.sysgears.statistic.Watcher;
import com.sysgears.ui.FileProcessor;
import com.sysgears.ui.IExecutable;
import com.sysgears.ui.ServiceRunner;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * A command to split a file into chunks
 */
@Parameters(commandNames = "split", commandDescription = "Break file into parts")
public class CommandSplit implements IExecutable {
    /**
     * The path to the file
     */
    @Parameter(names = "-p", required = true, description = "The absolute file path, i.e. '-p C:/DIR/file.example'")
    private String path;
    /**
     * The size of a chunk
     */
    @Parameter(names = "-s", required = true, description = "Chunks' size NUMBER[kB|MB|GB]")
    private String chunkSize = FileProcessor.CHUNK_SIZE;
    /**
     * The number of threads
     */
    @Parameter(names = "-t", description = "Number of threads")
    private int threadsNumber = FileProcessor.THREADS_NUMBER;
    /**
     * The first part number
     */
    @Parameter(names = "-f", description = "First part number")
    private int startNumber = FileProcessor.START_NUMBER;
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
     * The IO read/write buffer size
     */
    @Parameter(names = "-b", description = "IO buffer size(bytes)")
    private int bufferSize = FileProcessor.BUFFER_SIZE;
    /**
     * The string representation of byte contractions
     */
    private final String BYTES_STRING = "kB|MB|GB";
    /**
     * The regex of byte contractions
     */
    private final String BYTES_REGEX = "(?=(" + BYTES_STRING + "))";
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(CommandSplit.class);
    /**
     * The {@link FileSystem} instance
     */
    private final FileSystem fileSystem;

    /**
     * Constructs an object with the default file system
     */
    public CommandSplit() {
        fileSystem = FileSystems.getDefault();
    }

    /**
     * Constructs an object with the given file system
     *
     * @param fileSystem The {@code FileSystem}
     */
    public CommandSplit(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Split a file into chunks
     */
    @Override
    public void execute() {
        final long chunkSizeNumber = convertToNumber(chunkSize);

        log.info("Starting a split command execution");
        final long fileSize = new File(path).length();
        log.info("File: " + path + "The size of the file: " + fileSize);

        if (chunkSizeNumber > fileSize) {
            log.debug("The wrong chunk size! Size can't exceed the size of the file");
            throw new ParameterException("The wrong chunk size! It can't exceed the size of the file. Chunk: " +
                                         chunkSizeNumber + "bytes > " + fileSize + "bytes");
        }

        log.info("Creating the IO handler: " + SyncReadIO.class.getSimpleName() + " object");
        final IOHandler syncReadIO = new SyncReadIO();

        log.debug("Trying to create a RandomAccessFile, file name: " + path);
        RandomAccessFile source;
        try {
            source = new RandomAccessFile(Files.createFile(fileSystem.getPath(path)).toFile(), "rw");
        } catch (IOException e) {
            throw new ParameterException(path + " doesn't exist.");
        }

        log.info("Creating " + IProcessableFactory.class.getSimpleName() + " object");
        IProcessableFactory processableFactory = new FileSplitFactory();

        log.info("Creating " + FileChunksSet.class.getSimpleName() + " object");
        final Iterable<ChunkProperties> fileSplitter = new FileChunksSet(fileSize,
                                                                        chunkSizeNumber,
                                                                        startNumber,
                                                                        path,
                                                                        partPrefix);

        log.info("Creating the statistic holder: " + ConcurrentRecordsHolder.class.getSimpleName() + " object");
        final AbstractRecordsHolder<Long, Pair<Long, Long>> holder = new ConcurrentRecordsHolder<>();

        log.info("Creating the statistic watcher: " + Watcher.class.getSimpleName() + " object");
        final Watcher<Long, Pair<Long, Long>> watcher = new Watcher<>(holder, fileSize, delay);

        log.info("Creating the IO processor: " + IOProcessor.class.getSimpleName() + " object");
        final IProcessableProcessor processor = new IOProcessor(syncReadIO, holder, bufferSize);

        log.info("Creating the workers factory" + FileWorkersFactory.class.getSimpleName() + " object");
        final FileWorkersFactory wFactory = new FileWorkersFactory(processor, fileSplitter, processableFactory, source);

        log.info("Creating the execution service: " + ServiceRunner.class.getSimpleName() + " object");
        final ServiceRunner serviceRunner = new ServiceRunner(wFactory, watcher, threadsNumber);

        log.info("Running a service");
        serviceRunner.run();
    }

    /**
     * Converts a string with byte constraints into a number
     *
     * @param stringSize The string with byte constraints
     * @return The number
     */
    private long convertToNumber(final String stringSize) {
        long chunkSize;

        log.debug("Trying to convert size from string to number format");
        String[] split = stringSize.split(BYTES_REGEX);

        try {
            chunkSize = Long.valueOf(split[0]);
        } catch (NumberFormatException e) {
            throw new ParameterException("You've entered the wrong chunk size, it should be a positive number or " +
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
            throw new ParameterException("You've entered the wrong chunk size, it must be a positive number greater " +
                                         "than 0");
        }

        return chunkSize;
    }

    /**
     * Builds a string representation of this
     *
     * @return The string representation of this
     */
    @Override
    public String toString() {
        return "CommandSplit{" +
                "path='" + path + '\'' +
                ", chunkSize='" + chunkSize + '\'' +
                ", threadsNumber=" + threadsNumber +
                ", startNumber=" + startNumber +
                ", delay=" + delay +
                ", partPrefix='" + partPrefix + '\'' +
                ", bufferSize=" + bufferSize + '}';
    }
}