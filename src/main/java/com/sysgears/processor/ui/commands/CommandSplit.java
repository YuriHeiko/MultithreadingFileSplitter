package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "split", commandDescription = "Break file into parts")
public class CommandSplit implements Executor {
    @Parameter(names = "-p", required = true, description = "The file path")
    private String path;

    @Parameter(names = "-s", required = true, description = "Chunks' size (GB - gigabytes, MB - megabytes, kB - kilobytes)")
    private String chunkSize = "10MB";

    @Override
    public boolean execute(JCommander jCommander) {
        System.out.println("split " + "-p " + path + " -s " + chunkSize);

        return true;
    }
}