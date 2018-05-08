package org.hildan.livedoc.core.annotations.messages;

/**
 * Defines the type of action performed using a message.
 */
public enum StompCommand {
    /**
     * Describes when the user sends a message on a channel.
     */
    SEND,
    /**
     * Describes when the user subscribes to the channel to receive messages.
     */
    SUBSCRIBE
}
