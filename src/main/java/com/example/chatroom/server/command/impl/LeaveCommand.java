package com.example.chatroom.server.command.impl;

import com.example.chatroom.server.command.Command;
import com.example.chatroom.server.command.annotation.CommandInfo;
import com.example.chatroom.server.core.RoomManager;
import com.example.chatroom.server.model.Room;
import com.example.chatroom.server.model.User;

@CommandInfo(
        cmd = "leave",
        description = "Leaves the current chat room.",
        usage = "/leave"
)
public class LeaveCommand implements Command {

    private final RoomManager roomManager;

    public LeaveCommand(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void execute(User user, String... args) {
        if (args.length != 0) {
            throw new IllegalArgumentException("Invalid usage of /leave command. For mor details /help leave");
        }


        Room currentUserRoom = user.getCurrentRoom();
        if (currentUserRoom == null) {
            throw new IllegalStateException("Join a room to execute /leave command");
        }

        currentUserRoom.leave(user);
        if (currentUserRoom.isEmpty()) {
            roomManager.remove(currentUserRoom.getName());
        }
    }

}
