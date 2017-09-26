package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.pojo.LivedocDefaultType;

/**
 * This annotation is to be used on your method and represents the returned value
 *
 * @see ApiObject
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponseObject {

    /**
     * Specify this element if are using old style servlets which return void for methods like doGet and doPost
     */
    Class<?> clazz() default LivedocDefaultType.class;
}
