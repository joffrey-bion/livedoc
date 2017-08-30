package org.hildan.livedoc.core.scanners.types.filters;

import java.util.function.Predicate;

@FunctionalInterface
public interface TypeFilter extends Predicate<Class<?>> {
}
