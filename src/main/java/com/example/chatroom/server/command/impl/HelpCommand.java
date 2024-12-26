package com.example.chatroom.server.command.impl;

import com.example.chatroom.server.command.Command;
import com.example.chatroom.server.command.CommandRegistry;
import com.example.chatroom.server.command.annotation.CommandInfo;
import com.example.chatroom.server.core.ClientConnection;
import com.example.chatroom.server.model.User;

@CommandInfo(
        cmd = "help",
        description = "Displays help information for commands.",
        usage = "/help [command]"
)
public class HelpCommand implements Command {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void execute(User user, String... args) {
        if (args.length > 1) {
            throw new IllegalArgumentException("Invalid usage of /help command. Usage: /help [command]");
        }

        ClientConnection connection = user.getConnection();
        if (args.length == 0) {
            connection.send(registry.getCommandInfo("help"));
            return;
        }

        // If [command] argument is given
        String command = args[0];
        connection.send(registry.getCommandInfo(command));
    }

}
