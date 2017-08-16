package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hildan.livedoc.core.scanner.types.filters.TypeFilter;
import org.hildan.livedoc.core.scanner.types.records.RecordTypesScanner;
import org.reflections.Reflections;

public class TypesExplorer {

    private final Reflections reflections;

    private final RecordTypesScanner scanner;

    private final TypeFilter filter;

    public TypesExplorer(Reflections reflections, RecordTypesScanner scanner, TypeFilter filter) {
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
        for (Type propType : scanner.getFieldTypes(clazz)) {
            exploreType(propType, exploredClasses);
        }
    }

    private Set<Class<?>> getClassesToExplore(Type typeDeclaration) {
        Set<Class<?>> types = GenericTypeExplorer.getClassesInDeclaration(typeDeclaration)
                                                 .stream()
                                                 .filter(filter)
                                                 .collect(Collectors.toSet());
        replaceInterfacesByImpl(types);
        return types;
    }

    private void replaceInterfacesByImpl(Set<Class<?>> types) {
        Set<Class<?>> interfaces = types.stream().filter(Class::isInterface).collect(Collectors.toSet());
        types.removeAll(interfaces);
        types.addAll(interfaces.stream().flatMap(this::getSubTypes).collect(Collectors.toSet()));
    }

    private <T> Stream<Class<? extends T>> getSubTypes(Class<T> interfaceType) {
        return reflections.getSubTypesOf(interfaceType).stream();
    }
}
