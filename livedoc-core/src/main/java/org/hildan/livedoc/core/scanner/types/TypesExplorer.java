package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hildan.livedoc.core.scanner.properties.Property;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;
import org.hildan.livedoc.core.scanner.types.filters.BasicTypesExcludingFilter;
import org.hildan.livedoc.core.scanner.types.filters.ContainerTypesExcludingFilter;
import org.hildan.livedoc.core.scanner.types.filters.TypeFilter;
import org.hildan.livedoc.core.scanner.types.mappers.TypeMapper;

public class TypesExplorer {

    private final PropertyScanner scanner;

    private TypeFilter filter;

    private TypeFilter explorationFilter;

    private TypeMapper mapper;

    public TypesExplorer(PropertyScanner scanner) {
        this.scanner = scanner;
        this.filter = new ContainerTypesExcludingFilter();
        this.explorationFilter = new BasicTypesExcludingFilter();
        this.mapper = Collections::singleton;
    }

    public void setFilter(TypeFilter filter) {
        this.filter = filter;
    }

    public void setExplorationFilter(TypeFilter explorationFilter) {
        this.explorationFilter = explorationFilter;
    }

    public void setMapper(TypeMapper mapper) {
        this.mapper = mapper;
    }

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
                           .filter(filter)
                           .flatMap(mapper.andThen(Collection::stream))
                           .distinct()
                           .forEach(c -> exploreClass(c, exploredClasses));
    }

    private void exploreClass(Class<?> clazz, Set<Class<?>> exploredClasses) {
        if (exploredClasses.contains(clazz)) {
            return; // already explored
        }
        exploredClasses.add(clazz);
        if (!explorationFilter.test(clazz)) {
            return; // exploration of the inner properties is disabled for the current type
        }
        scanner.getProperties(clazz)
               .stream()
               .map(Property::getType)
               .distinct()
               .forEach(propType -> exploreType(propType, exploredClasses));
    }

}
