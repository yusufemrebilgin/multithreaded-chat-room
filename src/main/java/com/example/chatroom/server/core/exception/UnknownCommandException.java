package com.example.chatroom.server.core.exception;

/**
 * The {@code UnknownCommandException} is a runtime exception thrown when an unrecognized
 * command is encountered in the chat room server.
 */
public class UnknownCommandException extends RuntimeException {

    /**
     * Constructs a new {@code UnknownCommandException} with the specified command name.
     *
     * @param command the unrecognized command that caused the exception.
     */
    public UnknownCommandException(String command) {
        super("Unknown command: " + command);
    }

}
