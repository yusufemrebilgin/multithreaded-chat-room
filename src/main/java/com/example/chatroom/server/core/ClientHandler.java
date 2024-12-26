package com.example.chatroom.server.core;

import com.example.chatroom.server.command.CommandHandler;
import com.example.chatroom.server.model.Room;
import com.example.chatroom.server.model.User;

import java.io.IOException;
import java.net.Socket;

import static com.example.chatroom.server.core.config.ApplicationConfig.*;

/**
 * The {@code ClientHandler} class is responsible for managing a single client connection in the chatroom server.
 * It handles user initializing, message processing, and communication between the client and the server.
 * The class also supports processing commands and relaying messages to chat rooms.
 */
public class ClientHandler implements Runnable {

    private User client;
    private ClientConnection clientConnection;

    private final Socket clientSocket;
    private final CommandHandler commandHandler;
    private final ConnectionManager connectionManager;

    /**
     * Creates a new {@code ClientHandler} for managing a client connection.
     *
     * @param clientSocket      the socket associated with the client.
     * @param commandHandler    the {@link CommandHandler} for processing commands from the client.
     * @param connectionManager the {@link ConnectionManager} for managing active client connections.
     * @throws IllegalArgumentException if {@code clientSocket} is {@code null}.
     */
    public ClientHandler(Socket clientSocket, CommandHandler commandHandler, ConnectionManager connectionManager) {
        if (clientSocket == null) {
            throw new IllegalArgumentException("Client socket cannot be null");
        }
        this.clientSocket = clientSocket;
        this.commandHandler = commandHandler;
        this.connectionManager = connectionManager;
    }

    /**
     * The main entry point for handling the client connection.
     * This method initializes the client connection, validates the username, and starts listening for client input.
     */
    @Override
    public void run() {
        try {
            // Initialize client connection and streams
            clientConnection = new ClientConnection(clientSocket);

            // Prompt for username and initialize user
            sendInfoMessageToClient("Please enter your username: ");
            String username;
            while ((username = clientConnection.read()) != null) {
                if (isUsernameValid(username)) {
                    this.client = new User(username, clientConnection);
                    connectionManager.addConnection(client.getId(), clientConnection);
                    break;
                }
                // If username is not valid send prompt again
                sendInfoMessageToClient("Please try again: ");
            }

            sendWelcomeMessageToClient();
            listenAndProcessUserInput();

        } catch (IOException ex) {
            closeConnection();
        }
    }

    // Helper Methods

    /**
     * Validates the username provided by the client.
     *
     * @param username the username to validate.
     * @return {@code true} if the username is valid, {@code false} otherwise.
     * @throws IOException if an error occurs while sending messages to the client.
     */
    private boolean isUsernameValid(String username) throws IOException {
        if (username == null || username.isBlank()) {
            sendInfoMessageToClient("Username cannot be empty");
            return false;
        }

        int length = username.length();
        if (length < 3 || length > 8) {
            sendInfoMessageToClient("Username length must be between 3 and 8 characters");
            return false;
        }

        return true;
    }

    /**
     * Sends a welcome message to the client after successful authentication.
     */
    private void sendWelcomeMessageToClient() {
        clientConnection.send(WELCOME_MESSAGE);
    }

    /**
     * Sends an informational message to the client.
     *
     * @param message the message to send.
     */
    private void sendInfoMessageToClient(String message) {
        clientConnection.sendInfo(message);
    }

    /**
     * Listens for user input and processes it. Commands are handled separately, while normal messages
     * are broadcast to the chatroom the client is currently in.
     *
     * @throws IOException if an error occurs while reading from the client.
     */
    private void listenAndProcessUserInput() throws IOException {
        String userInput;
        while ((userInput = clientConnection.read()) != null) {
            if (userInput.trim().isEmpty())
                continue;

            if (userInput.startsWith(COMMAND_PREFIX)) {
                processCommand(userInput);
            } else {
                processMessage(userInput);
            }
        }
    }

    /**
     * Processes a command input by the user.
     *
     * @param userInput the command string input by the client.
     */
    private void processCommand(String userInput) {
        try {
            commandHandler.handle(client, userInput);
        } catch (Exception ex) {
            sendInfoMessageToClient(ex.getMessage());
        }
    }

    /**
     * Processes a normal message input by the user.
     * If the user is in a chatroom, the message is broadcast to other members.
     * Otherwise, an informational message is sent to the client.
     *
     * @param userInput the message string input by the client.
     */
    private void processMessage(String userInput) {
        Room clientRoom = client.getCurrentRoom();
        if (clientRoom != null) {
            clientRoom.broadcast(client, String.format("%s:> %s", client.getUsername(), userInput));
        } else {
            sendInfoMessageToClient("Join a room to send a message");
        }
    }

    /**
     * Closes the client connection and removes it from the {@link ConnectionManager}.
     */
    private void closeConnection() {
        connectionManager.closeConnection(client.getId());
    }

}
