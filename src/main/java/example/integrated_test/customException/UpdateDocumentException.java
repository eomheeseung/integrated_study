package example.integrated_test.customException;

public class UpdateDocumentException extends CreateIndexException {
    public UpdateDocumentException(String message) {
        super(message);
    }
}
