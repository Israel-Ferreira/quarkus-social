package io.codekaffee.quarkussocial.exceptions;

public class FollowerIdIsEqualToUserException extends RuntimeException {
    public FollowerIdIsEqualToUserException() {
        super("Error: UserId is Equal to Follower Id");
    }

    public FollowerIdIsEqualToUserException(String message) {
        super(message);
    }

    public FollowerIdIsEqualToUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
