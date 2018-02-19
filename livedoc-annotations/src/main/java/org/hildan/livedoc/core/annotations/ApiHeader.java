package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used inside an annotation of type ApiHeaders
 *
 * @see ApiHeaders
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiHeader {

    /**
     * The name of the header parameter
     */
    String name() default "";

    /**
     * A description of what the header is needed for
     */
    String description() default "";

    /**
     * An array representing the allowed values this header can have.
     */
    String[] allowedValues() default {"*"};
}
