package com.sysgears.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;

/**
 * A command to show help
 */
@Parameters(commandNames = "help", commandDescription = "Help")
public class CommandHelp implements IExecutable {
    /**
     * Shows help
     *
     * @param jCommander The {@code JCommander} object
     *
     * @return The string with the command representation
     */
    @Override
    public String execute(final JCommander jCommander) {
        jCommander.usage();
        return "help";
    }
}
