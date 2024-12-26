package com.example.chatroom.server.core;

import com.example.chatroom.server.core.exception.RoomAlreadyExistsException;
import com.example.chatroom.server.core.exception.RoomNotFoundException;
import com.example.chatroom.server.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code RoomManager} class is responsible for managing chat rooms in the server.
 * It provides functionality to create, retrieve, and remove chat rooms, ensuring thread-safe
 * operations using a {@link ConcurrentHashMap}.
 */
public class RoomManager {

    private static final Logger logger = LoggerFactory.getLogger(RoomManager.class);

    private final Map<String, Room> activeChatRooms = new ConcurrentHashMap<>();

    /**
     * Creates a new chat room with the specified name and password.
     * If a room with the given name already exists, a {@link RoomAlreadyExistsException} is thrown.
     *
     * @param roomName     the name of the chat room to be created.
     * @param roomPassword the password for the chat room.
     * @return the newly created {@link Room}.
     * @throws RoomAlreadyExistsException if a room with the given name already exists.
     */
    public Room create(String roomName, String roomPassword) {
        if (activeChatRooms.containsKey(roomName)) {
            throw new RoomAlreadyExistsException(roomName);
        }

        Room newChatRoom = new Room(roomName, roomPassword);
        activeChatRooms.put(roomName, newChatRoom);

        return newChatRoom;
    }

    /**
     * Retrieves a chat room by its name.
     * If the room does not exist, a {@link RoomNotFoundException} is thrown.
     *
     * @param roomName the name of the chat room to retrieve.
     * @return the {@link Room} with the specified name.
     * @throws RoomNotFoundException if the room with the given name does not exist.
     */
    public Room get(String roomName) {
        Room room = activeChatRooms.get(roomName);
        if (room == null) {
            logger.debug("Chat room '{}' not found", roomName);
            throw new RoomNotFoundException(roomName);
        }
        return room;
    }

    /**
     * Removes a chat room by its name.
     * If the room does not exist, a warning is logged, but no exception is thrown.
     *
     * @param roomName the name of the chat room to remove.
     */
    public void remove(String roomName) {
        if (!activeChatRooms.containsKey(roomName)) {
            logger.warn("Failed to remove chat room. Room '{}' does not exist.", roomName);
            return;
        }

        activeChatRooms.remove(roomName);
        logger.info("Chat room '{}' removed successfully.", roomName);
    }

}
