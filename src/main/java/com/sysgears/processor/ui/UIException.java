package com.sysgears.processor.ui;

/**
 * Thrown to indicate that the UI error is arisen
 */
public class UIException extends FileProcessorException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public UIException(String message) {
        super(message);
    }
}
