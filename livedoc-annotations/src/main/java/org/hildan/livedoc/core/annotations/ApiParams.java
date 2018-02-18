package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on your method and contains the path and query parameters to document.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParams {

    /**
     * An array of {@link ApiQueryParam} annotations
     */
    ApiQueryParam[] queryParams() default {};

    /**
     * An array of {@link ApiPathParam} annotations
     */
    ApiPathParam[] pathParams() default {};
}
