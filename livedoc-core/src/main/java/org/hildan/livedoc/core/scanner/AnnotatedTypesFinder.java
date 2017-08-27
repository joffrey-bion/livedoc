package org.hildan.livedoc.core.scanner;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

public interface AnnotatedTypesFinder extends Function<Class<? extends Annotation>, Collection<Class<?>>> {}
