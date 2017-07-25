package com.sysgears.ui;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
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
     * Logger
     */
    private final static Logger log = Logger.getLogger(CommandsHandler.class);

    /**
     * Constructs an object
     */
    CommandsHandler() {
        log.info("Trying to find all the possible commands");
        Reflections reflections = new Reflections(this.getClass().getPackage().getName() + COMMANDS_PACKAGE);
        Set<Class<? extends IExecutable>> commands = reflections.getSubTypesOf(IExecutable.class);

        log.info("Start building a JCommander instance");
        JCommander.Builder jBuilder = JCommander.newBuilder();
        for (Class<? extends IExecutable> command : commands) {
            try {
                jBuilder.addCommand(command.newInstance());
            } catch (Exception e) {
                log.fatal("An error has occurred during commands adding");
                throw new UIException("An error has occurred during commands adding");
            }
        }
        jCommander = jBuilder.build();
        jCommander.setAllowParameterOverwriting(true);
        log.info("JCommander was successfully built");
    }

    /**
     * Handles commands received from UI
     *
     * @param args The command line parameters
     */
    IExecutable encode(final String[] args) {
        log.info("Parsing a user command");
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
