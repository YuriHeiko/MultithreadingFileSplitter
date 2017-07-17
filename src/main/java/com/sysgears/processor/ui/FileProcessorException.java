package com.sysgears.processor.ui;

/**
 * Thrown to indicate that the some error is arisen in
 * the FileProcessor app.
 */
public class FileProcessorException extends RuntimeException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public FileProcessorException(String message) {
        super(message);
    }
}
