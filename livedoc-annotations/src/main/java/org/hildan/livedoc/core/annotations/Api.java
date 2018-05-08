package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as an API resource/controller, and provides additional documentation details.
 * <p>
 * An {@code Api}-annotated class defines a group of API operations that interact with similar parts of the system. It
 * usually corresponds to a REST resource. In the context of a Spring application, it usually corresponds to a
 * controller.
 * <p>
 * The {@code Api} annotation is not necessary for a class to be scanned, this depends on the configuration. For
 * instance, in the context of a Spring application, scanners are defined to look for Spring controllers, and Spring
 * annotations are sufficient. That being said, this annotation can be used to provide additional documentation
 * information, or override automatically-generated pieces of the documentation.
 * <p>
 * The API operations defined in this class are logically grouped in the doc. They correspond to some of the Java
 * methods of this class (and/or the parent classes). Whether or not the methods are interpreted as API operations is
 * defined by the configuration. To manually add a method as an API operation, use the {@link ApiOperation} annotation.
 * <p>
 * {@code Api}-annotated classes define groups of API operations, but can also be grouped themselves, which is what the
 * {@link #group()} attribute controls.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

    /**
     * The name of the API
     */
    String name() default "";

    /**
     * A description of what this API allows, usually describing the resource affected by the operations in this API.
     */
    String description() default "";

    /**
     * A user-defined logical group to which this API belongs. For example, if you have APIs like city services or
     * country services, you can group them in the "Geography" group, while if you have author services and book
     * services you can group them in the "Library" group.
     */
    String group() default "";
}
