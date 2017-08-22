package org.hildan.livedoc.core.scanner.properties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.reflect.TypeToken;
import static org.junit.Assert.*;

public class FieldPropertyScannerTest {

    private PropertyScanner scanner;

    @Before
    public void setUp() throws Exception {
        scanner = new FieldPropertyScanner();
    }

    @Test
    public void getFieldTypes_primitive() {
        Set<Property> props = scanner.getProperties(int.class);
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
    public void getFieldTypes_excludesTransient() {
        Set<Property> props = new HashSet<>();
        props.add(new Property("stringField", String.class));
        props.add(new Property("intField", int.class));
        props.add(new Property("objField", Object.class));
        props.add(new Property("genericField", new TypeToken<List<Integer>>(){}.getType()));
        assertEquals(props, scanner.getProperties(CustomObject.class));
    }

    private static class Parent {
        private Float parentField;
    }

    private static class Child extends Parent {
        private Double childField;
    }

    @Test
    public void getFieldTypes_inheritedFields() {
        Set<Property> childProps = new HashSet<>();
        childProps.add(new Property("parentField", Float.class));
        childProps.add(new Property("childField", Double.class));
        assertEquals(childProps, scanner.getProperties(Child.class));
    }

    enum MyEnum {
        A,
        B;

        private int intField;

        private Double doubleField;
    }

    @Test
    public void getFieldTypes_enum() {
        Set<Property> props = new HashSet<>();
        props.add(new Property("name", String.class));
        props.add(new Property("ordinal", int.class));
        props.add(new Property("intField", int.class));
        props.add(new Property("doubleField", Double.class));
        assertEquals(props, scanner.getProperties(MyEnum.class));
    }

}
