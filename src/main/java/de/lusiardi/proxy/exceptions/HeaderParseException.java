package de.lusiardi.proxy.exceptions;

/**
 * Exception to indicate a problem while parsing a HTTP header.
 *
 * @author Joachim Lusiardi
 */
public class HeaderParseException extends Exception {

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     */
    public HeaderParseException(String message) {
        super(message);
    }
}
