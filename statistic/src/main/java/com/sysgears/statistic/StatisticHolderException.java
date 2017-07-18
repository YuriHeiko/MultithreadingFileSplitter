package com.sysgears.statistic;

/**
 * Thrown to indicate that the error is happened on
 * the statistic layer
 */
public class StatisticHolderException extends RuntimeException {
    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public StatisticHolderException(String message) {
        super(message);
    }
}
