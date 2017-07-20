package com.sysgears.statistic;

/**
 * Thrown to indicate that the error is happened on
 * the statistic layer
 */
public class StatisticException extends RuntimeException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public StatisticException(String message) {
        super(message);
    }
}
