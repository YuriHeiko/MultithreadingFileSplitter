package com.sysgears.processor.ui.commands;

import com.beust.jcommander.JCommander;

public interface Executor {
    boolean execute(JCommander jCommander);
}
