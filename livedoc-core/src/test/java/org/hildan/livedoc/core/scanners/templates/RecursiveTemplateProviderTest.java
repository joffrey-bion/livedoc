package org.hildan.livedoc.core.scanners.templates;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.LivedocPropertyScannerWrapper;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;

import static org.junit.Assert.assertEquals;

public class RecursiveTemplateProviderTest {

    private static ObjectMapper mapper;

    private TemplateProvider templateProvider;

    @BeforeClass
    public static void setUpMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpTemplateProvider() {
        PropertyScanner propertyScanner = new LivedocPropertyScannerWrapper(new FieldPropertyScanner());
        templateProvider = new RecursiveTemplateProvider(propertyScanner, c -> true);
    }

    private void assertTemplate(String expectedJson, Type type) throws JsonProcessingException {
        Object template = templateProvider.getTemplate(type);
        String actualJson = mapper.writeValueAsString(template);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testJson_null() throws JsonProcessingException {
        assertTemplate("null", null);
    }

    @Test
    public void testJson_basicTypes() throws JsonProcessingException {
        assertTemplate("null", void.class);
        assertTemplate("0", byte.class);
        assertTemplate("0", Short.class);
        assertTemplate("0", int.class);
        assertTemplate("0", Integer.class);
        assertTemplate("0", long.class);
        assertTemplate("0.0", Float.class);
        assertTemplate("0.0", double.class);
        assertTemplate("true", boolean.class);
        assertTemplate("\" \"", char.class);
        assertTemplate("\"\"", String.class);
    }

    @Test
    public void testJson_complexTypes() throws JsonProcessingException {
        assertTemplate("{}", Object.class);
        assertTemplate("{\"id\":\"\",\"name\":\"\"}", TemplateSubSubObject.class);
        assertTemplate("{\"id\":0,\"subSubObj\":{\"id\":\"\",\"name\":\"\"},\"test\":\"\"}", TemplateSubObject.class);
    }

    @Test
    public void testJson_basicArrays() throws JsonProcessingException {
        assertTemplate("[0]", int[].class);
        assertTemplate("[0]", Integer[].class);
        assertTemplate("[[0]]", int[][].class);
        assertTemplate("[0]", long[].class);
        assertTemplate("[0.0]", Double[].class);
        assertTemplate("[true]", boolean[].class);
        assertTemplate("[\" \"]", Character[].class);
        assertTemplate("[\"\"]", String[].class);
        assertTemplate("[[\"\"]]", String[][].class);
    }

    @Test
    public void testJson_collections() throws JsonProcessingException {
        Type integerList = new TypeToken<List<Integer>>() {}.getType();
        Type integerSet = new TypeToken<Set<Integer>>() {}.getType();
        Type integerCollection = new TypeToken<Collection<Integer>>() {}.getType();
        Type integerListList = new TypeToken<List<List<Integer>>>() {}.getType();
        Type integerSetList = new TypeToken<List<Set<Integer>>>() {}.getType();
        Type longArrayList = new TypeToken<ArrayList<Long>>() {}.getType();
        Type floatHashSet = new TypeToken<HashSet<Float>>() {}.getType();
        Type doubleArrayList = new TypeToken<ArrayList<Double>>() {}.getType();
        Type booleanLinkedList = new TypeToken<LinkedList<Boolean>>() {}.getType();
        Type stringList = new TypeToken<List<String>>() {}.getType();
        Type stringArrayList = new TypeToken<List<String[]>>() {}.getType();
        Type objectList = new TypeToken<List<Object>>() {}.getType();
        Type wildCardList = new TypeToken<List<?>>() {}.getType();
        Type customObjectList = new TypeToken<List<TemplateSubSubObject>>() {}.getType();
        Type enumList = new TypeToken<List<MyEnum>>() {}.getType();
        Type emptyEnumList = new TypeToken<List<EmptyEnum>>() {}.getType();

        assertTemplate("[]", List.class);
        assertTemplate("[]", Set.class);
        assertTemplate("[]", Collection.class);

        assertTemplate("[0]", integerList);
        assertTemplate("[0]", integerSet);
        assertTemplate("[0]", integerCollection);
        assertTemplate("[[0]]", integerListList);
        assertTemplate("[[0]]", integerSetList);

        assertTemplate("[0]", longArrayList);
        assertTemplate("[0.0]", floatHashSet);
        assertTemplate("[0.0]", doubleArrayList);
        assertTemplate("[true]", booleanLinkedList);
        assertTemplate("[\"\"]", stringList);
        assertTemplate("[[\"\"]]", stringArrayList);

        assertTemplate("[{}]", objectList);
        assertTemplate("[{}]", wildCardList);
        assertTemplate("[{\"id\":\"\",\"name\":\"\"}]", customObjectList);

        assertTemplate("[\"A\"]", enumList);
        // a null value is ugly in a list, an empty list looks better in this case
        assertTemplate("[]", emptyEnumList);
    }

    @Test
    public void testJson_maps() throws JsonProcessingException {
        Type mapIntegerString = new TypeToken<Map<Integer, String>>() {}.getType();
        Type mapIntegerListString = new TypeToken<Map<Integer, List<String>>>() {}.getType();
        Type mapStringObject = new TypeToken<Map<String, Object>>() {}.getType();
        Type mapStringCustomObj = new TypeToken<Map<String, TemplateSubSubObject>>() {}.getType();
        Type mapCustomObjInteger = new TypeToken<Map<TemplateSubSubObject, Integer>>() {}.getType();
        Type mapEnumString = new TypeToken<Map<MyEnum, String>>() {}.getType();
        Type mapEmptyEnumString = new TypeToken<Map<EmptyEnum, String>>() {}.getType();

        assertTemplate("{}", Map.class);
        assertTemplate("{\"0\":\"\"}", mapIntegerString);
        assertTemplate("{\"0\":[\"\"]}", mapIntegerListString);
        assertTemplate("{\"\":{}}", mapStringObject);
        assertTemplate("{\"\":{\"id\":\"\",\"name\":\"\"}}", mapStringCustomObj);
        assertTemplate("{\"{id=, name=}\":0}", mapCustomObjInteger);
        assertTemplate("{\"A\":\"\"}", mapEnumString);
        // JSON does not support null keys, so we don't want 'null' as key in the map
        // (which is what an empty enum yields as template)
        assertTemplate("{}", mapEmptyEnumString);
    }

    @SuppressWarnings("unused")
    private enum MyEnum {
        A,
        B
    }

    @Test
    public void testJson_enum_isFirstValue() throws JsonProcessingException {
        assertTemplate("\"A\"", MyEnum.class);
    }

    private enum EmptyEnum {}

    @Test
    public void testJson_emptyEnum_isNull() throws JsonProcessingException {
        assertTemplate("null", EmptyEnum.class);
    }

    @SuppressWarnings("unused")
    @ApiType(name = "defaultOrder")
    static class DefaultOrder {

        @ApiTypeProperty(name = "xField")
        public String x;

        @ApiTypeProperty(name = "aField")
        public String a;
    }

    @SuppressWarnings("unused")
    @ApiType(name = "ordered")
    static class CustomOrder {

        @ApiTypeProperty(name = "bField", order = 2)
        public String b;

        @ApiTypeProperty(name = "xField", order = 1)
        public String x;

        @ApiTypeProperty(name = "aField", order = 2)
        public String a;
    }

    @Test
    public void testJson_objectCustomOrder() throws JsonProcessingException {
        assertTemplate("{\"aField\":\"\",\"xField\":\"\"}", DefaultOrder.class);
        assertTemplate("{\"xField\":\"\",\"aField\":\"\",\"bField\":\"\"}", CustomOrder.class);
    }

    @SuppressWarnings("unused")
    @ApiType
    static class Param<T, U> {

        @ApiTypeProperty
        public T obj;

        @ApiTypeProperty
        public List<T> list;

        @ApiTypeProperty
        public Map<T, U> map;
    }

    @Test
    public void testJson_parameterized() throws JsonProcessingException {
        Type typeParamIntStr = new TypeToken<Param<Integer, String>>() {}.getType();
        // TODO support this
        //        assertTemplate("{\"list\":[0],\"map\":{\"0\":\"\"},\"obj\":0}", typeParamIntStr);
        assertTemplate("{\"list\":[{}],\"map\":{\"{}\":{}},\"obj\":{}}", typeParamIntStr);

        Type typeParamStrObj = new TypeToken<Param<String, TemplateSubSubObject>>() {}.getType();
        assertTemplate("{\"list\":[{}],\"map\":{\"{}\":{}},\"obj\":{}}", typeParamStrObj);
    }

    @SuppressWarnings("unused")
    @ApiType
    static class SelfRef {
        @ApiTypeProperty
        SelfRef self;
    }

    @Test
    public void testJson_recursiveLoop_selfRef() throws JsonProcessingException {
        assertTemplate("{\"self\":null}", SelfRef.class);
    }

    @SuppressWarnings("unused")
    @ApiType
    static class Left {
        @ApiTypeProperty
        Right right;
    }

    @SuppressWarnings("unused")
    @ApiType
    static class Right {
        @ApiTypeProperty
        Left left;
    }

    @Test
    public void testJson_recursiveLoop_doubleRef1() throws JsonProcessingException {
        assertTemplate("{\"right\":{\"left\":null}}", Left.class);
    }

    @Test
    public void testJson_recursiveLoop_doubleRef2() throws JsonProcessingException {
        assertTemplate("{\"left\":{\"right\":null}}", Right.class);
    }
}
