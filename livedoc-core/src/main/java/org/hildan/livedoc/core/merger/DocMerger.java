package org.hildan.livedoc.core.merger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;
import org.hildan.livedoc.core.util.Defaults;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DocMerger {

    private final PropertyScanner propScanner;

    public DocMerger(PropertyScanner propScanner) {
        this.propScanner = propScanner;
    }

    /**
     * Overrides each property of the given target if the given source has a meaningful (non default) value for the
     * property.
     *
     * @param <T>
     *         the type of objects to manipulate
     * @param base
     *         the object to set properties on (if there is something meaningful to put in place of the previous value)
     * @param override
     *         the object to read the property values from
     */
    @Contract("!null, !null -> !null; null, null -> null")
    public <T> T merge(@Nullable T base, @Nullable T override) {
        return merge(base, override, null);
    }

    @Contract("!null, !null, _ -> !null; null, null, _ -> null")
    public <T> T merge(@Nullable T base, @Nullable T override, @Nullable T neutralValue) {
        if (base == null || base.equals(neutralValue)) {
            return override;
        }
        if (override == null || override.equals(neutralValue)) {
            return base;
        }
        if (TypePredicates.isContainer(override.getClass()) && isEmptyContainer(override)) {
            return base;
        }
        if (base instanceof Mergeable) {
            @SuppressWarnings("unchecked")
            Mergeable<T> mergeableTarget = (Mergeable<T>) base;
            return mergeableTarget.merge(override, this);
        }
        return override;
    }

    public <T> T mergeProperties(@NotNull T base, @NotNull T override, @NotNull T emptyTarget) {
        List<Property> properties = propScanner.getProperties(override.getClass());
        for (Property prop : properties) {
            Field field = (Field) prop.getAccessibleObject();
            mergeFieldAndSet(base, override, emptyTarget, field);
        }
        return emptyTarget;
    }

    private <T> void mergeFieldAndSet(@NotNull T base, @NotNull T override, @NotNull T emptyTarget, Field field) {
        try {
            Object mergedValue = mergeField(field, base, override);
            field.set(emptyTarget, mergedValue);
        } catch (IllegalAccessException e) {
            String msg = String.format("Could not copy field '%s' of the given object of type %s", field.getName(),
                    override.getClass().getSimpleName());
            throw new FieldCopyException(msg, e);
        }
    }

    private Object mergeField(Field field, Object base, Object override) throws IllegalAccessException {
        field.setAccessible(true);
        Object baseValue = field.get(base);
        Object overridingValue = field.get(override);
        Object neutralValue = getNeutralValueFor(field);
        return merge(baseValue, overridingValue, neutralValue);
    }

    private static boolean isEmptyContainer(@NotNull Object container) {
        if (container instanceof Collection) {
            return ((Collection) container).isEmpty();
        }
        if (container instanceof Map) {
            return ((Map) container).isEmpty();
        }
        if (container.getClass().isArray()) {
            return ((Object[]) container).length == 0;
        }
        return false;
    }

    private static Object getNeutralValueFor(Field field) {
        SpecialDefaultStringValue specialDefaultStr = field.getAnnotation(SpecialDefaultStringValue.class);
        if (specialDefaultStr != null) {
            return specialDefaultStr.value();
        }
        SpecialDefaultIntValue specialDefaultInt = field.getAnnotation(SpecialDefaultIntValue.class);
        if (specialDefaultInt != null) {
            return specialDefaultInt.value();
        }
        return Defaults.defaultValueFor(field.getType());
    }

    public <T, K> List<T> mergeList(List<T> base, List<T> overrides, Function<T, K> keyExtractor) {
        List<T> basePool = new ArrayList<>(base);
        List<T> overridden = new ArrayList<>();
        for (T o : overrides) {
            K key = keyExtractor.apply(o);
            Optional<T> match = takeMatch(basePool, keyExtractor, key);
            T merged = match.map(b -> merge(b, o)).orElse(o);
            overridden.add(merged);
        }
        overridden.addAll(basePool);
        if (!overridden.isEmpty() && overridden.get(0) instanceof Comparable) {
            //noinspection unchecked
            Collections.sort((List<Comparable>) overridden);
        }
        return overridden;
    }

    private <T, K> Optional<T> takeMatch(List<T> basePool, Function<T, K> keyExtractor, K key) {
        Optional<T> match = basePool.stream().filter(b -> Objects.equals(keyExtractor.apply(b), key)).findAny();
        match.ifPresent(basePool::remove);
        return match;
    }

    public static class FieldCopyException extends RuntimeException {
        FieldCopyException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
