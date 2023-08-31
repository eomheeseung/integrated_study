package example.integrated_test.customException;

public class InquiryDocumentException extends CreateIndexException {
    public InquiryDocumentException(String message) {
        super(message);
    }
}
