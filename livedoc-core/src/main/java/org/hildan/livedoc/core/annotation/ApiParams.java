package org.hildan.livedoc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on your method and contains an array of ApiParam
 *
 * @author Fabio Maffioletti
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParams {

    /**
     * An array of ApiQueryParam annotations
     */
    public ApiQueryParam[] queryparams() default {};

    /**
     * An array of ApiPathParam annotations
     */
    public ApiPathParam[] pathparams() default {};

}
