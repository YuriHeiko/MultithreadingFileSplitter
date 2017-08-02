package com.sysgears.ui.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.sysgears.io.IOHandler;
import com.sysgears.io.SyncWriteIO;
import com.sysgears.service.FileWorkersFactory;
import com.sysgears.service.processor.IOProcessor;
import com.sysgears.service.processor.IProcessableProcessor;
import com.sysgears.service.processor.processable.ChunkProperties;
import com.sysgears.service.processor.processable.FileChunksSet;
import com.sysgears.service.processor.processable.factory.FileJoinFactory;
import com.sysgears.service.processor.processable.factory.IProcessableFactory;
import com.sysgears.statistic.AbstractRecordsHolder;
import com.sysgears.statistic.ConcurrentRecordsHolder;
import com.sysgears.statistic.Viewer;
import com.sysgears.ui.FileProcessor;
import com.sysgears.ui.IExecutable;
import com.sysgears.ui.ServiceRunner;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;

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
     * The IO read/write buffer size
     */
    @Parameter(names = "-b", description = "IO buffer size(bytes) (The max size: " + FileProcessor.MAX_BUFFER_SIZE + ")")
    private int bufferSize = FileProcessor.BUFFER_SIZE;
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(CommandJoin.class);

    /**
     * Constructs an object
     */
    public CommandJoin() {
    }

    /**
     * Constructs an object
     *
     * @param path           The path to the file
     * @param threadsNumber  The number of threads
     * @param delay          The statistic output delay
     * @param partPrefix     The part prefix name
     * @param bufferSize     The IO read/write buffer size
     */
    public CommandJoin(String path, int threadsNumber, int delay, String partPrefix, int bufferSize) {
        this.path = path;
        this.threadsNumber = threadsNumber;
        this.delay = delay;
        this.partPrefix = partPrefix;
        this.bufferSize = bufferSize;
    }

    /**
     * Joins parts of a file into a big one
     */
    @Override
    public void execute() {
        if (bufferSize > FileProcessor.MAX_BUFFER_SIZE) {
            bufferSize = FileProcessor.MAX_BUFFER_SIZE;
            log.info("The buffer size was greater than allowed so that it was reduced to " + bufferSize);
        }

        log.info("Starting a join command execution");
        log.info("File: " + path);
        final long chunkSize = new File(path).length();
        log.info("Chunk size: " + chunkSize);
        final int firstPartNumber = getFirstPartNumber(path);
        log.info("The first part number: " + firstPartNumber);
        final String joinedFile = getJoinedFileName(path);
        log.info("The joined file name: " + joinedFile);
        final long fileSize = countFinalFileSize(joinedFile, firstPartNumber);
        log.info("The joined file size: " + fileSize);

        log.info("Creating the IO handler: " + SyncWriteIO.class.getSimpleName() + " object");
        final IOHandler syncWriteIO = new SyncWriteIO();

        log.info("Creating " + IProcessableFactory.class.getSimpleName() + " object");
        IProcessableFactory processableFactory = new FileJoinFactory();

        log.info("Creating " + FileChunksSet.class.getSimpleName() + " object");
        final Iterable<ChunkProperties> chunks = new FileChunksSet(fileSize, chunkSize, firstPartNumber, joinedFile, partPrefix);

        log.info("Creating the statistic holder: " + ConcurrentRecordsHolder.class.getSimpleName() + " object");
        final AbstractRecordsHolder<Long, Pair<Long, Long>> holder = new ConcurrentRecordsHolder<>();

        log.info("Creating the statistic viewer: " + Viewer.class.getSimpleName() + " object");
        final Viewer<Long, Pair<Long, Long>> viewer = new Viewer<>(holder, fileSize, delay);

        log.info("Creating the IO processor: " + IOProcessor.class.getSimpleName() + " object");
        final IProcessableProcessor processor = new IOProcessor(syncWriteIO, holder, bufferSize);

        log.info("Creating the workers factory" + FileWorkersFactory.class.getSimpleName() + " object");
        final FileWorkersFactory wFactory = new FileWorkersFactory(processor, chunks, processableFactory, joinedFile);

        log.info("Creating the execution service: " + ServiceRunner.class.getSimpleName() + " object");
        final ServiceRunner serviceRunner = new ServiceRunner(wFactory, viewer, threadsNumber);

        log.info("Running a service");
        serviceRunner.run();
    }

    /**
     * Calculates the size of the joined file
     *
     * @param joinedFileName The parent file name
     * @return The size of the joined file
     */
    long countFinalFileSize(final String joinedFileName, final int firstPartNumber) {
        long size = 0;
        int number = firstPartNumber;

        File file;
        while ((file = new File(joinedFileName + partPrefix + number++)).exists() && file.isFile()) {
            size += file.length();
        }

        return size;
    }

    /**
     * Gets the name of the joined file
     *
     * @param fileName The first part name
     * @return The joined file name
     */
    String getJoinedFileName(final String fileName) {
        final String name;

        try {
            name = fileName.substring(0, fileName.lastIndexOf(partPrefix));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ParameterException("You've entered the wrong file name");
        }

        return name;
    }

    /**
     * Gets the start number of the first part
     *
     * @param fileName he first part name
     * @return The start number
     */
    int getFirstPartNumber(final String fileName) {
        int number;

        try {
            number = Integer.parseInt(fileName.substring(fileName.lastIndexOf(partPrefix) + partPrefix.length()));
        } catch (NumberFormatException e) {
            throw new ParameterException("You've entered the wrong file name");
        }

        return number;
    }

    /**
     * Builds a string representation of this
     *
     * @return The string representation of this
     */
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