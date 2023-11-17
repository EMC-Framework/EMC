package me.deftware.client.framework.message;

import java.util.function.Function;

/**
 * Represents the chat hud, and allows for appending and removing messages.
 *
 * @author Deftware
 */
public interface GameChat {

    /**
     * Appends a message to the bottom of the chat
     *
     * @param message The message to append
     */
    void append(Message message);

    /**
     * Visits all visible messages, and allows removing them.
     *
     * @param visitor Visitor function to apply
     */
    void remove(Function<String, Boolean> visitor);

    /**
     * Removes a message at a specific index
     *
     * @param index The index of the message to remove
     */
    void remove(int index);

}
