package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.io.IOHandler;

@Parameters(commandNames = "join", commandDescription = "Join parts into file")
public class CommandJoin implements Executor {
    @Parameter(names = "-p", required = true, description = "The first part absolute file path, i.e. '-p C:/DIR/file.example.part1'")
    private String path;

    @Parameter(names = "-t", description = "Number of threads")
    private int threadsNumber = 4;

    private IOHandler io;

    public CommandJoin(IOHandler io) {
        this.io = io;
    }

    @Override
    public String execute(JCommander jCommander) {
        System.out.println("join " + "-p " + path);
        return "join";
    }
}