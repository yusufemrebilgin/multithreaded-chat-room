package com.example.chatroom.server.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.chatroom.server.core.config.ApplicationConfig.SYSTEM_MESSAGE_PREFIX;

/**
 * The {@code ClientConnection} class provides an abstraction for managing a client socket connection.
 * It encapsulates the input and output streams for communication and provides utility methods
 * for reading, sending messages, checking connection status, and closing the connection.
 */
public class ClientConnection {

    private final Socket clientSocket;

    // Socket streams
    private final BufferedReader socketIn;
    private final PrintWriter socketOut;

    /**
     * Constructs a {@code ClientConnection} with the given client socket.
     * Initializes the input and output streams for communication.
     *
     * @param clientSocket the socket associated with the client.
     * @throws IOException              if an I/O error occurs when creating the input or output stream.
     * @throws IllegalArgumentException if {@code clientSocket} is {@code null}.
     */
    public ClientConnection(Socket clientSocket) throws IOException {
        if (clientSocket == null) {
            throw new IllegalArgumentException("Client socket cannot be null");
        }
        this.clientSocket = clientSocket;

        // Initialize socket's input and output streams
        socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    /**
     * Reads a line of text from the client.
     *
     * @return the line of text received from the client, or {@code null} if the end of the stream is reached.
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    public String read() throws IOException {
        return socketIn.readLine();
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent to the client.
     */
    public void send(String message) {
        socketOut.println(message);
    }

    /**
     * Sends a system message prefixed with a system-specific identifier to the client.
     *
     * @param message the system message to be sent to the client.
     */
    public void sendInfo(String message) {
        send(SYSTEM_MESSAGE_PREFIX + " " + message);
    }

    /**
     * Checks whether the client connection is still active and functional.
     * It verifies that the socket is open, connected, and the output stream is operational.
     *
     * @return {@code true} if the connection is active and operational, {@code false} otherwise.
     */
    public boolean isConnected() {
        if (clientSocket == null || clientSocket.isClosed() || !clientSocket.isConnected()) {
            return false;
        }

        if (socketOut.checkError()) {
            return false;
        }

        // Test write operation
        try {
            socketOut.println();
            socketOut.flush();
            return !socketOut.checkError();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Closes the client connection and releases associated resources.
     * Closing the socket will also close the associated input and output streams.
     *
     * @throws IOException if an I/O error occurs while closing the socket.
     */
    public void close() throws IOException {
        if (!clientSocket.isClosed()) {
            clientSocket.close(); // Closing this socket will also close socket's streams
        }
    }

}
