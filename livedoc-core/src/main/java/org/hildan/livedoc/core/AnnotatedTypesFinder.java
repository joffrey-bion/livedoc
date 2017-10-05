package org.hildan.livedoc.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

/**
 * Just an alias for a {@link Function} that returns a collection of classes with a given annotation.
 */
public interface AnnotatedTypesFinder extends Function<Class<? extends Annotation>, Collection<Class<?>>> {}
