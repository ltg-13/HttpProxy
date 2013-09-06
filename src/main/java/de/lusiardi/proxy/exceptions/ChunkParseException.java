package de.lusiardi.proxy.exceptions;

/**
 * Exception to indicate a problem while parsing a HTTP body chunk.
 *
 * @author Joachim Lusiardi
 */
public class ChunkParseException extends Exception {

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     */
    public ChunkParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     * @param cause the causing {@link Throwable} of this exception
     */
    public ChunkParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
