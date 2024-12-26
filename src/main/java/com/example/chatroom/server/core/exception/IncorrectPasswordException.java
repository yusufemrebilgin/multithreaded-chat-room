package com.example.chatroom.server.core.exception;

/**
 * The {@code IncorrectPasswordException} is a runtime exception thrown when
 * an incorrect password is provided for a chat room or user authentication.
 */
public class IncorrectPasswordException extends RuntimeException {

    /**
     * Constructs a new {@code IncorrectPasswordException} with a default error message.
     */
    public IncorrectPasswordException() {
        super("Incorrect password");
    }

}
