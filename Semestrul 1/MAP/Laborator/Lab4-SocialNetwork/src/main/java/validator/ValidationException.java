package validator;

/**
 * Custom exception class for validation errors.
 */
public class ValidationException extends RuntimeException{
    /**
     * Constructor for ValidationException.
     *
     * @param message the detail message explaining the error.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor for exception chaining.
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
