package org.hildan.livedoc.core.scanners.templates;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;

public class RecursiveTemplateProvider implements TemplateProvider {

    private static final Map<Class<?>, Object> simpleDefaultValues = new HashMap<>();

    private static final Map<Class<?>, Object> defaultValuesOfParentTypes = new HashMap<>();

    static {
        simpleDefaultValues.put(boolean.class, true);
        simpleDefaultValues.put(Boolean.class, Boolean.TRUE);
        simpleDefaultValues.put(byte.class, 0);
        simpleDefaultValues.put(Byte.class, 0);
        simpleDefaultValues.put(short.class, 0);
        simpleDefaultValues.put(Short.class, 0);
        simpleDefaultValues.put(int.class, 0);
        simpleDefaultValues.put(Integer.class, 0);
        simpleDefaultValues.put(long.class, 0L);
        simpleDefaultValues.put(Long.class, 0L);
        simpleDefaultValues.put(float.class, 0F);
        simpleDefaultValues.put(Float.class, 0F);
        simpleDefaultValues.put(double.class, 0d);
        simpleDefaultValues.put(Double.class, 0d);
        simpleDefaultValues.put(char.class, ' ');
        simpleDefaultValues.put(Character.class, ' ');
        simpleDefaultValues.put(String.class, "");
        simpleDefaultValues.put(Date.class, new Date());

        defaultValuesOfParentTypes.put(CharSequence.class, "");
        defaultValuesOfParentTypes.put(TemporalAccessor.class, Instant.now());
        defaultValuesOfParentTypes.put(TemporalAmount.class, Duration.ofSeconds(5));
    }

    private final Map<Class<?>, Object> templates;

    private final PropertyScanner scanner;

    private final Predicate<Class<?>> filter;

    public RecursiveTemplateProvider(PropertyScanner scanner, Predicate<Class<?>> filter) {
        this(scanner, filter, new HashMap<>());
    }

    public RecursiveTemplateProvider(PropertyScanner scanner, Predicate<Class<?>> filter,
                                     Map<Class<?>, Object> defaultTemplates) {
        this.scanner = scanner;
        this.filter = filter;
        this.templates = new HashMap<>();
        templates.putAll(simpleDefaultValues);
        templates.putAll(defaultTemplates);
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
        template = createTemplateWithProperties(type, containingClasses);
        templates.put(type, template);
        return template;
    }

    private Object createTemplateWithProperties(Class<?> type, Set<Class<?>> containingClasses) {
        Map<String, Object> objectTemplate = new LinkedHashMap<>();
        containingClasses.add(type);
        for (Property property : scanner.getProperties(type)) {
            Object value = getExampleValue(property, containingClasses);
            objectTemplate.put(property.getName(), value);
        }
        containingClasses.remove(type);
        return objectTemplate;
    }

    private Object getExampleValue(Property property, Set<Class<?>> containingClasses) {
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
        Object defaultValueOfParentType = getDefaultValueOfParentType(type);
        if (defaultValueOfParentType != null) {
            return defaultValueOfParentType;
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

    private static Object getDefaultValueOfParentType(Class<?> type) {
        for (Entry<Class<?>, Object> parentTypeDefault : defaultValuesOfParentTypes.entrySet()) {
            if (parentTypeDefault.getKey().isAssignableFrom(type)) {
                return parentTypeDefault.getValue();
            }
        }
        return null;
    }
}
