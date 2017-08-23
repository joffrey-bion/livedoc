package org.hildan.livedoc.core.scanner.types.filters;

import java.util.function.Predicate;

@FunctionalInterface
public interface TypeFilter extends Predicate<Class<?>> {
}
