package com.sysgears.processor.ui;

import com.beust.jcommander.ParameterException;
import com.sysgears.processor.exceptions.FileProcessorException;
import com.sysgears.processor.io.IOHandler;
import com.sysgears.processor.ui.commands.CommandsHandler;

import java.io.*;

public class FileProcessor {
    private InputStream systemIS;
    private OutputStream systemOS;
    private IOHandler io;

    public FileProcessor(IOHandler io) {
        this.io = io;
    }

    public FileProcessor(InputStream is, OutputStream os, IOHandler io) {
        systemIS = System.in;
        systemOS = System.out;
        this.io = io;

        System.setIn(is);
        System.setOut(new PrintStream(os));
    }

    public void run() {
        String command = "";
        System.out.println("This program can split and join a file using multiple threads" + System.lineSeparator());
        CommandsHandler handler = new CommandsHandler(io);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (!command.equals("exit")) {
                System.out.println("Type a command or 'help' to see how to use the program:");
                try {
                    command = handler.handle(reader.readLine().split(" "));

                } catch (ParameterException e) {
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
