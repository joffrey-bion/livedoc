package org.hildan.livedoc.core.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LivedocTypeBuilderTest {

    private ObjectMapper mapper = new ObjectMapper();

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
    public void testReflex()
            throws NoSuchMethodException, SecurityException, ClassNotFoundException, JsonGenerationException,
            JsonMappingException, IOException {
        mapper.setSerializationInclusion(Include.NON_NULL);
        LivedocType livedocType = new LivedocType();

        Method method = LivedocTypeBuilderTest.class.getMethod("getString");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Integer", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getInt");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("int", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getLong");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Long", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getlong");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("long", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListString");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("List of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListSetString");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("List of Set of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getStringArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getIntegerArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of Integer", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListOfStringArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of List of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getSetOfStringArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of Set of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getList");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("List", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListOfWildcard");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("List of wildcard", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListOfWildcardArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of List of wildcard", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getListArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of List", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getSetArray");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("array of Set", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMap");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getHashMap");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("HashMap", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapStringInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[String, Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfStringInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[List of String, Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapStringSetOfInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[String, Set of Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfStringSetOfInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[List of String, Set of Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfSetOfStringSetOfInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[List of Set of String, Set of Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapWildcardInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[wildcard, Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapWildcardWildcard");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[wildcard, wildcard]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapListOfWildcardWildcard");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[List of wildcard, wildcard]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapMapInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[Map, Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getMapMapStringLongInteger");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("Map[Map[String, Long], Integer]", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getResponseEntityString");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("ResponseEntity of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getResponseEntityListOfString");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("ResponseEntity of List of String", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getParentPojoList");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("List of my_parent", livedocType.getOneLineText());
        System.out.println("---------------------------");

        livedocType = new LivedocType();
        method = LivedocTypeBuilderTest.class.getMethod("getSpecializedWGenericsPojo");
        LivedocTypeBuilder.build(livedocType, method.getReturnType(), method.getGenericReturnType());
        System.out.println(mapper.writeValueAsString(livedocType));
        System.out.println(livedocType.getOneLineText());
        Assert.assertEquals("fooPojo of T", livedocType.getOneLineText());
        System.out.println("---------------------------");
    }

}
