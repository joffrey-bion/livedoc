package org.hildan.livedoc.core.annotations.messages;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Acts as a container for multiple {@link ApiMessageChannel} annotations.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMessageChannels {

    ApiMessageChannel[] value();
}
