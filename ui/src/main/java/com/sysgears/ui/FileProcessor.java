package com.sysgears.ui;

import com.beust.jcommander.ParameterException;
import com.sysgears.io.IOHandlerException;
import com.sysgears.service.ServiceException;
import com.sysgears.statistic.StatisticException;
import com.sysgears.ui.commands.CommandExit;
import com.sysgears.ui.commands.CommandHelp;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Conducts dialogue with a user and performs their commands
 */
public class FileProcessor {
    /**
     * The saved {@code System.in} object
     */
    private InputStream systemIS;
    /**
     * The saved {@code System.out} object
     */
    private OutputStream systemOS;
    /**
     * The default prefix string of a part name
     */
    public final static String PART_PREFIX = ".part";
    /**
     * The default number of threads
     */
    public final static int THREADS_NUMBER = 4;
    /**
     * The default first part number
     */
    public final static int START_NUMBER = 1;
    /**
     * The default statistic output delay
     */
    public final static int DELAY = 1000;
    /**
     * The default IO buffer size
     */
    public final static int BUFFER_SIZE = 1024;
    /**
     * The default size of a chunk
     */
    public final static String CHUNK_SIZE = "10MB";
    /**
     * Logger
     */
    private final static Logger log = Logger.getLogger(FileProcessor.class);

    /**
     * Constructs an object with default console Input and
     * Output streams
     */
    public FileProcessor() {
        log.info("File processor starts, default IO");
    }

    /**
     * Constructs an object with received Input and Output
     * streams
     *
     * @param is The Input stream
     * @param os The output stream
     */
    public FileProcessor(InputStream is, OutputStream os) {
        systemIS = System.in;
        systemOS = System.out;

        System.setIn(is);
        System.setOut(new PrintStream(os));

        log.info("File processor starts, IS: " + is + ", OS: " + os);
    }

    /**
     * Conducts dialogue with a user and performs their commands
     */
    public void run() {
        System.out.println("This program can split and join a file using multiple threads" + System.lineSeparator());
        log.debug("Initialising the commands handler");
        CommandsHandler handler = new CommandsHandler();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Type a command or 'help' to see how to use the program:");
                try {
                    log.info("Waiting for a user command");
                    IExecutable command = handler.encode(reader.readLine().split(" "));
                    log.info("A user has entered next command: " + command);

                    if (command.getClass() == CommandExit.class) {
                        log.info("Quit program");
                        System.exit(0);

                    } else if (command.getClass() == CommandHelp.class) {
                        log.info("Show usage command");
                        handler.showCommandsUsage();

                    } else {
                        log.info("Trying to execute a user command");
                        command.execute();
                    }

                } catch (ParameterException e) {
                    System.out.println(e.getMessage());
                    System.out.println("You've entered the wrong command. Try again or type 'help':");
                    log.info("A wrong command was entered: " + e.getMessage());

                } catch (UIException e) {
                    System.out.println();
                    System.out.println("A program error happened: " + e.getMessage());
                    System.out.println("*******************************************");
                    System.out.println("* The resulting file(s) can be corrupted! *");
                    System.out.println("*******************************************");
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
            log.fatal("An unexpected error happened: " + e.getMessage());

        } finally {
            if (systemIS != null && systemOS != null) {
                log.info("Releasing system streams");
                System.setIn(systemIS);
                System.setOut(new PrintStream(systemOS));
            }
        }
    }
}
