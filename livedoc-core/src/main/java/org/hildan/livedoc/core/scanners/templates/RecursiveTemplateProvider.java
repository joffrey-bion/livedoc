package org.hildan.livedoc.core.scanners.templates;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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

    private final Map<Type, Object> templates;

    private final PropertyScanner scanner;

    private final Predicate<Type> filter;

    public RecursiveTemplateProvider(PropertyScanner scanner, Predicate<Type> filter) {
        this(scanner, filter, new HashMap<>());
    }

    public RecursiveTemplateProvider(PropertyScanner scanner, Predicate<Type> filter,
            Map<Type, Object> defaultTemplates) {
        this.scanner = scanner;
        this.filter = filter;
        this.templates = new HashMap<>();
        templates.putAll(simpleDefaultValues);
        templates.putAll(defaultTemplates);
    }

    @Override
    public Object getTemplate(Type type) {
        return getTemplate(type, new HashSet<>());
    }

    private Object getTemplate(Type type, Set<Type> containingClasses) {
        if (type == null || !filter.test(type)) {
            return null;
        }
        if (containingClasses.contains(type)) {
            // avoid infinite recursion of templates
            return null;
        }
        Object template = templates.get(type);
        if (template != null) {
            return template;
        }
        template = createExample(type, containingClasses);
        templates.put(type, template);
        return template;
    }

    private Object createExample(Type type, Set<Type> containingClasses) {
        if (type instanceof WildcardType || type instanceof TypeVariable) {
            // simulate an empty object (without properties) because we have no information
            return Collections.emptyMap();
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Object exampleElement = getTemplate(componentType, containingClasses);
            return Collections.singletonList(exampleElement);
        }
        if (type instanceof ParameterizedType) {
            return createParameterizedTypeExample((ParameterizedType) type, containingClasses);
        }
        assert type instanceof Class : "Unknown type category " + type.getClass();
        Class<?> clazz = (Class<?>) type;
        return createClassExample(clazz, containingClasses);
    }

    private Object createParameterizedTypeExample(ParameterizedType type, Set<Type> containingClasses) {
        Class<?> rawType = (Class<?>) type.getRawType();
        Type[] typeParams = type.getActualTypeArguments();

        if (Collection.class.isAssignableFrom(rawType)) {
            Type componentType = typeParams[0];
            Object exampleElement = getTemplate(componentType, containingClasses);
            return Collections.singletonList(exampleElement);
        }
        if (Map.class.isAssignableFrom(rawType)) {
            Type keyType = typeParams[0];
            Type valueType = typeParams[1];
            Object exampleKey = getTemplate(keyType, containingClasses);
            Object exampleValue = getTemplate(valueType, containingClasses);
            return Collections.singletonMap(exampleKey, exampleValue);
        }
        return createBeanExample(type, rawType, containingClasses);
    }

    private Object createClassExample(Class<?> type, Set<Type> containingClasses) {
        Object defaultValueOfParentType = getDefaultValueOfParentType(type);
        if (defaultValueOfParentType != null) {
            return defaultValueOfParentType;
        }
        if (type.isEnum()) {
            return getEnumDefaultValue(type);
        }
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            Object exampleElement = getTemplate(componentType, containingClasses);
            return Collections.singletonList(exampleElement);
        }
        // raw Collection
        if (Collection.class.isAssignableFrom(type)) {
            return Collections.emptyList();
        }
        // raw Map
        if (Map.class.isAssignableFrom(type)) {
            return Collections.emptyMap();
        }
        return createBeanExample(type, type, containingClasses);
    }

    private Object createBeanExample(Type type, Class<?> rawType, Set<Type> containingClasses) {
        Map<String, Object> objectTemplate = new LinkedHashMap<>();
        containingClasses.add(type);
        for (Property property : scanner.getProperties(rawType)) {
            Object value = getPropertyExample(property, containingClasses);
            objectTemplate.put(property.getName(), value);
        }
        containingClasses.remove(type);
        return objectTemplate;
    }

    private Object getPropertyExample(Property property, Set<Type> containingClasses) {
        Type type = property.getGenericType();
        if (containingClasses.contains(type)) {
            // avoid infinite recursion of templates
            return null;
        }
        Object defaultValue = property.getDefaultValue();
        if (defaultValue != null) {
            return defaultValue;
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
