package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.ApiVisibility;

/**
 * This annotation is to be used on your exposed methods.
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {

    String DEFAULT_RESPONSE_STATUS = "200 - OK";

    /**
     * A custom identifier to be used inside Livedoc. This string has to be unique inside the Livedoc documentation.
     * It's the responsibility of the documentation writer to guarantee this uniqueness.
     */
    String id() default "";

    /**
     * The relative path for this method (ex. /country/get/{name})
     */
    String[] path() default {};

    /**
     * A summary of what the method does. It's like a short description.
     */
    String summary() default "";

    /**
     * A description of what the method does
     */
    String description() default "";

    /**
     * The request verb for this method. Defaults to "GET"
     *
     * @see ApiVerb
     */
    ApiVerb[] verb() default ApiVerb.UNDEFINED;

    /**
     * An array of strings representing media types produced by the method, like application/json, application/xml, ...
     */
    String[] produces() default {};

    /**
     * An array of strings representing media types consumed by the method, like application/json, application/xml, ...
     */
    String[] consumes() default {};

    /**
     * Response status code that this method will return to the caller. Defaults to 200
     */
    String responsestatuscode() default DEFAULT_RESPONSE_STATUS;

    /**
     * Indicates the visibility of the method
     */
    ApiVisibility visibility() default ApiVisibility.UNDEFINED;

    /**
     * Indicates the stage of development or release
     */
    ApiStage stage() default ApiStage.UNDEFINED;
}
