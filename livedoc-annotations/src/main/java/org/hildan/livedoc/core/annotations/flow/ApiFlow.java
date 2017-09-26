package org.hildan.livedoc.core.annotations.flow;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on a method (typically on a class dedicated to flow description) and contains an array
 * of ApiFlowStep
 *
 * @see ApiFlowStep
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFlow {

    /**
     * The name of the flow
     */
    String name();

    /**
     * The description of the flow
     */
    String description() default "";

    /**
     * The preconditions for this flow (example: "the user must be logged", "the user must have an administration
     * role")
     */
    String[] preconditions() default {};

    /**
     * An array of ApiFlowStep annotations
     *
     * @see ApiFlowStep
     */
    ApiFlowStep[] steps() default {};

    /**
     * With this it is possible to specify the logical grouping of this flow. See the same property in the @Api
     * annotation.
     */
    String group() default "";

}
