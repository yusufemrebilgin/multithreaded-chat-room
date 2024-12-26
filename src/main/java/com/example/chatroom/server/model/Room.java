package com.example.chatroom.server.model;

import com.example.chatroom.server.core.ClientConnection;
import com.example.chatroom.server.core.exception.IncorrectPasswordException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a chat room where users can join, leave, and communicate with each other.
 * This class manages user interactions and room-specific operations, ensuring thread-safe behavior.
 */
public class Room {

    private final String name;
    private final String password;

    private final Set<User> activeUsers;

    /**
     * Constructs a new {@code Room} with the specified name and password.
     *
     * @param name     the name of the room.
     * @param password the password for the room. (Consider using hashing for security.)
     */
    public Room(String name, String password) {
        this.name = name;
        this.password = password; // todo: implement hashing for better security
        this.activeUsers = ConcurrentHashMap.newKeySet();
    }

    /**
     * Gets the name of the room.
     *
     * @return the name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * Allows a user to join the room if the provided password matches the room's password.
     *
     * @param password the password provided by the user.
     * @param newUser  the user attempting to join the room.
     * @throws IncorrectPasswordException if the provided password is incorrect.
     */
    public void join(String password, User newUser) {
        if (password != null && !password.equals(this.password)) {
            throw new IncorrectPasswordException();
        }

        if (activeUsers.add(newUser)) {
            newUser.setCurrentRoom(this);
            sendInfoMessageToUser(newUser, "Joined room successfully!");
            broadcast(newUser, String.format("%s has joined to chat!", newUser.getUsername()));
        }
    }

    /**
     * Removes a user from the room. Notifies the room and the user about the action.
     *
     * @param user the user leaving the room.
     * @throws IllegalArgumentException if the user is null or not present in the room.
     */
    public void leave(User user) {
        if (user == null || !activeUsers.contains(user)) {
            throw new IllegalArgumentException("User not present in the current room");
        }

        activeUsers.remove(user);
        user.setCurrentRoom(null);
        broadcast(null, String.format("%s has left the chat!", user.getUsername()));
        sendInfoMessageToUser(user, "You've left the current chat.");
    }

    /**
     * Sends a broadcast message to all users in the room except the sender.
     *
     * @param sender  the user sending the message, or null for system messages.
     * @param message the message to be broadcast.
     */
    public void broadcast(User sender, String message) {
        activeUsers.forEach(user -> {
            if (!sender.equals(user)) {
                ClientConnection connection = user.getConnection();
                connection.send(message);
            }
        });
    }

    /**
     * Checks if the room is empty (i.e., no active users).
     *
     * @return {@code true} if the room is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return activeUsers.isEmpty();
    }

    /**
     * Sends an informational message to a specific user.
     *
     * @param user    the user to send the message to.
     * @param message the informational message.
     */
    private void sendInfoMessageToUser(User user, String message) {
        ClientConnection connection = user.getConnection();
        connection.sendInfo(message);
    }

}
