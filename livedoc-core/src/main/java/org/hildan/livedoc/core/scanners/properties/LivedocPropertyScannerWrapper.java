package org.hildan.livedoc.core.scanners.properties;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PropertyScanner} that reads properties from a wrapped {@link PropertyScanner} and overrides some details
 * based on the {@link ApiTypeProperty} annotation.
 */
public class LivedocPropertyScannerWrapper implements PropertyScanner {

    private final PropertyScanner scanner;

    public LivedocPropertyScannerWrapper(PropertyScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Property> getProperties(Type type) {
        return scanner.getProperties(type)
                      .stream()
                      .map(LivedocPropertyScannerWrapper::overrideProperty)
                      .sorted(Comparator.comparing(Property::getOrder).thenComparing(Property::getName))
                      .collect(Collectors.toList());
    }

    private static Property overrideProperty(Property property) {
        Property overridden = property;
        Field field = property.getField();
        if (field != null) {
            overridden = overrideFromMember(overridden, field);
        }
        Method getter = property.getGetter();
        if (getter != null) {
            overridden = overrideFromMember(overridden, getter);
        }
        return overridden;
    }

    private static Property overrideFromMember(Property property, AnnotatedElement annotatedElement) {
        ApiTypeProperty ann = annotatedElement.getAnnotation(ApiTypeProperty.class);
        if (ann == null) {
            return property; // no override
        }
        return overrideFromAnnotation(property, ann);
    }

    @NotNull
    private static Property overrideFromAnnotation(Property property, ApiTypeProperty ann) {
        String name = ann.name().isEmpty() ? property.getName() : ann.name();
        int order = ann.order() == Integer.MAX_VALUE ? property.getOrder() : ann.order();
        Class<?> type = property.getType();
        Type genericType = property.getGenericType();

        Property overridden = new Property(name, type, genericType, property.getAccessibleObject());
        overridden.setOrder(order);
        overridden.setRequired(property.isRequired());
        overridden.setField(property.getField());
        overridden.setGetter(property.getGetter());
        return overridden;
    }
}
