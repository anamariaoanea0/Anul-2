package validator;


/**
 * Custom exception class for repository and file operations errors.
 */
public class RepositoryException extends RuntimeException{

    /**
     * Constructs a new RepositoryException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public RepositoryException(String message){
        super(message);
    }

    /**
     * Constructs a new RepositoryException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the underlying cause of the exception
     */
    public RepositoryException(String message, Throwable cause){
        super(message, cause);
    }
}
