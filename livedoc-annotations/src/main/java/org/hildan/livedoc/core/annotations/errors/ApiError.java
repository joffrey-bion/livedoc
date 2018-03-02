package org.hildan.livedoc.core.annotations.errors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes an error that can be returned by an API endpoint, it should be used inside an {@link ApiErrors}.
 * <p>
 * When the {@link @ApiErrors} container is used at the class level, this error will be applied to all member methods.
 * Member methods can override this error if they declare the same code in their own list.
 *
 * @see ApiError
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiError {

    /**
     * The error code returned
     */
    String code();

    /**
     * A description of what the error code means
     */
    String description();
}
