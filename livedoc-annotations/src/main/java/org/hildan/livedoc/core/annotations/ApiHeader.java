package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a header that is expected in a request to map to a specific API operation. This annotation should be used
 * inside {@link ApiHeaders}, or on a method parameter that receives the value of a request header.
 *
 * @see ApiHeaders
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiHeader {

    /**
     * The name of the header
     */
    String name() default "";

    /**
     * A description of what the header is needed for
     */
    String description() default "";

    /**
     * An optional array representing the allowed values this header can have. By default, any value is accepted.
     */
    String[] allowedValues() default {"*"};
}
