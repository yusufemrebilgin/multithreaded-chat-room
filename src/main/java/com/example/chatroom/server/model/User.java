package com.example.chatroom.server.model;

import com.example.chatroom.server.core.ClientConnection;

import java.util.UUID;

/**
 * Represents a user in the chat application.
 * <p>
 * Each user has a unique user ID and a username. The user can be assigned to a chat room,
 * which is represented by the {@link Room} object. The username must be non-null and non-blank.
 * The user ID is generated automatically using a UUID.
 */
public class User {

    private final String id;
    private final String username;

    private Room currentRoom;
    private final ClientConnection connection;

    /**
     * Constructs a new {@link User} with the specified username.
     * <p>
     * The username must not be null or blank. An {@link IllegalArgumentException} is thrown
     * if the username is invalid.
     *
     * @param username the username of the user.
     * @throws IllegalArgumentException if the username is null or blank.
     */
    public User(String username, ClientConnection connection) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        this.username = username;
        this.id = generateId();
        this.connection = connection;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user's unique ID in the format "user-xxxxxxxx"
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the current chat room that the user is part of.
     *
     * @return the current room, or null if the user is not in any room.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Sets the chat room that the user is currently in.
     *
     * @param currentRoom the {@link Room} to set as the user's current room.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public ClientConnection getConnection() {
        return connection;
    }

    /**
     * Generates a unique user ID using a randomly generated UUID.
     * <p>
     * The ID is prefixed with "user-" and is truncated to the first 8 characters of the UUID string.
     *
     * @return the generated unique user ID.
     */
    private String generateId() {
        String uuid = UUID.randomUUID().toString();
        return "user-" + uuid.substring(0, 8);
    }

}
