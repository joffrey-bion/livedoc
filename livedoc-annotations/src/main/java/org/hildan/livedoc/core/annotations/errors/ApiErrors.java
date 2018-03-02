package org.hildan.livedoc.core.annotations.errors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the possible errors returned by the API endpoints, or by one method in particular.
 * <p>
 * When the {@code @ApiErrors} is present at the class level, the defined errors will be applied to all member methods.
 * Member methods can override the errors declared at the class level and provide more specific ones.
 *
 * @see ApiError
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrors {

    /**
     * An array of {@link ApiError}s
     */
    ApiError[] value();
}
