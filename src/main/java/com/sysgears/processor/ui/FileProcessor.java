package com.sysgears.processor.ui;

import com.beust.jcommander.ParameterException;
import com.sysgears.processor.exceptions.FileProcessorException;
import com.sysgears.processor.ui.commands.CommandsHandler;

import java.io.*;

public class FileProcessor {
    private InputStream systemIS;
    private OutputStream systemOS;

    public FileProcessor() {
    }

    public FileProcessor(InputStream is, OutputStream os) {
        systemIS = System.in;
        systemOS = System.out;

        System.setIn(is);
        System.setOut(new PrintStream(os));
    }

    public void run() {
        System.out.println("This program can split and join a file using multiple threads" + System.lineSeparator());
        CommandsHandler handler = new CommandsHandler();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Type a command or 'help' to see how to use the program:");
                try {
                    if (!handler.handle(reader.readLine().split(" "))) {
                        break;
                    }

                } catch (ParameterException e) {
                    e.usage();
                    System.out.println("You've entered the wrong command. Try again or type 'help':");

                } catch (FileProcessorException e) {
                    System.out.println(e.getMessage());
                }
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
