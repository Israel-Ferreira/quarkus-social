package io.codekaffee.quarkussocial.exceptions;

public class UserConflictException extends RuntimeException {

    /**
     * 
     */
    public UserConflictException() {
        super("Conflito de Usuarios");
    }

    /**
     * @param message
     */
    public UserConflictException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public UserConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
