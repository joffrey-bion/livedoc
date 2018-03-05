package org.hildan.livedoc.core.readers.types;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.generics.GenericDeclarationExplorer;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceBuilder;
import org.junit.Assert;
import org.junit.Test;

public class TypeReferenceBuilderTest {

    public String getString() {
        return null;
    }

    public Integer getInteger() {
        return null;
    }

    public Long getLong() {
        return null;
    }

    public int getInt() {
        return 0;
    }

    public long getlong() {
        return 0L;
    }

    public List<String> getListString() {
        return null;
    }

    public List<Set<String>> getListSetString() {
        return null;
    }

    public String[] getStringArray() {
        return null;
    }

    public Integer[] getIntegerArray() {
        return null;
    }

    public List<String>[] getArrayOfListsOfStrings() {
        return null;
    }

    public Set<String>[] getArrayOfSetsOfStrings() {
        return null;
    }

    public List getList() {
        return null;
    }

    public List<?> getListOfWildcard() {
        return null;
    }

    public List<?>[] getArrayOfListsOfWildcard() {
        return null;
    }

    public List[] getListArray() {
        return null;
    }

    public Set[] getSetArray() {
        return null;
    }

    public Map getMap() {
        return null;
    }

    public HashMap getHashMap() {
        return null;
    }

    public Map<String, Integer> getMapStringInteger() {
        return null;
    }

    public Map<List<String>, Integer> getMapListOfStringInteger() {
        return null;
    }

    public Map<String, Set<Integer>> getMapStringSetOfInteger() {
        return null;
    }

    public Map<List<String>, Set<Integer>> getMapListOfStringSetOfInteger() {
        return null;
    }

    public Map<List<Set<String>>, Set<Integer>> getMapListOfSetOfStringSetOfInteger() {
        return null;
    }

    public Map<?, Integer> getMapWildcardInteger() {
        return null;
    }

    public Map<?, ?> getMapWildcardWildcard() {
        return null;
    }

    public Map<List<?>, ?> getMapListOfWildcardWildcard() {
        return null;
    }

    public Map<Map, Integer> getMapMapInteger() {
        return null;
    }

    public Map<Map<String, Long>, Integer> getMapMapStringLongInteger() {
        return null;
    }

    public ResponseEntity<String> getResponseEntityString() {
        return null;
    }

    public ResponseEntity<List<String>> getResponseEntityListOfString() {
        return null;
    }

    public List<ParentPojo> getParentPojoList() {
        return null;
    }

    public <T> FooPojo<T> getSpecializedWGenericsPojo() {
        return null;
    }

    // similar to the Spring class
    private static class ResponseEntity<T> {}

    @ApiType(name = "my_parent")
    private class ParentPojo {

    }

    @ApiType(name = "fooPojo", group = "foo")
    private class FooPojo<K> {
        @ApiTypeProperty
        private K fooField;
    }

    @Test
    public void testReflex() throws NoSuchMethodException, SecurityException {
        LivedocType livedocType;

        livedocType = buildReturnTypeFor("getString");
        Assert.assertEquals("String", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getInteger");
        Assert.assertEquals("Integer", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getInt");
        Assert.assertEquals("int", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getLong");
        Assert.assertEquals("Long", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getlong");
        Assert.assertEquals("long", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getListString");
        Assert.assertEquals("List<String>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getListSetString");
        Assert.assertEquals("List<Set<String>>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getStringArray");
        Assert.assertEquals("String[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getIntegerArray");
        Assert.assertEquals("Integer[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getArrayOfListsOfStrings");
        Assert.assertEquals("List<String>[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getArrayOfSetsOfStrings");
        Assert.assertEquals("Set<String>[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getList");
        Assert.assertEquals("List", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getListOfWildcard");
        Assert.assertEquals("List<?>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getArrayOfListsOfWildcard");
        Assert.assertEquals("List<?>[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getListArray");
        Assert.assertEquals("List[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getSetArray");
        Assert.assertEquals("Set[]", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMap");
        Assert.assertEquals("Map", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getHashMap");
        Assert.assertEquals("HashMap", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapStringInteger");
        Assert.assertEquals("Map<String, Integer>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapListOfStringInteger");
        Assert.assertEquals("Map<List<String>, Integer>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapStringSetOfInteger");
        Assert.assertEquals("Map<String, Set<Integer>>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapListOfStringSetOfInteger");
        Assert.assertEquals("Map<List<String>, Set<Integer>>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapListOfSetOfStringSetOfInteger");
        Assert.assertEquals("Map<List<Set<String>>, Set<Integer>>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapWildcardInteger");
        Assert.assertEquals("Map<?, Integer>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapWildcardWildcard");
        Assert.assertEquals("Map<?, ?>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapListOfWildcardWildcard");
        Assert.assertEquals("Map<List<?>, ?>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapMapInteger");
        Assert.assertEquals("Map<Map, Integer>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getMapMapStringLongInteger");
        Assert.assertEquals("Map<Map<String, Long>, Integer>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getResponseEntityString");
        Assert.assertEquals("ResponseEntity<String>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getResponseEntityListOfString");
        Assert.assertEquals("ResponseEntity<List<String>>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getParentPojoList");
        Assert.assertEquals("List<my_parent>", livedocType.getOneLineText());

        livedocType = buildReturnTypeFor("getSpecializedWGenericsPojo");
        Assert.assertEquals("fooPojo<T>", livedocType.getOneLineText());
    }

    private static LivedocType buildReturnTypeFor(String methodName) throws NoSuchMethodException {
        Method method = TypeReferenceBuilderTest.class.getMethod(methodName);
        Type returnType = method.getGenericReturnType();
        return GenericDeclarationExplorer.explore(returnType, new TypeReferenceBuilder(c -> true));
    }
}
