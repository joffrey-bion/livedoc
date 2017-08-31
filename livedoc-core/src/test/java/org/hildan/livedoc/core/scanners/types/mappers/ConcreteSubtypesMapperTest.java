package org.hildan.livedoc.core.scanners.types.mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;

import static org.junit.Assert.assertTrue;

public class ConcreteSubtypesMapperTest {

    private interface A {}

    private interface B extends A {}

    private abstract class AbstractA implements A {}

    private class SimpleAImpl implements A {}

    private class ConcreteAImpl extends AbstractA implements A {}

    private class BImpl implements B {}

    private static ConcreteSubtypesMapper mapper;

    @BeforeClass
    public static void setUp() {
        mapper = new ConcreteSubtypesMapper(new Reflections("org.hildan.livedoc.core.scanners.types.mappers"));
    }

    @Test
    public void apply_concreteClass() {
        assertEquals(mapper.apply(SimpleAImpl.class), SimpleAImpl.class);
        assertEquals(mapper.apply(ConcreteAImpl.class), ConcreteAImpl.class);
        assertEquals(mapper.apply(BImpl.class), BImpl.class);
    }

    @Test
    public void apply_abstractClass() {
        assertEquals(mapper.apply(AbstractA.class), ConcreteAImpl.class);
    }

    @Test
    public void apply_interface() {
        assertEquals(mapper.apply(B.class), BImpl.class);
        assertEquals(mapper.apply(A.class), SimpleAImpl.class, ConcreteAImpl.class, BImpl.class);
    }

    private void assertEquals(Set<Class<?>> classes, Class<?>... expected) {
        Set<Class<?>> expectedSet = new HashSet<>(Arrays.asList(expected));
        assertTrue(expectedSet.equals(classes));
    }
}
