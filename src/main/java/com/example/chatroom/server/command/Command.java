package com.example.chatroom.server.command;

import com.example.chatroom.server.model.User;

/**
 * Represents a command that can be executed by a user in the chat application.
 * <p>
 * Implementations of this interface should define the behavior of the command
 * that will be executed when the user triggers it. Commands typically operate
 * on user input, process arguments, and produce results or side effects
 * (such as creating room, joining a room , leaving a room etc.).
 *
 * @see User
 */
@FunctionalInterface
public interface Command {

    /**
     * Executes the command with the given arguments for the specified user.
     *
     * @param user The user who is executing the command.
     * @param args The arguments passed to the command (e.g., room names, user actions).
     * @throws IllegalArgumentException If the arguments are invalid for the command.
     */
    void execute(User user, String... args);

}
