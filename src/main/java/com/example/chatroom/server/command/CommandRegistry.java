package com.example.chatroom.server.command;

import com.example.chatroom.server.command.annotation.CommandInfo;
import com.example.chatroom.server.command.annotation.CommandInfoProcessor;

import java.util.HashMap;
import java.util.Map;

import static com.example.chatroom.server.core.config.ApplicationConfig.SYSTEM_MESSAGE_PREFIX;

/**
 * The {@code CommandRegistry} class manages the registration and retrieval of commands
 * for the chatroom server. It ensures that all commands are annotated with {@link CommandInfo}
 * and provides methods to fetch command implementations and their related information.
 */
public class CommandRegistry {

    // Stores registered commands with their identifiers
    private final Map<String, Command> commands = new HashMap<>();

    // Caches command information for quick retrieval
    private final Map<String, String> commandInfoCache = new HashMap<>();

    private final CommandInfoProcessor commandInfoProcessor = new CommandInfoProcessor();

    /**
     * Registers a new command in the registry. The command class must be annotated with {@link CommandInfo}.
     *
     * @param command the command to register.
     * @throws IllegalArgumentException if the command is null or not annotated with {@link CommandInfo}.
     */
    public void register(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        Class<?> commandClass = command.getClass();
        if (!commandClass.isAnnotationPresent(CommandInfo.class)) {
            throw new IllegalArgumentException(
                    String.format("Command class %s must have @CommandInfo annotation", commandClass.getName())
            );
        }

        CommandInfo info = commandClass.getAnnotation(CommandInfo.class);
        commands.put(info.cmd(), command);
        commandInfoCache.put(info.cmd(), commandInfoProcessor.buildInfoMessageFromAnnotation(info));
    }

    /**
     * Retrieves a command by its identifier.
     *
     * @param cmd the identifier of the command to retrieve.
     * @return the command associated with the given identifier.
     * @throws IllegalArgumentException if the command is not found in the registry.
     */
    public Command getCommand(String cmd) {
        if (!commands.containsKey(cmd)) {
            throw new IllegalArgumentException("Command not found");
        }
        return commands.get(cmd);
    }

    /**
     * Retrieves the information message for a command by its identifier.
     *
     * @param cmd the identifier of the command.
     * @return the information message for the given command.
     * @throws IllegalArgumentException if the command information is not found in the cache.
     */
    public String getCommandInfo(String cmd) {
        if (!commandInfoCache.containsKey(cmd)) {
            throw new IllegalArgumentException("Unknown command: " + cmd);
        }
        return commandInfoCache.get(cmd);
    }

    /**
     * Generates a help message listing all available commands with descriptions.
     * <p>
     * It formats the commands and their descriptions, then adds instructions for viewing more details about
     * each command.
     *
     * @return a string containing the list of commands and usage instructions.
     */
    public String getHelpMessage() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append(SYSTEM_MESSAGE_PREFIX).append(" Available Commands:\n\n");

        for (String cmd : commands.keySet()) {
            Class<?> commandClass = commands.get(cmd).getClass();
            CommandInfo commandInfo = commandClass.getAnnotation(CommandInfo.class);
            helpMessage.append(String.format("\t%-20s %s\n", commandInfo.cmd(), commandInfo.description()));
        }

        helpMessage.append("\n" + SYSTEM_MESSAGE_PREFIX);
        helpMessage.append(" To see more details, try /help [command-name]");
        return helpMessage.toString();
    }

}
