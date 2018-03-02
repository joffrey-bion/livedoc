package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.model.LivedocDefaultType;

/**
 * Describes a path parameter of the request. This annotation can either be used on an actual method parameter, or
 * inside an {@link ApiParams} annotation on the method.
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPathParam {

    /**
     * The name of the url parameter, as expected by the server
     */
    String name() default "";

    /**
     * A description of what the parameter is needed for
     */
    String description() default "";

    /**
     * An array representing the allowed values this parameter can have.
     */
    String[] allowedValues() default {};

    /**
     * The format from the parameter (ex. yyyy-MM-dd HH:mm:ss, ...)
     */
    String format() default "";

    /**
     * Specify this element if you need to use the ApiParam annotation on the method declaration and not on the method
     * parameter. This is to be able to document old style servlets' methods like doGet and doPost. This element, even
     * if specified, is not taken into account when the annotation is put inside the method's signature.
     */
    Class<?> type() default LivedocDefaultType.class;
}
