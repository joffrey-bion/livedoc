package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the version range for which the annotated element is supported.
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {

    /**
     * The version in which the annotated method/object/class was introduced.
     */
    String since();

    /**
     * The last version supporting the annotated method/object/class.
     */
    String until() default "";
}
