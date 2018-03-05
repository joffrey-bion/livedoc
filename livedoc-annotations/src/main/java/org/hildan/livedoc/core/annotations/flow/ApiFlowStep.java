package org.hildan.livedoc.core.annotations.flow;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.annotations.ApiOperation;

/**
 * This annotation is to be used inside an annotation of type {@link ApiFlow} and references an operation previously
 * documented with the {@link ApiOperation} annotation, in which the {@link ApiOperation#id()} property is specified.
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFlowStep {

    /**
     * References the operation to perform in this step of the flow
     */
    String apiOperationId();
}
