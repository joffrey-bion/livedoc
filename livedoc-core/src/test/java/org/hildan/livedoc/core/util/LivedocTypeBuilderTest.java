package org.hildan.livedoc.core.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class LivedocTypeBuilderTest {

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

    public List<String>[] getListOfStringArray() {
        return null;
    }

    public Set<String>[] getSetOfStringArray() {
        return null;
    }

    public List getList() {
        return null;
    }

    public List<?> getListOfWildcard() {
        return null;
    }

    public List<?>[] getListOfWildcardArray() {
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

    @ApiObject(name = "my_parent")
    class ParentPojo {

    }

    @ApiObject(name = "fooPojo", group = "foo")
    public class FooPojo<K> {
        @ApiObjectField
        private K fooField;
    }

    @Test
    public void testReflex() throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            IOException {
        LivedocType livedocType = new LivedocType();

        Method method = LivedocTypeBuilderTest.class.getMethod("getString");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Integer", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getInt");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("int", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getLong");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Long", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getlong");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("long", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListString");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("List of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListSetString");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("List of Set of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getStringArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getIntegerArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of Integer", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListOfStringArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of List of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getSetOfStringArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of Set of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getList");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("List", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListOfWildcard");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("List of wildcard", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListOfWildcardArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of List of wildcard", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of List", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getSetArray");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("array of Set", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMap");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getHashMap");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("HashMap", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapStringInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[String, Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfStringInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[List of String, Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapStringSetOfInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[String, Set of Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfStringSetOfInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[List of String, Set of Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfSetOfStringSetOfInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[List of Set of String, Set of Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapWildcardInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[wildcard, Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapWildcardWildcard");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[wildcard, wildcard]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfWildcardWildcard");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[List of wildcard, wildcard]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapMapInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[Map, Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapMapStringLongInteger");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("Map[Map[String, Long], Integer]", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getResponseEntityString");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("ResponseEntity of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getResponseEntityListOfString");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("ResponseEntity of List of String", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getParentPojoList");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("List of my_parent", livedocType.getOneLineText());

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getSpecializedWGenericsPojo");
        LivedocTypeBuilder.build(method.getReturnType(), method.getGenericReturnType());
        Assert.assertEquals("fooPojo of T", livedocType.getOneLineText());
    }
}
