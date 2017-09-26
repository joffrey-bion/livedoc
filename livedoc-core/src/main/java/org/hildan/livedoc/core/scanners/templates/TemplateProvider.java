package org.hildan.livedoc.core.scanners.templates;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;

public class TemplateProvider {

    private static final Map<Class<?>, Class<?>> primitives = new HashMap<>();

    static {
        primitives.put(boolean.class, Boolean.class);
        primitives.put(byte.class, Byte.class);
        primitives.put(char.class, String.class);
        primitives.put(double.class, Double.class);
        primitives.put(float.class, Float.class);
        primitives.put(int.class, Integer.class);
        primitives.put(long.class, Long.class);
        primitives.put(short.class, Short.class);
        primitives.put(void.class, Void.class);
    }

    private final Map<Class<?>, Object> templates;

    private final PropertyScanner scanner;

    private final Predicate<Class<?>> filter;

    public TemplateProvider(PropertyScanner scanner, Predicate<Class<?>> filter) {
        this.scanner = scanner;
        this.filter = filter;
        this.templates = new HashMap<>();
    }

    public Object getTemplate(Class<?> type) {
        if (!filter.test(type)) {
            return null;
        }
        return getTemplate(type, new HashSet<>());
    }

    private Object getTemplate(Class<?> type, Set<Class<?>> containingClasses) {
        if (containingClasses.contains(type)) {
            // avoid infinite recursion of templates
            return null;
        }
        Object template = templates.get(type);
        if (template != null) {
            return template;
        }
        template = createTemplate(type, containingClasses);
        templates.put(type, template);
        return template;
    }

    private Object createTemplate(Class<?> type, Set<Class<?>> containingClasses) {
        Map<String, Object> objectTemplate = new LinkedHashMap<>();
        containingClasses.add(type);
        for (Property property : scanner.getProperties(type)) {
            Object value = getDefaultValue(property, containingClasses);
            objectTemplate.put(property.getName(), value);
        }
        containingClasses.remove(type);
        return objectTemplate;
    }

    private Object getDefaultValue(Property property, Set<Class<?>> containingClasses) {
        Class<?> type = property.getType();
        if (containingClasses.contains(type)) {
            // avoid infinite recursion of templates
            return null;
        }
        Object defaultValue = property.getDefaultValue();
        if (defaultValue != null) {
            return defaultValue;
        }
        return getDefaultValueForType(type, containingClasses);
    }

    private Object getDefaultValueForType(Class<?> type, Set<Class<?>> containingClasses) {
        if (type.isPrimitive()) {
            return getDefaultValueForType(primitives.get(type), containingClasses);
        }
        if (Boolean.class.isAssignableFrom(type)) {
            return Boolean.TRUE;
        }
        if (Number.class.isAssignableFrom(type)) {
            return 0;
        }
        if (String.class.isAssignableFrom(type)) {
            return "";
        }
        if (type.isEnum()) {
            return getEnumDefaultValue(type);
        }
        if (type.isArray() || Collection.class.isAssignableFrom(type)) {
            return Collections.emptyList();
        }
        if (Map.class.isAssignableFrom(type)) {
            return Collections.emptyMap();
        }
        return getTemplate(type, containingClasses);
    }

    private static <T> String getEnumDefaultValue(Class<T> enumType) {
        T[] values = enumType.getEnumConstants();
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0].toString();
    }
}