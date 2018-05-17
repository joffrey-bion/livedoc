package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.model.doc.ApiVerb;

/**
 * Marks a Java method as an API operation, and provides additional documentation details.
 * <p>
 * This annotation is not necessary for a method to be understood as an API operation, this depends on the
 * configuration. For instance, in the context of a Spring application, scanners are defined to look for request
 * mappings, in which case Spring annotations are sufficient. That being said, this annotation can be used to provide
 * additional documentation information, or override automatically-generated pieces of the documentation.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {

    String DEFAULT_RESPONSE_STATUS = "200 - OK";

    /**
     * The HTTP methods mapped to this operation.
     */
    ApiVerb[] verbs() default {};

    /**
     * The relative path(s) that map to this operation. They may contain path variables between braces. For instance:
     * {@code /country/get/{name}}
     */
    String[] path() default {};

    /**
     * A single-sentence summary of what this operation does. It's like a short description.
     */
    String summary() default "";

    /**
     * A description of what the method does. It may contain HTML elements.
     */
    String description() default "";

    /**
     * The media types that can be produced by this operation as response body, e.g. {@code application/json}.
     */
    String[] produces() default {};

    /**
     * The media types supported by this operation in incoming requests, e.g. {@code application/json}.
     */
    String[] consumes() default {};

    /**
     * The response status code that this method will return to the caller. Defaults to {@value
     * #DEFAULT_RESPONSE_STATUS}.
     */
    String responseStatusCode() default DEFAULT_RESPONSE_STATUS;
}
