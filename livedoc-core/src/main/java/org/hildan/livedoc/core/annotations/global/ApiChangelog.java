package org.hildan.livedoc.core.annotations.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used inside an annotation of type ApiChangelogSet
 *
 * @see ApiChangelogSet
 */
@Documented
@Target(value = ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiChangelog {

    /**
     * Api version this changelog refers to
     */
    String version();

    /**
     * List of changes introduced in this api version
     */
    String[] changes();
}
