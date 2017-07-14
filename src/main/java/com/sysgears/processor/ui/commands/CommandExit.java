package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.sysgears.processor.ui.Executable;

@Parameters(commandNames = "exit", commandDescription = "Quit program")
public class CommandExit implements Executable {
    @Override
    public String execute(JCommander jCommander) {
        return "exit";
    }
}
