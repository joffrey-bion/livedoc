package org.hildan.livedoc.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on your "service" class, for example controllers, or on exposed methods. It specifies
 * that the controller/method needs token authentication.
 */
@Documented
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAuthToken {

    /**
     * The role(s) a user must have to correctly access the annotated controller/method. Defaults to '*'
     */
    String[] roles() default {"*"};

    /**
     * A list of test tokens that can be used to test methods
     */
    String[] testtokens() default {};

    /**
     * The default scheme placed at the beginning of the Authorization header
     */
    String scheme() default "";
}
