package com.sysgears.processor.ui;

import com.beust.jcommander.ParameterException;
import com.sysgears.processor.exceptions.FileProcessorException;
import com.sysgears.processor.io.IOHandler;

import java.io.*;

public class FileProcessor {
    private InputStream systemIS;
    private OutputStream systemOS;

    public final static int bufferSize = 1024;

    public final static String partPrefix = ".part";

    public FileProcessor() {
    }

    public FileProcessor(InputStream is, OutputStream os) {
        systemIS = System.in;
        systemOS = System.out;

        System.setIn(is);
        System.setOut(new PrintStream(os));
    }

    public void run() {
        String command = "";
        System.out.println("This program can split and join a file using multiple threads" + System.lineSeparator());
        CommandsHandler handler = new CommandsHandler();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (!command.equals("exit")) {
                System.out.println("Type a command or 'help' to see how to use the program:");
                try {
                    command = handler.handle(reader.readLine().split(" "));

                } catch (ParameterException e) {
                    System.out.println(e.getMessage());
                    System.out.println("You've entered the wrong command. Try again or type 'help':");

                } catch (FileProcessorException e) {
                    System.out.println(e.getMessage());
                }

                // Have to create a new instance every time due to a JCommander 'feature'
                // https://github.com/cbeust/jcommander/issues/271
                handler = new CommandsHandler();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (systemIS != null && systemOS != null) {
                System.setIn(systemIS);
                System.setOut(new PrintStream(systemOS));
            }
        }
    }
}
