package com.sysgears.ui;

import com.beust.jcommander.JCommander;
import com.sysgears.ui.commands.*;

/**
 * Handles User's commands
 */
public class CommandsHandler {
    /**
     * Handles commands received from UI
     *
     * @param args The command line parameters
     */
    public String handle(final String[] args) {
        // Have to create a new instance every time due to a JCommander 'feature'
        // https://github.com/cbeust/jcommander/issues/271
        JCommander jCommander = JCommander.newBuilder().addCommand(new CommandExit()).
                                                        addCommand(new CommandHelp()).
                                                        addCommand(new CommandJoin()).
                                                        addCommand(new CommandSplit()).
                                                        build();

        jCommander.parse(args);
        JCommander parsedJCommander = jCommander.getCommands().get(jCommander.getParsedCommand());
        Executable executable = (Executable) parsedJCommander.getObjects().get(0);
        return executable.execute(jCommander);
    }
}
