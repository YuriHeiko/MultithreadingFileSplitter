package com.sysgears.io;

/**
 * Thrown to indicate that the IO error is arisen
 */
public class IOHandlerException extends RuntimeException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    IOHandlerException(String message) {
        super(message);
    }
}
