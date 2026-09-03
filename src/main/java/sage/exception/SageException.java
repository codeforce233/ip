package sage.exception;

/**
 * Signals that user input or task operations are invalid.
 */
public class SageException extends Exception {
    /**
     * Creates a new exception with the given user-facing message.
     *
     * @param message The reason the command failed.
     */
    public SageException(String message) {
        super(message);
    }
}
