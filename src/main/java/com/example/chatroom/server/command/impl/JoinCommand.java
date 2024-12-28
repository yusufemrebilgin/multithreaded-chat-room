package com.example.chatroom.server.command.impl;

import com.example.chatroom.server.command.Command;
import com.example.chatroom.server.command.CommandParser;
import com.example.chatroom.server.command.annotation.CommandInfo;
import com.example.chatroom.server.core.RoomManager;
import com.example.chatroom.server.model.Room;
import com.example.chatroom.server.model.User;

@CommandInfo(
        cmd = "join",
        description = "Joins an existing chat room.",
        usage = "/join <room-name> [room-password]"
)
public class JoinCommand implements Command {

    private final RoomManager roomManager;

    public JoinCommand(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void execute(User user, String... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Invalid usage of /join command. For more details /help join");
        }

        CommandParser parser = CommandParser.of(args);
        String roomName = parser.arg(0).required().get();
        String roomPassword = parser.arg(1).optional().getOrDefault("");

        Room existingChatRoom = roomManager.get(roomName);
        existingChatRoom.join(roomPassword, user);
    }

}
