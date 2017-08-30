package org.hildan.livedoc.core.scanners.properties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.reflect.TypeToken;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldPropertyScannerTest {

    private PropertyScanner scanner;

    @Before
    public void setUp() throws Exception {
        scanner = new FieldPropertyScanner();
    }

    @Test
    public void getFieldTypes_primitive() {
        List<Property> props = scanner.getProperties(int.class);
        assertTrue(props.isEmpty());
    }

    private static class CustomObject {

        private static final Float SOME_CONSTANT = 0f;

        private static Float someStaticThing;

        public String stringField;

        int intField;

        private Object objField;

        private List<Integer> genericField;

        public transient Long transientField;

        private transient double transientField2;
    }

    @Test
    public void getFieldTypes_excludesTransient() throws NoSuchFieldException {
        Set<Property> props = new HashSet<>();
        props.add(createFieldProperty("stringField", String.class, CustomObject.class));
        props.add(createFieldProperty("intField", int.class, CustomObject.class));
        props.add(createFieldProperty("objField", Object.class, CustomObject.class));
        props.add(new Property("genericField", List.class, new TypeToken<List<Integer>>() {}.getType(),
                CustomObject.class.getDeclaredField("genericField")));
        assertEquals(props, new HashSet<>(scanner.getProperties(CustomObject.class)));
    }

    private static class Parent {
        private Float parentField;
    }

    private static class Child extends Parent {
        private Double childField;
    }

    @Test
    public void getFieldTypes_inheritedFields() throws NoSuchFieldException {
        Set<Property> props = new HashSet<>();
        props.add(createFieldProperty("parentField", Float.class, Parent.class));
        props.add(createFieldProperty("childField", Double.class, Child.class));
        assertEquals(props, new HashSet<>(scanner.getProperties(Child.class)));
    }

    enum MyEnum {
        A,
        B;

        private int intField;

        private Double doubleField;
    }

    @Test
    public void getFieldTypes_enum() throws NoSuchFieldException {
        Set<Property> props = new HashSet<>();
        props.add(createFieldProperty("name", String.class, Enum.class));
        props.add(createFieldProperty("ordinal", int.class, Enum.class));
        props.add(createFieldProperty("intField", int.class, MyEnum.class));
        props.add(createFieldProperty("doubleField", Double.class, MyEnum.class));
        assertEquals(props, new HashSet<>(scanner.getProperties(MyEnum.class)));
    }

    private static Property createFieldProperty(String name, Class<?> type, Class<?> containingClass)
            throws NoSuchFieldException {
        return new Property(name, type, containingClass.getDeclaredField(name));
    }
}
