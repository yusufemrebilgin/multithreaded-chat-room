package com.example.chatroom.server.command.annotation;

import static com.example.chatroom.server.core.config.ApplicationConfig.SYSTEM_MESSAGE_PREFIX;

/**
 * Processes {@link CommandInfo} annotations to generate formatted help messages.
 * <p>
 * This class builds a structured message containing information such as the command name,
 * usage, and description, using the {@link CommandInfo} annotation values. It is typically used
 * for displaying help or system messages related to commands in the chatroom application.
 * <p>
 * The message format includes headers for each section (e.g., CMD, USAGE, DESCRIPTION),
 * and the content is indented for better readability.
 */
public class CommandInfoProcessor {

    // Separator used to format sections in the generated message
    private static final String LINE_SEPARATOR = "\n\t";

    // Header constants for reusability
    private static final String HEADER_CMD = "CMD";
    private static final String HEADER_USAGE = "USAGE";
    private static final String HEADER_DESCRIPTION = "DESCRIPTION";

    private final StringBuilder messageBuilder;

    public CommandInfoProcessor() {
        this.messageBuilder = new StringBuilder();
    }

    /**
     * Builds a formatted help message based on the provided {@link CommandInfo} annotation.
     *
     * @param info the {@link CommandInfo} annotation containing command metadata.
     * @return a formatted string containing the command's information.
     */
    public String buildInfoMessageFromAnnotation(CommandInfo info) {
        messageBuilder.setLength(0); // Reset builder

        appendSection(HEADER_CMD, info.cmd());
        appendSection(HEADER_USAGE, info.usage());
        appendSection(HEADER_DESCRIPTION, info.description());

        return messageBuilder.toString();
    }

    /**
     * Appends a section to the help message with a specific header and content.
     *
     * @param header  the header for the section (e.g., CMD, USAGE, DESCRIPTION).
     * @param content the content to include under the header.
     */
    private void appendSection(String header, String content) {
        messageBuilder.append(SYSTEM_MESSAGE_PREFIX)
                .append(" ")
                .append(header)
                .append(LINE_SEPARATOR)
                .append(content)
                .append("\n");
    }

}
