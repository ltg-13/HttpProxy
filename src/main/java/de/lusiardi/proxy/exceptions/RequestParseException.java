package de.lusiardi.proxy.exceptions;

/**
 * Exception to indicate a problem while parsing a HTTP reuest.
 *
 * @author Joachim Lusiardi
 */
public class RequestParseException extends Exception {

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     */
    public RequestParseException(String message) {
        super(message);
    }
}
