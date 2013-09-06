package de.lusiardi.proxy.exceptions;

import de.lusiardi.proxy.stream.HttpInputStream;

/**
 * Exception to state a shortness of bytes on an input stream to full fill a
 * request. See {@link HttpInputStream#readFixedNumberOfBytes(byte[], int) } for
 * the usage.
 *
 * @author Joachim Lusiardi
 */
public class NotEnoughBytesAvailableException extends Exception {

    /**
     * Constructs a new Exception.
     *
     * @param message the message of the exception
     */
    public NotEnoughBytesAvailableException(String message) {
        super(message);
    }
}
