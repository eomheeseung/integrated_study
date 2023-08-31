package example.integrated_test.customException;

public class DeleteDocumentException extends CreateIndexException{
    public DeleteDocumentException(String message) {
        super(message);
    }
}
