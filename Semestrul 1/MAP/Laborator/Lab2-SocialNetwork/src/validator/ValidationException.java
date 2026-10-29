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
}
