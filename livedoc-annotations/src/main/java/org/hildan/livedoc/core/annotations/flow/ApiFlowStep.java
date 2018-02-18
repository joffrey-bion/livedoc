package org.hildan.livedoc.core.annotations.flow;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.ApiMethod;

/**
 * This annotation is to be used inside an annotaion of type {@link ApiFlow} and references a method previously
 * documented with the {@link ApiMethod} annotation in which the "id" property is specified
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFlowStep {

    /**
     * The api method identified by this id is the method used in the flow step
     */
    String apiMethodId();
}
