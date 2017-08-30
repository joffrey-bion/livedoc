package org.hildan.livedoc.core.scanners.types.mappers;

import java.util.Set;
import java.util.function.Function;

@FunctionalInterface
public interface TypeMapper extends Function<Class<?>, Set<Class<?>>> {
}
