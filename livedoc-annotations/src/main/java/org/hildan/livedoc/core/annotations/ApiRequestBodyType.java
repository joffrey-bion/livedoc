package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.LivedocDefaultType;

/**
 * This annotation is to be used on your method and represents the body object passed the request.
 *
 * @see ApiType
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequestBodyType {

    /**
     * Specify this element if you need to use the ApiRequestBodyType annotation on the method declaration and not
     * inside the method's signature. This is to be able to document old style servlets' methods like doGet and doPost.
     * This element, even if specified, is not taken into account when the annotation is put inside the method's
     * signature.
     */
    Class<?> value() default LivedocDefaultType.class;
}
