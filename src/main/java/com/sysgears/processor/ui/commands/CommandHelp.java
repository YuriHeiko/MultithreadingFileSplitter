package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;

/**
 * A command to show help
 */
@Parameters(commandNames = "help", commandDescription = "Help")
public class CommandHelp extends Command {
    /**
     * Shows help
     *
     * @param jCommander The {@code JCommander} object
     *
     * @return The string with the command representation
     */
    @Override
    public String execute(JCommander jCommander) {
        jCommander.usage();
        return "help";
    }
}
