package com.example.chatroom.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@code ConnectionManager} class manages active client connections in the chatroom server.
 * It ensures connections are monitored periodically for validity and provides mechanisms
 * to add, close, and maintain client connections.
 */
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    // Stores active connections mapped by user ID
    private final Map<String, ClientConnection> activeConnections = new ConcurrentHashMap<>();

    // Scheduled executor for monitoring connections periodically
    private final ScheduledExecutorService connectionMonitor = Executors.newSingleThreadScheduledExecutor();

    // Interval for checking active connections in seconds
    private static final int CHECK_INTERVAL_SECONDS = 10;

    /**
     * Creates a new {@code ConnectionManager} instance and starts connection monitoring.
     */
    public ConnectionManager() {
        startConnectionMonitoring();
    }

    /**
     * Starts periodic monitoring of active connections. Invalid connections are removed automatically.
     */
    private void startConnectionMonitoring() {
        connectionMonitor.scheduleAtFixedRate(this::checkConnections, 10, CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Checks all active connections to verify their validity. Removes invalid connections.
     * Logs the number of active connections before and after the check.
     */
    private void checkConnections() {
        int initialCount = getActiveConnectionCount();
        logger.debug("Starting connection check. Current active connections: {}", initialCount);

        activeConnections.entrySet().removeIf(entry -> {
            ClientConnection connection = entry.getValue();
            if (!isConnectionValid(connection)) {
                closeConnection(entry.getKey());
                return true;
            }
            return false;
        });

        int finalCount = getActiveConnectionCount();
        if (initialCount != finalCount) {
            logger.info("Connection check completed. Connections changed: {} -> {}", initialCount, finalCount);
        }
    }

    /**
     * Verifies if a connection is valid.
     *
     * @param connection the {@link ClientConnection} to validate.
     * @return {@code true} if the connection is valid and connected, {@code false} otherwise.
     */
    private boolean isConnectionValid(ClientConnection connection) {
        return connection != null && connection.isConnected();
    }

    /**
     * Adds a new client connection to the active connections pool.
     *
     * @param userId     the unique identifier of the client.
     * @param connection the {@link ClientConnection} to add.
     * @throws IllegalArgumentException if either {@code userId} or {@code connection} is {@code null}.
     */
    public void addConnection(String userId, ClientConnection connection) {
        if (userId == null || connection == null) {
            throw new IllegalArgumentException("User ID or connection cannot be null");
        }
        activeConnections.put(userId, connection);
        logger.info("New client connected with id '{}' (Active Connection(s): {})", userId, getActiveConnectionCount());
    }

    /**
     * Closes the connection for a given user ID and removes it from the active connections pool.
     *
     * @param userId the unique identifier of the client whose connection is to be closed.
     */
    public void closeConnection(String userId) {
        if (activeConnections.containsKey(userId)) {
            logger.error("Connection not active for client '{}'", userId);
            return;
        }

        ClientConnection connection = activeConnections.get(userId);
        if (connection == null) {
            logger.error("Attempted to close non-existent connection");
            return;
        }

        try {
            connection.close();
            activeConnections.remove(userId);
            logger.info("Connection closed for client - {}, (Active: {})", userId, getActiveConnectionCount());
        } catch (IOException ex) {
            logger.error("Closing connection failed for client '{}'", userId, ex);
        }
    }

    /**
     * Closes all active connections and shuts down the connection monitor service.
     */
    public void closeAllConnections() {
        activeConnections.keySet().forEach(this::closeConnection);
        connectionMonitor.shutdownNow();
    }

    /**
     * Returns the number of active connections currently being managed.
     *
     * @return the count of active connections.
     */
    private int getActiveConnectionCount() {
        return activeConnections.size();
    }

}
