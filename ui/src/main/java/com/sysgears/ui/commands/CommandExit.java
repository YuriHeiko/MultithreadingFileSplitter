package com.sysgears.ui.commands;

import com.beust.jcommander.Parameters;
import com.sysgears.ui.IExecutable;

/**
 * A command to quit program
 */
@Parameters(commandNames = "exit", commandDescription = "Quit program")
public class CommandExit implements IExecutable {
    /**
     * Shows help
     */
    @Override
    public void execute() {
        // do nothing
    }
}
