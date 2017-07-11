package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "join", commandDescription = "Join parts into file")
public class CommandJoin implements Executor {
    @Parameter(names = "-p", required = true, description = "The first part file path")
    private String path;

    @Override
    public boolean execute(JCommander jCommander) {
        System.out.println("join " + "-p " + path);
        return true;
    }
}