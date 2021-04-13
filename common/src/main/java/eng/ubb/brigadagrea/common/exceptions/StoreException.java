package eng.ubb.brigadagrea.common.exceptions;

/**
 * The type Store exception.
 */
public class StoreException extends RuntimeException{
    /**
     * Instantiates a new Store exception.
     *
     * @param message the message
     */
    public StoreException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Store exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Store exception.
     *
     * @param cause the cause
     */
    public StoreException(Throwable cause) {
        super(cause);
    }
}
