package com.sysgears.ui;

import com.beust.jcommander.JCommander;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Handles User's commands
 */
class CommandsHandler {
    /**
     * The configured {@link JCommander} object
     */
    private final JCommander jCommander;
    /**
     * The commands' package name
     */
    private final String COMMANDS_PACKAGE = ".commands";

    /**
     * Constructs an object
     */
    CommandsHandler() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName() + COMMANDS_PACKAGE);
        Set<Class<? extends IExecutable>> commands = reflections.getSubTypesOf(IExecutable.class);

        JCommander.Builder jBuilder = JCommander.newBuilder();
        for (Class<? extends IExecutable> command : commands) {
            try {
                jBuilder.addCommand(command.newInstance());
            } catch (Exception e) {
                throw new UIException("An error has occurred during commands adding");
            }
        }
        jCommander = jBuilder.build();
        jCommander.setAllowParameterOverwriting(true);
    }

    /**
     * Handles commands received from UI
     *
     * @param args The command line parameters
     */
    IExecutable encode(final String[] args) {
        jCommander.parse(args);

        JCommander parsedJCommander = jCommander.getCommands().get(jCommander.getParsedCommand());
        return (IExecutable) parsedJCommander.getObjects().get(0);
    }

    /**
     * Shows the usage of {@code Commands}
     */
    void showCommandsUsage() {
        jCommander.usage();
    }
}
