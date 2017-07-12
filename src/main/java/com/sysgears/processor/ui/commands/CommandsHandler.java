package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.sysgears.processor.io.IOHandler;

/**
 * Handles User's commands
 */
public class CommandsHandler {
    private final JCommander jCommander;

    public CommandsHandler(final IOHandler io) {
        jCommander = JCommander.newBuilder().addCommand(new CommandExit()).
                                             addCommand(new CommandHelp()).
                                             addCommand(new CommandJoin(io)).
                                             addCommand(new CommandSplit(io)).
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
        Executor commandObject = (Executor) parsedJCommander.getObjects().get(0);
        return commandObject.execute(jCommander);
    }
}
