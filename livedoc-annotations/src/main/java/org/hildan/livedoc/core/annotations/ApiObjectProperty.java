package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on your objects' fields and represents a field of an object
 */
@Documented
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiObjectProperty {

    /**
     * A description of the property
     */
    String description() default "";

    /**
     * The format pattern for this property (mostly relevant for string properties)
     */
    String format() default "";

    /**
     * The allowed values for this property
     */
    String[] allowedvalues() default {};

    /**
     * Whether the property is required, defaults to false.
     */
    boolean required() default false;

    /**
     * The display name for this property, if different from the java name.
     */
    String name() default "";

    /**
     * Position of property in the generated example, smaller is first. Defaults to {@link Integer#MAX_VALUE}.
     */
    int order() default Integer.MAX_VALUE;
}
