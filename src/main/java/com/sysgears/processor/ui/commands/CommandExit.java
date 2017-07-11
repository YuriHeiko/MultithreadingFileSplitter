package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "exit", commandDescription = "To exit from program")
public class CommandExit implements Executor {
    @Override
    public boolean execute(JCommander jCommander) {
        return false;
    }
}
