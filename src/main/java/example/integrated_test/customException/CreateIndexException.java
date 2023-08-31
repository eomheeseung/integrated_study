package example.integrated_test.customException;

public class CreateIndexException extends RuntimeException {
    public CreateIndexException() {
        super();
    }

    public CreateIndexException(String message) {
        super(message);
    }

    public CreateIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateIndexException(Throwable cause) {
        super(cause);
    }

    protected CreateIndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
