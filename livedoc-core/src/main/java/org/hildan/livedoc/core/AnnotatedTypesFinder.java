package org.hildan.livedoc.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

public interface AnnotatedTypesFinder extends Function<Class<? extends Annotation>, Collection<Class<?>>> {}
