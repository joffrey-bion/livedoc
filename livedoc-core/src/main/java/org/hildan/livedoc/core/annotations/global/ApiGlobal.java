package org.hildan.livedoc.core.annotations.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation has to be used on a class dedicated to global api documentation
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiGlobal {

    /**
     * An array of {@link ApiGlobalSection} annotations
     */
    ApiGlobalSection[] sections();

}
