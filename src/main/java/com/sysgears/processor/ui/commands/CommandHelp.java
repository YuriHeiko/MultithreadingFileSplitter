package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.ui.Executable;

@Parameters(commandNames = "help", commandDescription = "Help")
public class CommandHelp implements Executable {
    @Override
    public String execute(JCommander jCommander) {
        jCommander.usage();
        return "done";
    }
}
