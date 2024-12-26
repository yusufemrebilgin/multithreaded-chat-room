package com.example.chatroom.server.command;

import com.example.chatroom.server.core.exception.UnknownCommandException;
import com.example.chatroom.server.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * The {@code CommandHandler} class is responsible for processing and executing commands received from users.
 * It uses a {@link CommandRegistry} to look up and invoke the appropriate command handler.
 * If a command is not recognized, an {@link UnknownCommandException} is thrown.
 */
public class CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    private final CommandRegistry registry;

    /**
     * Constructs a {@code CommandHandler} with the specified {@code CommandRegistry}.
     *
     * @param registry the {@link CommandRegistry} used to map command names to their corresponding handlers.
     */
    public CommandHandler(CommandRegistry registry) {
        this.registry = registry;
    }

    /**
     * Handles a user command by parsing the input, resolving the command, and executing it with the provided arguments.
     * If the command is not registered, an {@link UnknownCommandException} is thrown.
     *
     * @param user      the {@link User} who issued the command.
     * @param userInput the raw input string received from the user.
     * @throws UnknownCommandException if the command is not registered in the {@link CommandRegistry}.
     */
    public void handle(User user, String userInput) {
        String[] parts = userInput.split("\\s++");
        // Example command: "/create <room-name> <room-pw>
        String command = parts[0].substring(1); // Remove command prefix '/'

        String[] commandArgs = Arrays.copyOfRange(parts, 1, parts.length);
        Optional.ofNullable(registry.getCommand(command)).ifPresentOrElse(
                cmd -> cmd.execute(user, commandArgs),
                () -> handleUnknownCommand(user, command)
        );
    }

    /**
     * Handles an unrecognized command by logging a warning and throwing an {@link UnknownCommandException}.
     *
     * @param user    the {@link User} who issued the unknown command.
     * @param command the unrecognized command name.
     * @throws UnknownCommandException always thrown to indicate the command is not recognized.
     */
    private void handleUnknownCommand(User user, String command) {
        logger.warn("Unknown command '{}' received from user '{}'", command, user.getUsername());
        throw new UnknownCommandException(command);
    }

}
