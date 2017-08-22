package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.hildan.livedoc.core.scanner.properties.Property;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;
import org.hildan.livedoc.core.scanner.types.filters.TypeFilter;
import org.reflections.Reflections;

import static java.util.stream.Collectors.toSet;

public class TypesExplorer {

    private final Reflections reflections;

    private final PropertyScanner scanner;

    private final TypeFilter filter;

    public TypesExplorer(Reflections reflections, PropertyScanner scanner, TypeFilter filter) {
        this.reflections = reflections;
        this.scanner = scanner;
        this.filter = filter;
    }

    public Set<Class<?>> findTypes(Set<Type> rootTypes) {
        Set<Class<?>> allTypes = new HashSet<>();
        for (Type type : rootTypes) {
            exploreType(type, allTypes);
        }
        return allTypes;
    }

    private void exploreType(Type type, Set<Class<?>> exploredClasses) {
        for (Class<?> c : getClassesToExplore(type)) {
            exploreClass(c, exploredClasses);
        }
    }

    private void exploreClass(Class<?> clazz, Set<Class<?>> exploredClasses) {
        if (exploredClasses.contains(clazz)) {
            return; // already explored
        }
        exploredClasses.add(clazz);

        Set<Type> propertyTypes = scanner.getProperties(clazz).stream().map(Property::getType).collect(toSet());
        for (Type propType : propertyTypes) {
            exploreType(propType, exploredClasses);
        }
    }

    private Set<Class<?>> getClassesToExplore(Type typeDeclaration) {
        Set<Class<?>> types = GenericTypeExplorer.getClassesInDeclaration(typeDeclaration)
                                                 .stream()
                                                 .filter(filter)
                                                 .collect(toSet());
        replaceInterfacesByImpl(types);
        return types;
    }

    private void replaceInterfacesByImpl(Set<Class<?>> types) {
        Set<Class<?>> interfaces = types.stream().filter(Class::isInterface).collect(toSet());
        types.removeAll(interfaces);
        types.addAll(interfaces.stream().flatMap(this::getSubTypes).collect(toSet()));
    }

    private <T> Stream<Class<? extends T>> getSubTypes(Class<T> interfaceType) {
        return reflections.getSubTypesOf(interfaceType).stream();
    }
}
