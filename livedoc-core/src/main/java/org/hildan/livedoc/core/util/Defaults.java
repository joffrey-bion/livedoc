package org.hildan.livedoc.core.util;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public class Defaults {

    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();

    static {
        primitiveDefaults.put(boolean.class, false);
        primitiveDefaults.put(byte.class, 0);
        primitiveDefaults.put(short.class, 0);
        primitiveDefaults.put(int.class, 0);
        primitiveDefaults.put(long.class, 0L);
        primitiveDefaults.put(float.class, 0F);
        primitiveDefaults.put(double.class, 0d);
        primitiveDefaults.put(char.class, '\0');
    }

    public static Object defaultValueFor(@Nullable Class<?> type) {
        if (type == null) {
            return null;
        }
        return primitiveDefaults.get(type);
    }
}
