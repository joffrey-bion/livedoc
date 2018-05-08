package org.hildan.livedoc.core.readers.combined;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;
import org.hildan.livedoc.core.util.Defaults;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component able to merge doc objects of the same type into one. This is allows to build a single doc object from
 * the result of multiple readers.
 */
public class DocMerger {

    private final PropertyScanner propScanner;

    public DocMerger() {
        // Note that merging doc objects has nothing to do with the user-defined property scanner for API types
        this.propScanner = new FieldPropertyScanner();
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

    @NotNull
    public <T extends Comparable<T>, K> List<T> mergeAndSort(List<T> base, List<T> overrides,
            Function<T, K> keyExtractor) {
        List<T> overridden = mergeLists(base, overrides, keyExtractor);
        Collections.sort(overridden);
        return overridden;
    }

    @NotNull
    public <T, K> List<T> mergeLists(List<T> base, List<T> overrides, Function<T, K> keyExtractor) {
        List<T> basePool = new ArrayList<>(base);
        basePool.addAll(overrides);
        return mergeList(basePool, keyExtractor);
    }

    @NotNull
    public <T, K> List<T> mergeList(List<T> list, Function<T, K> keyExtractor) {
        List<T> mergedList = new ArrayList<>();
        for (T o : list) {
            K key = keyExtractor.apply(o);
            Optional<T> match = takeMatch(mergedList, keyExtractor, key);
            T merged = match.map(b -> merge(b, o)).orElse(o);
            mergedList.add(merged);
        }
        return mergedList;
    }

    private static <T, K> Optional<T> takeMatch(List<T> basePool, Function<T, K> keyExtractor, K key) {
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
