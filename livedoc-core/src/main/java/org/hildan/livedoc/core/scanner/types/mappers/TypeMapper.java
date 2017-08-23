package org.hildan.livedoc.core.scanner.types.mappers;

import java.util.Collection;
import java.util.function.Function;

@FunctionalInterface
public interface TypeMapper extends Function<Class<?>, Collection<Class<?>>> {
}
