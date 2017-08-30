package org.hildan.livedoc.core.scanners.types.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hildan.livedoc.core.pojo.Livedoc;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainerTypesExcludingFilterTest {

    private static class Custom {
    }

    private static class MyList extends ArrayList {
    }

    private static class MyMap extends HashMap {
    }

    private TypeFilter typeFilter;

    @Before
    public void setUp() throws Exception {
        typeFilter = new ContainerTypesExcludingFilter();
    }

    @Test
    public void test_primitiveMatch() {
        assertTrue(typeFilter.test(boolean.class));
        assertTrue(typeFilter.test(byte.class));
        assertTrue(typeFilter.test(short.class));
        assertTrue(typeFilter.test(char.class));
        assertTrue(typeFilter.test(int.class));
        assertTrue(typeFilter.test(long.class));
        assertTrue(typeFilter.test(float.class));
        assertTrue(typeFilter.test(double.class));
    }

    @Test
    public void test_primitiveWrappersMatch() {
        assertTrue(typeFilter.test(Boolean.class));
        assertTrue(typeFilter.test(Byte.class));
        assertTrue(typeFilter.test(Short.class));
        assertTrue(typeFilter.test(Character.class));
        assertTrue(typeFilter.test(Integer.class));
        assertTrue(typeFilter.test(Long.class));
        assertTrue(typeFilter.test(Float.class));
        assertTrue(typeFilter.test(Double.class));
    }

    @Test
    public void test_customClassMatches() {
        assertTrue(typeFilter.test(Custom.class));
    }

    @Test
    public void test_containersDontMatch() {
        assertFalse(typeFilter.test(Collection.class));

        assertFalse(typeFilter.test(List.class));
        assertFalse(typeFilter.test(ArrayList.class));
        assertFalse(typeFilter.test(MyList.class));

        assertFalse(typeFilter.test(Set.class));
        assertFalse(typeFilter.test(HashSet.class));

        assertFalse(typeFilter.test(Map.class));
        assertFalse(typeFilter.test(HashMap.class));
        assertFalse(typeFilter.test(TreeMap.class));
        assertFalse(typeFilter.test(MyMap.class));
    }

    @Test
    public void test_livedocDoesntMatch() {
        assertFalse(typeFilter.test(Livedoc.class));
    }
}
