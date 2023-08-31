package example.integrated_test.customException;

public class CreateDocumentException extends CreateIndexException{
    public CreateDocumentException(String message) {
        super(message);
    }
}
