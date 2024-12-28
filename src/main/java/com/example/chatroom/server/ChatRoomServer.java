package com.example.chatroom.server;

import com.example.chatroom.server.command.CommandHandler;
import com.example.chatroom.server.command.CommandRegistry;
import com.example.chatroom.server.command.impl.CreateCommand;
import com.example.chatroom.server.command.impl.HelpCommand;
import com.example.chatroom.server.command.impl.JoinCommand;
import com.example.chatroom.server.core.ClientHandler;
import com.example.chatroom.server.core.ConnectionManager;
import com.example.chatroom.server.core.RoomManager;
import com.example.chatroom.server.core.SimpleApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static com.example.chatroom.server.core.config.ApplicationConfig.SERVER_PORT;

/**
 * The {@code ChatRoomServer} class represents the server component of the chat room application.
 * It is responsible for initializing and managing the server, handling client connections,
 * and registering command handlers for client interaction.
 * <p>
 * This class uses a {@link SimpleApplicationContext} to inject dependencies required for operation,
 * such as the command handler, command registry, room manager, and connection manager.
 * The server listens on a specified port, accepts incoming client connections, and
 * processes commands through a command handler.
 * <p>
 * The server runs continuously in a loop, accepting client connections, and assigning {@link ClientHandler}
 * tasks to an {@link ExecutorService} for concurrent processing.
 * <p>
 * The {@code ChatRoomServer} provides a shutdown hook to ensure the server shuts down gracefully,
 * closing all client connections and shutting down the executor service.
 */
public class ChatRoomServer {

    private static final Logger logger = LoggerFactory.getLogger(ChatRoomServer.class);

    private final CommandHandler handler;
    private final CommandRegistry registry;
    private final ExecutorService executorService;

    private final RoomManager roomManager;
    private final ConnectionManager connectionManager;

    private final int port = SERVER_PORT;

    /**
     * Constructs a {@code ChatRoomServer} instance with the provided application context.
     * The context is used to inject necessary dependencies, such as the command handler,
     * command registry, room manager, connection manager, and executor service.
     *
     * @param context the application context used for dependency injection
     * @throws IllegalArgumentException if the context is null or if the port is not valid
     */
    public ChatRoomServer(SimpleApplicationContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }

        // Inject dependencies
        handler = context.getCommandHandler();
        registry = context.getCommandRegistry();
        roomManager = context.getRoomManager();
        connectionManager = context.getConnectionManager();
        executorService = context.getExecutorService();
        registerCommands();
    }

    /**
     * The entry point for starting the chat room server. It initializes the application context
     * and starts the server on the specified port.
     */
    public static void main(String[] args) {
        SimpleApplicationContext context = new SimpleApplicationContext();
        new ChatRoomServer(context).start();
    }

    /**
     * Starts the server, listening for incoming client connections on the specified port.
     * It accepts client connections and delegates them to a {@link ClientHandler} using an
     * {@link ExecutorService}.
     * <p>
     * A shutdown hook is added to ensure the server shuts down gracefully when the process exits.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void start() {
        logger.info("Starting server on port {}", port);
        // Add shutdown hook for proper exit
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is up and running on port {}", port);
            logger.info("Waiting for clients to connect...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket, handler, connectionManager));
            }
        } catch (IOException ex) {
            logger.error("An error occurred at server side: {}", ex.getMessage(), ex);
        } finally {
            shutdown();
        }
    }

    /**
     * Shuts down the server by closing all connections and shutting down the executor service.
     */
    private void shutdown() {
        logger.info("Shutting down the server...");
        connectionManager.closeAllConnections();
        executorService.shutdownNow();
        logger.info("Server shutdown complete");
    }

    /**
     * Registers the available commands for the server.
     */
    private void registerCommands() {
        registry.register(new CreateCommand(roomManager));
        registry.register(new JoinCommand(roomManager));
        registry.register(new HelpCommand(registry));
    }

}
