package org.hildan.livedoc.core.scanners.properties;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

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
    public List<Property> getProperties(Class<?> type) {
        return scanner.getProperties(type)
                      .stream()
                      .map(LivedocPropertyScannerWrapper::overrideProperty)
                      .sorted(Comparator.comparing(Property::getOrder).thenComparing(Property::getName))
                      .collect(Collectors.toList());
    }

    private static Property overrideProperty(Property property) {
        AccessibleObject accessibleObject = property.getAccessibleObject();
        ApiTypeProperty ann = accessibleObject.getAnnotation(ApiTypeProperty.class);
        if (ann == null) {
            return property; // no override
        }
        String name = ann.name().isEmpty() ? property.getName() : ann.name();
        int order = ann.order() == Integer.MAX_VALUE ? property.getOrder() : ann.order();
        Class<?> type = property.getType();
        Type genericType = property.getGenericType();

        Property overridden = new Property(name, type, genericType, accessibleObject);
        overridden.setOrder(order);
        overridden.setRequired(property.isRequired());
        return overridden;
    }
}
