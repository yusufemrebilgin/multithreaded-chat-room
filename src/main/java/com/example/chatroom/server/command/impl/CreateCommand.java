package com.example.chatroom.server.command.impl;

import com.example.chatroom.server.command.Command;
import com.example.chatroom.server.command.CommandParser;
import com.example.chatroom.server.command.annotation.CommandInfo;
import com.example.chatroom.server.core.RoomManager;
import com.example.chatroom.server.model.Room;
import com.example.chatroom.server.model.User;

@CommandInfo(
        cmd = "create",
        description = "Creates a new chat room.",
        usage = "/create <room-name> [room-password]"
)
public class CreateCommand implements Command {

    private final RoomManager roomManager;

    public CreateCommand(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void execute(User user, String... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Invalid usage of /create command. For more details /help create");
        }

        CommandParser parser = CommandParser.of(args);
        String roomName = parser.arg(0).required().get();
        String roomPassword = parser.arg(1).optional().getOrDefault("");

        Room newChatRoom = roomManager.create(roomName, roomPassword);
        newChatRoom.join(roomPassword, user);
    }

}
