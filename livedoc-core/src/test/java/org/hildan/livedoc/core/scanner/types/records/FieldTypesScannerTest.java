package org.hildan.livedoc.core.scanner.types.records;

import java.lang.reflect.Type;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTypesScannerTest {

    private RecordTypesScanner scanner;

    @Before
    public void setUp() throws Exception {
        scanner = new FieldTypesScanner();
    }

    private static class WithPrimitives {
        private int intField;

        private float floatField;

        private Double doubleField;
    }

    @Test
    public void getFieldTypes_primitivesAndWrappers() {
        checkContains(WithPrimitives.class, int.class, float.class, Double.class);
    }

    private static class Custom {
        private Long longField;

        private String stringField;

        private WithPrimitives nestedPrimitives2;
    }

    @Test
    public void getFieldTypes_customObject() {
        checkContains(Custom.class, Long.class, String.class, WithPrimitives.class);
    }

    private static class Parent {
        private Float floatField;
    }

    private static class Child extends Parent {
        private Double doubleField;
    }

    @Test
    public void getFieldTypes_inheritedFields() throws Exception {
        checkContains(Child.class, Double.class, Float.class);
    }

    enum MyEnum {
        A,
        B;

        private int intField;

        private float floatField;

        private Double doubleField;
    }

    @Test
    public void getFieldTypes_enum() {
        checkContains(MyEnum.class, int.class, float.class, Double.class);
    }

    @Test
    public void getFieldTypes_excludesGarbage() {
        checkExcludes(MyEnum.class, Class.class, Enum.class);
    }

    private static class WithSerializableStuff {
        private String stringField;

        private int intField;

        private transient Long transientField;

        private transient double transientField2;
    }

    @Test
    public void getFieldTypes_excludesTransient() {
        checkExcludes(WithSerializableStuff.class, Long.class, double.class);
    }

    private void checkContains(Class<?> clazz, Type... expected) {
        Set<Type> types = scanner.getFieldTypes(clazz);
        for (Type type : expected) {
            assertTrue(clazz.getName() + " record types should contain " + type.getTypeName(), types.contains(type));
        }
    }

    private void checkExcludes(Class<?> clazz, Type... excludedTypes) {
        Set<Type> types = scanner.getFieldTypes(clazz);
        for (Type type : excludedTypes) {
            assertFalse(clazz.getName() + " record types should NOT contain " + type.getTypeName(),
                    types.contains(type));
        }
    }
}
