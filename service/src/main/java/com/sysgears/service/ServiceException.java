package com.sysgears.service;

/**
 * Thrown to indicate that the error is happened on the
 * service layer
 */
public class ServiceException extends RuntimeException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public ServiceException(String message) {
        super(message);
    }
}
