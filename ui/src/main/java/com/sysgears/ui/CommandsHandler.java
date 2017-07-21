package com.sysgears.ui;

import com.beust.jcommander.JCommander;
import org.reflections.Reflections;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
     * Constructs an object
     */
    CommandsHandler() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName() + ".commands");

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        JCommander.Builder jBuilder = JCommander.newBuilder();
        getClassNamesFromPackage(this.getClass().getPackage().getName() + ".commands").forEach(jBuilder::addCommand);
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

    void showCommandsUsage() {
        jCommander.usage();
    }
    /**
     * Searches for classes in classpath by the specified package name.
     *
     * @param packageName the name of the package
     * @return {@code List} of all classes
     * @throws UIException if some error
     */
    private static List<IExecutable> getClassNamesFromPackage(final String packageName) {
        List<IExecutable> classes = new ArrayList<>();

        String packName = packageName.replace(".", "/");
        URL packageURL = Thread.currentThread().getContextClassLoader().getResource(packName);

        try {
            for (File file : new File(packageURL.toURI()).listFiles()) {
                String name = file.getName();
                name = name.substring(0, name.lastIndexOf('.'));
                classes.add((IExecutable) Class.forName(packageName + "." + name).newInstance());
            }
        } catch (Exception e) {
            throw new UIException("An error during collecting a list of commands from the classpath: " + e.getMessage());
        }

        return classes;
    }
}
