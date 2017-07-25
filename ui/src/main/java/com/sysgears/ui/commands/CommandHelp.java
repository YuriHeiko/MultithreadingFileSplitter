package com.sysgears.ui.commands;

import com.beust.jcommander.Parameters;
import com.sysgears.ui.IExecutable;

/**
 * A command to show help
 */
@Parameters(commandNames = "help", commandDescription = "Help")
public class CommandHelp implements IExecutable {
    /**
     * Shows help
     */
    @Override
    public void execute() {
        // do nothing
    }

    @Override
    public String toString() {
        return "CommandHelp{}";
    }
}
