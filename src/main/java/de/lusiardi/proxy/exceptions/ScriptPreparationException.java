package de.lusiardi.proxy.exceptions;

/**
 * Exception to indicate problems while preparing a script.
 *
 * @author Joachim Lusiardi
 */
public class ScriptPreparationException extends Exception {

    /**
     * Constructs a new exception.
     *
     * @param message the message for the exception
     * @param cause the cause for the exception
     */
    public ScriptPreparationException(String message, Throwable cause) {
        super(message, cause);
    }
}
