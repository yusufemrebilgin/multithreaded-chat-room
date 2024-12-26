package com.example.chatroom.server.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for the chat room server application.
 * <p>
 * This class loads application-level configurations from a properties file
 * and provides constants for various settings used throughout the server.
 *
 * <h3>Configuration Source:</h3>
 * The configuration is loaded from a `config.properties` file located in the classpath.
 * If a configuration property is missing, default values are used for settings.
 */
public class ApplicationConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ApplicationConfig.class.getResourceAsStream("/config.properties")) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration file", ex);
        }
    }

    // Server Configuration
    public static final int SERVER_PORT = getOrDefault("server.port", "8888");
    public static final int THREAD_POOL_SIZE = getOrDefault("server.thread-pool-size", "10");

    // Helper Constants
    public static final String COMMAND_PREFIX = "/";
    public static final String SYSTEM_MESSAGE_PREFIX = ">";

    /**
     * The welcome message displayed to users upon connecting to the server.
     */
    public static final String WELCOME_MESSAGE = """
            > Welcome to the chat-room application!
            > Here's how you can get started!:
                - Type `create` to create a new room
                - Usage: /create <room-name> <room-password>
            
                - Type `join` to join an existing room
                - Usage: /join <room-name> <room-password>
            
            > Need more help?
                - Type `/help` to see all commands.
                - Type `/help <command-name>` for details about a specific command.
            """;

    /**
     * Retrieves an integer configuration value from the properties file.
     * If the key is not found, the provided default value is used.
     *
     * @param key        the configuration key.
     * @param defaultVal the default value to use if the key is not present.
     * @return the configuration value as an integer.
     */
    private static int getOrDefault(String key, String defaultVal) {
        return Integer.parseInt(properties.getProperty(key, defaultVal));
    }

}
