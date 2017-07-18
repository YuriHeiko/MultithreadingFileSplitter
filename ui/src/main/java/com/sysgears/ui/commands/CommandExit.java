package com.sysgears.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;

/**
 * A command to quit program
 */
@Parameters(commandNames = "exit", commandDescription = "Quit program")
public class CommandExit extends Command {
    /**
     * Shows help
     *
     * @param jCommander The {@code JCommander} object
     * @return The string with the command representation
     */
    @Override
    public String execute(final JCommander jCommander) {
        return "exit";
    }
}
