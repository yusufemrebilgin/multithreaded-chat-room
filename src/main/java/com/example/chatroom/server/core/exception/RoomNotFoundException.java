package com.example.chatroom.server.core.exception;

/**
 * The {@code RoomNotFoundException} is a runtime exception thrown when a chat room
 * with the specified name cannot be found.
 */
public class RoomNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code RoomNotFoundException} with the specified room name.
     *
     * @param roomName the name of the chat room that could not be found.
     */
    public RoomNotFoundException(String roomName) {
        super("Room not found with name " + roomName);
    }

}
