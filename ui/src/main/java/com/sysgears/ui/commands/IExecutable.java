package com.sysgears.ui.commands;

import com.beust.jcommander.JCommander;

/**
 * The commands' interface
 */
public interface IExecutable {
    /**
     * The command to execute
     *
     * @param jCommander The {@code JCommander} object
     * @return The string with the command representation
     */
    String execute(final JCommander jCommander);
}
