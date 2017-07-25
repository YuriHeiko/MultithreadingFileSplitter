package com.sysgears.service.processor;

import com.sysgears.service.processor.processable.IProcessable;

/**
 * Processes the {@code IProcessable} object
 */
public interface IProcessableProcessor {
    /**
     * Processes the received {@code IProcessable} object
     *
     * @param processable The object to process
     * @return true if everything is ok
     */
    boolean process(IProcessable processable);
}
