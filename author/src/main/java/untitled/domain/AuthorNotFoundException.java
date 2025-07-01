package untitled.domain;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id) {
        super("Author not found with id: " + id);
    }
    
    public AuthorNotFoundException(String message) {
        super(message);
    }
    
    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 