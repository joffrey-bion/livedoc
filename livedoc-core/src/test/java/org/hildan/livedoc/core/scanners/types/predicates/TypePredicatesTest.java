package org.hildan.livedoc.core.scanners.types.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TypePredicatesTest {

    @Test
    public void isBasicType_primitive() {
        assertTrue(TypePredicates.isBasicType(boolean.class));
        assertTrue(TypePredicates.isBasicType(byte.class));
        assertTrue(TypePredicates.isBasicType(short.class));
        assertTrue(TypePredicates.isBasicType(char.class));
        assertTrue(TypePredicates.isBasicType(int.class));
        assertTrue(TypePredicates.isBasicType(long.class));
        assertTrue(TypePredicates.isBasicType(float.class));
        assertTrue(TypePredicates.isBasicType(double.class));
    }

    @Test
    public void isBasicType_primitiveWrappers() {
        assertTrue(TypePredicates.isBasicType(Boolean.class));
        assertTrue(TypePredicates.isBasicType(Byte.class));
        assertTrue(TypePredicates.isBasicType(Short.class));
        assertTrue(TypePredicates.isBasicType(Character.class));
        assertTrue(TypePredicates.isBasicType(Integer.class));
        assertTrue(TypePredicates.isBasicType(Long.class));
        assertTrue(TypePredicates.isBasicType(Float.class));
        assertTrue(TypePredicates.isBasicType(Double.class));
    }

    private static class Custom {
    }

    @Test
    public void isBasicType_customClass() {
        assertFalse(TypePredicates.isBasicType(Custom.class));
    }

    @Test
    public void isBasicType_object() {
        assertFalse(TypePredicates.isBasicType(Object.class));
    }

    private static class MyList extends ArrayList {
    }

    private static class MyMap extends HashMap {
    }

    @Test
    public void isBasicType_containers() {
        assertFalse(TypePredicates.isBasicType(Collection.class));

        assertFalse(TypePredicates.isBasicType(List.class));
        assertFalse(TypePredicates.isBasicType(ArrayList.class));
        assertFalse(TypePredicates.isBasicType(MyList.class));

        assertFalse(TypePredicates.isBasicType(Set.class));
        assertFalse(TypePredicates.isBasicType(HashSet.class));

        assertFalse(TypePredicates.isBasicType(Map.class));
        assertFalse(TypePredicates.isBasicType(HashMap.class));
        assertFalse(TypePredicates.isBasicType(TreeMap.class));
        assertFalse(TypePredicates.isBasicType(MyMap.class));
    }

    @Test
    public void isContainer_containers() {
        assertTrue(TypePredicates.isContainer(Collection.class));

        assertTrue(TypePredicates.isContainer(List.class));
        assertTrue(TypePredicates.isContainer(ArrayList.class));
        assertTrue(TypePredicates.isContainer(MyList.class));

        assertTrue(TypePredicates.isContainer(Set.class));
        assertTrue(TypePredicates.isContainer(HashSet.class));

        assertTrue(TypePredicates.isContainer(Map.class));
        assertTrue(TypePredicates.isContainer(HashMap.class));
        assertTrue(TypePredicates.isContainer(TreeMap.class));
        assertTrue(TypePredicates.isContainer(MyMap.class));
    }
}
