package org.hildan.livedoc.core.annotations.messages;

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
