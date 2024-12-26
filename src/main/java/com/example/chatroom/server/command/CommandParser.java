package com.example.chatroom.server.command;

import java.util.Objects;

/**
 * A utility class for parsing command-line arguments.
 * <p>
 * This class helps retrieve arguments from an array of strings, marking them as
 * either required or optional. It provides methods to extract argument values
 * and convert them to different data types.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * CommandParser parser = CommandParser.of(args);
 * String username = parser.arg(0).required().get();
 * int age = parser.arg(1).optional().getAsIntOrDefault(18);
 * }
 * </pre>
 */
public class CommandParser {

    private final String[] arguments;

    private CommandParser(String[] args) {
        this.arguments = Objects.requireNonNull(args).clone();
    }

    /**
     * Creates a new CommandParser instance with the given arguments.
     *
     * @param args the array of arguments to parse
     * @return a new CommandParser instance
     */
    public static CommandParser of(String[] args) {
        return new CommandParser(args);
    }

    /**
     * Retrieves an argument by index. If the index is out of bounds,
     * it returns an empty string as the argument value.
     *
     * @param index the index of the argument to retrieve
     * @return an Argument instance wrapping the value at the given index
     * @throws IllegalArgumentException if the index is negative
     */
    public Argument arg(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Argument index cannot be negative");
        }

        return new Argument(index < arguments.length ? arguments[index] : "");
    }

    /**
     * Base class for arguments. Provides methods to specify whether
     * an argument is required or optional.
     */
    public static class Argument {

        private final String value;

        private Argument(String value) {
            this.value = value;
        }

        /**
         * Marks the argument as required and returns a RequiredArgument instance.
         *
         * @return a RequiredArgument instance wrapping this argument's value
         */
        public RequiredArgument required() {
            return new RequiredArgument(value);
        }

        /**
         * Marks the argument as optional and returns an OptionalArgument instance.
         *
         * @return an OptionalArgument instance wrapping this argument's value
         */
        public OptionalArgument optional() {
            return new OptionalArgument(value);
        }

    }

    /**
     * Represents a required argument. Ensures the value is present
     * and provides methods to retrieve it in different formats.
     */
    public static class RequiredArgument {

        private final String value;

        private RequiredArgument(String value) {
            this.value = value;
        }

        /**
         * Retrieves the argument value. Throws an exception if the value is missing.
         *
         * @return the argument value
         * @throws IllegalStateException if the value is null or blank
         */
        public String get() {
            if (value == null || value.isBlank()) {
                throw new IllegalStateException("Required argument is missing");
            }
            return value;
        }

        /**
         * Retrieves the argument value as an integer.
         *
         * @return the integer representation of the argument value
         * @throws NumberFormatException if the value is not a valid integer
         */
        public Integer getAsInt() throws NumberFormatException {
            return Integer.parseInt(get());
        }

    }

    /**
     * Represents an optional argument. Provides default values
     * if the argument is missing.
     */
    public static class OptionalArgument {

        private final String value;

        private OptionalArgument(String value) {
            this.value = value;
        }

        /**
         * Retrieves the argument value if present, or the given default value otherwise.
         *
         * @param defaultVal the default value to return if the argument is missing
         * @return the argument value or the default value
         */

        public String getOrDefault(String defaultVal) {
            if (isValueNullOrBlank()) {
                return defaultVal;
            }
            return value;
        }

        /**
         * Retrieves the argument value as an integer if present,
         * or the given default value otherwise.
         *
         * @param defaultVal the default integer value to return if the argument is missing
         * @return the integer representation of the argument value or the default value
         * @throws NumberFormatException if the value is not a valid integer
         */
        public Integer getAsIntOrDefault(int defaultVal) throws NumberFormatException {
            if (isValueNullOrBlank()) {
                return defaultVal;
            }
            return Integer.parseInt(value);
        }

        /**
         * Checks if the argument value is null or blank.
         *
         * @return true if the value is null or blank, false otherwise
         */
        private boolean isValueNullOrBlank() {
            return value == null || value.isBlank();
        }

    }

}
