package validator;

/**
 * Custom exception class for business logic errors in the service layer.
 */
public class ServiceException extends RuntimeException{

    /**
     * Constructs a new ServiceException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ServiceException(String message){
        super(message);
    }
}
