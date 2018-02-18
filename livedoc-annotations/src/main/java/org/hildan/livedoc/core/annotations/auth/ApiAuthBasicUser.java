package org.hildan.livedoc.core.annotations.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used inside an annotation of type ApiAuthBasic. It lets you specify username and password
 * for users that can/cannot access the annotated controller/method.
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAuthBasicUser {

    /**
     * The test user's username
     */
    String username();

    /**
     * The test user's password
     */
    String password();
}
