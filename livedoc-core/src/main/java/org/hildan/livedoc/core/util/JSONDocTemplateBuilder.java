package org.hildan.livedoc.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.pojo.JSONDocTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONDocTemplateBuilder {

    private static final Logger log = LoggerFactory.getLogger(JSONDocTemplateBuilder.class);
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

    public static JSONDocTemplate build(Class<?> clazz, Set<Class<?>> jsondocObjects) {
        final JSONDocTemplate jsonDocTemplate = new JSONDocTemplate();

        if(jsondocObjects.contains(clazz)) {
            try {
                Set<JSONDocFieldWrapper> fields = getAllDeclaredFields(clazz);

                for (JSONDocFieldWrapper jsondocFieldWrapper : fields) {
                    Field field = jsondocFieldWrapper.getField();
                    String fieldName = field.getName();
                    ApiObjectField apiObjectField = field.getAnnotation(ApiObjectField.class);
                    if (apiObjectField != null && !apiObjectField.name().isEmpty()) {
                        fieldName = apiObjectField.name();
                    }

                    Object value;
                    // This condition is to avoid StackOverflow in case class "A"
                    // contains a field of type "A"
                    if (field.getType().equals(clazz) || (apiObjectField != null && !apiObjectField.processtemplate())) {
                        value = getValue(Object.class, field.getGenericType(), fieldName, jsondocObjects);
                    } else {
                        value = getValue(field.getType(), field.getGenericType(), fieldName, jsondocObjects);
                    }

                    jsonDocTemplate.put(fieldName, value);
                }

            } catch (Exception e) {
                log.error("Error in JSONDocTemplate creation for class [" + clazz.getCanonicalName() + "]", e);
            }
        }

        return jsonDocTemplate;
    }

    private static Object getValue(Class<?> fieldClass, Type fieldGenericType, String fieldName, Set<Class<?>> jsondocObjects) {

        if (fieldClass.isPrimitive()) {
            return getValue(wrap(fieldClass), null, fieldName, jsondocObjects);

        } else if (Map.class.isAssignableFrom(fieldClass)) {
            return new HashMap<>();

        } else if (Number.class.isAssignableFrom(fieldClass)) {
            return 0;

        } else if (String.class.isAssignableFrom(fieldClass) || fieldClass.isEnum()) {
            return "";

        } else if (Boolean.class.isAssignableFrom(fieldClass)) {
            return Boolean.TRUE;

        } else if (fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass)) {
            return new ArrayList<>();

        } else {
            return build(fieldClass, jsondocObjects);
        }

    }

    private static Set<JSONDocFieldWrapper> getAllDeclaredFields(Class<?> clazz) {
        Set<JSONDocFieldWrapper> fields = new TreeSet<>();

        List<Field> declaredFields = new ArrayList<>();
        if (clazz.isEnum()) {
            return fields;
        } else {
            declaredFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }

        for (Field field : declaredFields) {
            if (!shouldBeSerialized(field)) {
                continue;
            }
            if (field.isAnnotationPresent(ApiObjectField.class)) {
                ApiObjectField annotation = field.getAnnotation(ApiObjectField.class);
                fields.add(new JSONDocFieldWrapper(field, annotation.order()));
            } else {
                fields.add(new JSONDocFieldWrapper(field, Integer.MAX_VALUE));
            }
        }

        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllDeclaredFields(clazz.getSuperclass()));
        }

        return fields;
    }

    private static boolean shouldBeSerialized(Field field) {
        return !field.isSynthetic() && !Modifier.isTransient(field.getModifiers());
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> wrap(Class<T> clazz) {
        return clazz.isPrimitive() ? (Class<T>) primitives.get(clazz) : clazz;
    }
}
