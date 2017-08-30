package org.hildan.livedoc.core.scanner.types.mappers;

import java.util.Set;
import java.util.function.Function;

@FunctionalInterface
public interface TypeMapper extends Function<Class<?>, Set<Class<?>>> {
}
