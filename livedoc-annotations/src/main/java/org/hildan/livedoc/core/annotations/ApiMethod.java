package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.model.doc.ApiVerb;

/**
 * Indicates that a method is an exposed service of an API, and provides additional documentation details.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {

    String DEFAULT_RESPONSE_STATUS = "200 - OK";

    /**
     * An optional custom identifier to be refer to this method from {@link ApiFlowStep#apiMethodId()}. This string has
     * to be unique inside the Livedoc documentation. It's the responsibility of the documentation writer to guarantee
     * this uniqueness.
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
     * A description of what the method does. It may contain HTML elements.
     */
    String description() default "";

    /**
     * The request verbs allowed for this method.
     */
    ApiVerb[] verbs() default {};

    /**
     * An array of strings representing media types produced by the method, like application/json, application/xml, ...
     */
    String[] produces() default {};

    /**
     * An array of strings representing media types consumed by the method, like application/json, application/xml, ...
     */
    String[] consumes() default {};

    /**
     * Response status code that this method will return to the caller. Defaults to {@value #DEFAULT_RESPONSE_STATUS}.
     */
    String responseStatusCode() default DEFAULT_RESPONSE_STATUS;
}
