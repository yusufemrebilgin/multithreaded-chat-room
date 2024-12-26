package com.example.chatroom.server.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define metadata for chat application commands.
 * <p>
 * This annotation can be applied to command implementation classes to provide information
 * about the command, such as its name, description, and usage instructions.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>
 * {@code
 * @CommandInfo(
 *     cmd = "help",
 *     description = "Displays help information for commands.",
 *     usage = "/help [command]"
 * )
 * public class HelpCommand implements Command {
 *     // Implementation here
 * }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    /**
     * Specifies the command name.
     *
     * @return the command string.
     */
    String cmd();

    /**
     * Provides a description of the command.
     *
     * @return a short explanation of the command.
     */
    String description();

    /**
     * Details the correct usage format for the command.
     *
     * @return a string representing the command usage syntax.
     */
    String usage();

}
