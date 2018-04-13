package org.hildan.livedoc.core.annotations.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation has to be used on a class dedicated to global api documentation. It similar to {@link ApiGlobal}
 * but references a file to process with FreeMarker.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiGlobalTemplate {

    /**
     * A reference to a FreeMarker template, in a format that corresponds to the FreeMarker configuration.
     */
    String value();
}
