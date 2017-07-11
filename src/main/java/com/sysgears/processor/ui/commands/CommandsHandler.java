package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.sysgears.processor.ui.commands.*;

/**
 * Handles User's commands
 */
public class CommandsHandler {

    /**
     * Handles commands received from UI
     *
     * @param args The command line parameters
     */
    public boolean handle(final String[] args) {
        JCommander jCommander = JCommander.newBuilder().
                                addCommand(new CommandExit()).
                                addCommand(new CommandHelp()).
                                addCommand(new CommandJoin()).
                                addCommand(new CommandSplit()).
                                build();

        jCommander.parse(args);
        String parsedCommand = jCommander.getParsedCommand();
        JCommander parsedJCommander = jCommander.getCommands().get(parsedCommand);
        Executor commandObject = (Executor) parsedJCommander.getObjects().get(0);
        return commandObject.execute(jCommander);
    }
}
