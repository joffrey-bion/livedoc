package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class contains API methods that should be inspected, and provides documentation details about the
 * API defined by that class.
 * <p>
 * Livedoc's "APIs" are groups of API methods that interact with similar parts of the system. An API usually corresponds
 * to a controller, especially in the context of a Spring application. APIs can themselves be grouped, which is what
 * the {@link #group()} attribute controls.
 * <p>
 * Whether or not each method of this class (and the parent classes) is actually included in the doc depends on the
 * configuration. These methods are logically grouped in the doc because they belong to the same "API", defined by
 * the {@link Api}-annotated class.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

    /**
     * A description of what the API does
     */
    String description();

    /**
     * The name of the API
     */
    String name();

    /**
     * With this it is possible to specify the logical grouping of this API. For example, if you have APIs like city
     * services or country services, you can group them in the "Geography" group, while if you have author services and
     * book services you can group them in the "Library" group.
     */
    String group() default "";
}
