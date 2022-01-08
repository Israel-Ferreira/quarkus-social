package io.codekaffee.quarkussocial.exceptions;

public class UnauthorizedUserException extends RuntimeException {

    /**
     * 
     */
    public UnauthorizedUserException() {
        super("Usuário não autorizado");
    }

    /**
     * @param message
     */
    public UnauthorizedUserException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public UnauthorizedUserException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
