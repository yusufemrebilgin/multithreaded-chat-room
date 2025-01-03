# Chat Room Application

This multithreaded chat room application is built in Java and designed to handle multiple client connections 
concurrently. Users interact with the application through a command-line interface (CLI), enabling them to create, 
join, leave chat rooms, and send messages. Communication within each room is private and room-specific, ensuring 
that users can only communicate with others in the same room.

The core functionality revolves around commands, which are processed using the **Command Pattern**. Each command is 
represented by a dedicated class, and the system uses the `@CommandInfo` annotation to generate help messages for users. 
This annotation provides metadata, including the command name, description, and usage details, making it easy to present 
comprehensive help information when requested.

The application's core components, such as the command handler, room manager, and connection manager, are efficiently 
managed and injected using a lightweight dependency injection container. This mechanism ensures clean separation of 
concerns and simplifies the management of components.

### Requirements
- Java 17 or higher
- Maven

### Running the Server:
```shell
mvn clean compile exec:java -Dexec.mainClass="com.example.chatroom.server.ChatRoomServer"
```

### Running the Client:
```shell
mvn clean compile exec:java -Dexec.mainClass="com.example.chatroom.client.ChatRoomClient"
```

### Getting Started

Once you've successfully entered your username, you’ll be greeted with helpful instructions to get you started:

```
> Welcome to the chat-room application!
> Here's how you can get started!:
    - Type `create` to create a new room
    - Usage: /create <room-name> [room-password]

    - Type `join` to join an existing room
    - Usage: /join <room-name> [room-password]

> Need more help?
    - Type `/help` to see all commands.
    - Type `/help [command]` for details about a specific command.
```

### Example Command Implementation

```java
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
```