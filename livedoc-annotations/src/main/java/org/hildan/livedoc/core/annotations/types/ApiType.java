package org.hildan.livedoc.core.annotations.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;

/**
 * This annotation is to be used on your object classes and represents an object used for communication between clients
 * and server
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiType {

    /**
     * The name of the object, to be referenced by other annotations with an "object" attribute
     *
     * @see ApiRequestBodyType
     * @see ApiResponseBodyType
     */
    String name() default "";

    /**
     * A description of what the object contains or represents
     */
    String description() default "";

    /**
     * Whether to build the json documentation for this object or not. Default value is true
     */
    boolean show() default true;

    /**
     * The logical group (or category) of this object. This allows to group some types together in the documentation.
     * For example, objects like "city", "country", "continent" can be grouped under "Geography", and "author" and
     * "book" under "Library", for a better readability of the whole documentation.
     */
    String group() default "";
}
