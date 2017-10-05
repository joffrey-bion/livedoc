package org.hildan.livedoc.core.scanners.types;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;

/**
 * An implementation of {@link TypeScanner} that reads the types of the properties of the classes, and recursively
 * inspect those types. The definition of a "property" is decided by the given {@link PropertyScanner}.
 */
public class RecursivePropertyTypeScanner implements TypeScanner {

    private final PropertyScanner scanner;

    private Predicate<Class<?>> typeFilter;

    private Predicate<Class<?>> typeExplorationFilter;

    private Function<Class<?>, Set<Class<?>>> typeMapper;

    public RecursivePropertyTypeScanner(PropertyScanner scanner) {
        this.scanner = scanner;
        this.typeFilter = c -> true;
        this.typeExplorationFilter = c -> true;
        this.typeMapper = Collections::singleton;
    }

    public Predicate<Class<?>> getTypeFilter() {
        return typeFilter;
    }

    public void setTypeFilter(Predicate<Class<?>> typeFilter) {
        this.typeFilter = typeFilter;
    }

    public Predicate<Class<?>> getTypeExplorationFilter() {
        return typeExplorationFilter;
    }

    public void setTypeExplorationFilter(Predicate<Class<?>> typeExplorationFilter) {
        this.typeExplorationFilter = typeExplorationFilter;
    }

    public Function<Class<?>, Set<Class<?>>> getTypeMapper() {
        return typeMapper;
    }

    public void setTypeMapper(Function<Class<?>, Set<Class<?>>> mapper) {
        this.typeMapper = mapper;
    }

    @Override
    public Set<Class<?>> findTypes(Collection<? extends Type> rootTypes) {
        Set<Class<?>> allTypes = new HashSet<>();
        for (Type type : rootTypes) {
            exploreType(type, allTypes);
        }
        return allTypes;
    }

    private void exploreType(Type type, Set<Class<?>> exploredClasses) {
        GenericTypeExplorer.getClassesInDeclaration(type)
                           .stream()
                           .filter(typeFilter)
                           .flatMap(typeMapper.andThen(Collection::stream))
                           .distinct()
                           .forEach(c -> exploreClass(c, exploredClasses));
    }

    private void exploreClass(Class<?> clazz, Set<Class<?>> exploredClasses) {
        if (exploredClasses.contains(clazz)) {
            return; // already explored
        }
        exploredClasses.add(clazz);
        if (!typeExplorationFilter.test(clazz)) {
            return; // exploration of the inner properties is disabled for the current type
        }
        scanner.getProperties(clazz)
               .stream()
               .map(Property::getGenericType)
               .distinct()
               .forEach(propType -> exploreType(propType, exploredClasses));
    }

}
