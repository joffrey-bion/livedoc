package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hildan.livedoc.core.scanner.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypesExplorerTest {

    private TypesExplorer explorer;

    @Before
    public void setUp() {
        PropertyScanner scanner = new FieldPropertyScanner();
        explorer = new TypesExplorer(scanner);
    }

    @Test
    public void findTypes_simpleString() {
        Set<Type> rootTypes = Collections.singleton(String.class);
        assertSetEquals(explorer.findTypes(rootTypes), String.class);
    }

    @Test
    public void findTypes_simpleCharacter() {
        Set<Type> rootTypes = Collections.singleton(Character.class);
        assertSetEquals(explorer.findTypes(rootTypes), Character.class);
    }

    private static class Custom {
        private Long longField;
    }

    @Test
    public void findTypes_customType() {
        Set<Type> rootTypes = Collections.singleton(Custom.class);
        assertSetEquals(explorer.findTypes(rootTypes), Custom.class, Long.class);
    }

    private static class Child extends Custom {
        private Double doubleField;
    }

    @Test
    public void findTypes_inheritedFields() {
        Set<Type> rootTypes = Collections.singleton(Child.class);
        assertSetEquals(explorer.findTypes(rootTypes), Child.class, Double.class, Long.class);
    }

    private static class TestRoot {
        private WithCustomObject nestedObject;

        private WithPrimitives nestedPrimitives;
    }

    private static class WithCustomObject {
        private Custom customObject;
    }

    private static class WithPrimitives {
        private String stringField;

        private int intField;
    }

    @Test
    public void findTypes_complexHierarchy() {
        Set<Type> rootTypes = Collections.singleton(TestRoot.class);
        assertSetEquals(explorer.findTypes(rootTypes), TestRoot.class, WithCustomObject.class, WithPrimitives.class,
                Custom.class, Long.class, String.class, int.class);

    }

    private static void assertSetEquals(Set<Class<?>> actual, Class<?>... expected) {
        assertEquals(new HashSet<>(Arrays.asList(expected)), actual);
    }
}
