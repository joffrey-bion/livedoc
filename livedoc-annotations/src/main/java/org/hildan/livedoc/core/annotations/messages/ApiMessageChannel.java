package org.hildan.livedoc.core.annotations.messages;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiPathParam;

/**
 * Describes a channel on which messages can be sent. Either the user can send messages or she can subscribe to the
 * channel and receive messages (e.g. websocket STOMP messages).
 */
@Documented
@Repeatable(ApiMessageChannels.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMessageChannel {

    /**
     * The livedoc ID of this message channel. This ID is used to uniquely identify this channel across the whole doc.
     * The default generated one is usually sufficient, but this override can be used to customize it.
     */
    String id() default "";

    /**
     * A name for the messages sent on this channel. Imagine that a function to subscribe to this channel could be
     * called "watchXxx()", where "xxx" is the name specified here.
     */
    String name() default "";

    /**
     * A single-sentence description of the messages exchanged on this channel.
     */
    String summary() default "";

    /**
     * A description of the messages exchanged on this channel.
     */
    String description() default "";

    /**
     * The kind of action documented by this annotation on this channel. See {@link StompCommand} for more details.
     */
    StompCommand command() default StompCommand.SUBSCRIBE;

    /**
     * The destination(s) (topic) of the messages of this channel.
     */
    String[] destinations();

    /**
     * The destination variables used in the {@link #destinations()}.
     */
    ApiPathParam[] destinationVariables() default {};

    /**
     * The headers present in the messages of this channel.
     */
    ApiHeader[] headers() default {};

    /**
     * The type of the payload of the messages on this channel.
     */
    Class<?> payloadType();
}
