package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.model.LivedocDefaultType;

/**
 * Specifies/overrides the expected type for the request body when calling the annotated method. It can also be used on
 * a parameter of a method in order to indicate that it represents the request body, but in that case the type cannot be
 * overridden.
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequestBodyType {

    /**
     * Specify this element if you need to use the {@code ApiRequestBodyType} annotation on the method declaration and
     * not on a parameter. This is to be able to document old style servlets' methods like doGet and doPost.
     * <p>
     * This element, even if specified, is not taken into account when the annotation is used on a parameter.
     */
    Class<?> value() default LivedocDefaultType.class;
}
