package org.hildan.livedoc.core.annotations.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used inside an annotation of type ApiMigrationSet
 *
 * @see ApiMigrationSet
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMigration {

    /**
     * Source api version
     */
    String fromversion();

    /**
     * Target api version
     */
    String toversion();

    /**
     * Steps needed to migrate from source api version to target api version
     */
    String[] steps();
}
