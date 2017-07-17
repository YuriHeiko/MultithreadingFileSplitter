package com.sysgears.processor.ui;

import com.beust.jcommander.JCommander;
import com.sysgears.processor.ui.commands.*;

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
        Command command = (Command) parsedJCommander.getObjects().get(0);
        return command.execute(jCommander);
    }
}
