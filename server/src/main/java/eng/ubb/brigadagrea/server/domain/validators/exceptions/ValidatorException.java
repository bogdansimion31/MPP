package eng.ubb.brigadagrea.server.domain.validators.exceptions;

/**
 * The type Validator exception.
 */
public class ValidatorException extends StoreException{
    /**
     * Instantiates a new Validator exception.
     *
     * @param message the message
     */
    public ValidatorException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Validator exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Validator exception.
     *
     * @param cause the cause
     */
    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
