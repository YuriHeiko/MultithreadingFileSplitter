package com.sysgears.ui;

import com.beust.jcommander.JCommander;
import com.sysgears.ui.commands.*;

/**
 * Handles User's commands
 */
public class CommandsHandler {
    /**
     * The {@code JCommander} object
     */
    private final JCommander jCommander;

    public CommandsHandler() {
        jCommander = JCommander.newBuilder().addCommand(new CommandExit()).
                                             addCommand(new CommandHelp()).
                                             addCommand(new CommandJoin()).
                                             addCommand(new CommandSplit()).
                                             build();
    }

    /**
     * Handles commands received from UI
     *
     * @param args The command line parameters
     */
    public String handle(final String[] args) {
        jCommander.parse(args);
        JCommander parsedJCommander = jCommander.getCommands().get(jCommander.getParsedCommand());
        Executable executable = (Executable) parsedJCommander.getObjects().get(0);
        return executable.execute(jCommander);
    }
}
