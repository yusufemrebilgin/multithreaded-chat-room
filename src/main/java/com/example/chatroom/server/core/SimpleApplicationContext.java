package com.example.chatroom.server.core;

import com.example.chatroom.server.command.CommandHandler;
import com.example.chatroom.server.command.CommandRegistry;
import com.example.chatroom.server.core.config.ApplicationConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple implementation of an application context that provides and manages core components
 * of the chat room server application.
 * <p>
 * This class serves as a lightweight dependency injection container, lazily initializing and
 * providing instances of key services and managers as needed. It ensures that shared components
 * like {@link RoomManager}, {@link ConnectionManager}, {@link CommandHandler},
 * {@link CommandRegistry}, and {@link ExecutorService} are instantiated only once (singleton-like).
 */
public class SimpleApplicationContext {

    private RoomManager roomManager;
    private ConnectionManager connectionManager;
    private CommandHandler commandHandler;
    private CommandRegistry commandRegistry;
    private ExecutorService executorService;

    /**
     * Provides a singleton-like instance of {@link RoomManager}.
     * Initializes the instance if it hasn't been created yet.
     *
     * @return the instance of {@link RoomManager}.
     */
    public RoomManager getRoomManager() {
        if (roomManager == null) {
            roomManager = new RoomManager();
        }
        return roomManager;
    }

    /**
     * Provides a singleton-like instance of {@link ConnectionManager}.
     * Initializes the instance if it hasn't been created yet.
     *
     * @return the instance of {@link ConnectionManager}.
     */
    public ConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }

    /**
     * Provides a singleton-like instance of {@link CommandHandler}.
     * Initializes the instance if it hasn't been created yet.
     * The {@link CommandHandler} depends on {@link CommandRegistry}.
     *
     * @return the instance of {@link CommandHandler}.
     */
    public CommandHandler getCommandHandler() {
        if (commandHandler == null) {
            commandHandler = new CommandHandler(getCommandRegistry());
        }
        return commandHandler;
    }

    /**
     * Provides a singleton-like instance of {@link CommandRegistry}.
     * Initializes the instance if it hasn't been created yet.
     *
     * @return the instance of {@link CommandRegistry}.
     */
    public CommandRegistry getCommandRegistry() {
        if (commandRegistry == null) {
            commandRegistry = new CommandRegistry();
        }
        return commandRegistry;
    }

    /**
     * Provides a singleton-like instance of {@link ExecutorService}.
     * Initializes the instance with a fixed thread pool size, as defined in
     * {@link ApplicationConfig#THREAD_POOL_SIZE}, if it hasn't been created yet.
     *
     * @return the instance of {@link ExecutorService}.
     */
    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(ApplicationConfig.THREAD_POOL_SIZE);
        }
        return executorService;
    }

}
