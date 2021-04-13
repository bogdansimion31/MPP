package eng.ubb.brigadagrea.server.domain.validators.exceptions;

/**
 * The type System exception.
 */
public class SystemException  extends RuntimeException{
    /**
     * Instantiates a new Validator exception.
     *
     * @param message the message
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Validator exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Validator exception.
     *
     * @param cause the cause
     */
    public SystemException(Throwable cause) {
        super(cause);
    }
}
