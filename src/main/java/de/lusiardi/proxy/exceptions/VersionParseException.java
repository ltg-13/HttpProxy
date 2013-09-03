package de.lusiardi.proxy.exceptions;

/**
 * Exception to indicate a problem while parsing a HTTP version.
 *
 * @author Joachim Lusiardi
 */
public class VersionParseException extends Exception {

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     */
    public VersionParseException(String message) {
        super(message);
    }
}
