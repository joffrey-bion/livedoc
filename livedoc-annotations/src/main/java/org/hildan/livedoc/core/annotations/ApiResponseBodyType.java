package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.LivedocDefaultType;

/**
 * Specifies/overrides the type of the response body when calling the annotated method. This is particularly useful when
 * using old style servlets which return void for methods like doGet and doPost.
 *
 * @see ApiType
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponseBodyType {

    /**
     * The type of the response body.
     */
    Class<?> value() default LivedocDefaultType.class;
}
