package com.example.chatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple chat room client implementation that connects to a chat server
 * via TCP/IP socket connection. This client can send and receive messages
 * through console input/output.
 */
public class ChatRoomClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8888)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // For console input from client
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            // Listening messages
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        System.out.println(receivedMessage);
                    }
                } catch (IOException ex) {
                    System.err.println("An error occurred at message listening: " + ex.getMessage());
                }
            }).start();

            // Sending messages in main thread
            try {
                String message;
                while ((message = consoleIn.readLine()) != null) {
                    out.println(message);
                }
            } catch (IOException ex) {
                System.err.println("An error occurred at message sending: " + ex.getMessage());
            }

        } catch (IOException ex) {
            System.err.printf(
                    "[%s] - An error occurred at client side: %s\n",
                    Thread.currentThread().getName(),
                    ex.getMessage()
            );
        }
    }
}
