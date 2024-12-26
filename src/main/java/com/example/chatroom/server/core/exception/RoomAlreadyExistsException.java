package com.example.chatroom.server.core.exception;

/**
 * The {@code RoomAlreadyExistsException} is a runtime exception thrown when
 * attempting to create a chat room with a name that already exists.
 */
public class RoomAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new {@code RoomAlreadyExistsException} with the specified room name.
     *
     * @param roomName the name of the chat room that already exists.
     */
    public RoomAlreadyExistsException(String roomName) {
        super(String.format("A room with the name '%s' already exists", roomName));
    }

}
