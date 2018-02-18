package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on your "service" class, for example controller classes in Spring MVC.
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
