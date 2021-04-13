package eng.ubb.brigadagrea.common;

public class ElectronicStoreServiceException extends RuntimeException{
    public ElectronicStoreServiceException(String message) {
        super(message);
    }

    public ElectronicStoreServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
