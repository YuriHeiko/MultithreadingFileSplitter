package com.sysgears.processor.io;

import com.sysgears.processor.ui.FileProcessorException;

/**
 * Thrown to indicate that the IO error is arisen
 */
public class IOHandlerException extends FileProcessorException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public IOHandlerException(String message) {
        super(message);
    }
}
