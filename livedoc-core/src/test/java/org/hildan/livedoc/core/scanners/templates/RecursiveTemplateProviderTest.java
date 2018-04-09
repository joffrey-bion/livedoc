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
import org.hildan.livedoc.core.scanners.properties.JacksonPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.LivedocPropertyScannerWrapper;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class RecursiveTemplateProviderTest {

    @DataPoints
    public static TemplateProvider[] templateProviders() {
        return new TemplateProvider[] {
            new RecursiveTemplateProvider(createFieldPropScanner(), c -> true),
            new RecursiveTemplateProvider(createJacksonPropertyScanner(), c -> true),
        };
    }

    private static PropertyScanner createFieldPropScanner() {
        return new LivedocPropertyScannerWrapper(new FieldPropertyScanner());
    }

    private static PropertyScanner createJacksonPropertyScanner() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig()
                                   .getDefaultVisibilityChecker()
                                   .withFieldVisibility(Visibility.PUBLIC_ONLY));
        return new LivedocPropertyScannerWrapper(new JacksonPropertyScanner(mapper));
    }

    private void assertTemplate(String expectedJson, Type type, TemplateProvider templateProvider) throws JsonProcessingException {
        Object template = templateProvider.getTemplate(type);
        String actualJson = new ObjectMapper().writeValueAsString(template);
        assertEquals(expectedJson, actualJson);
    }

    @Theory
    public void testJson_null(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("null", null, templateProvider);
    }

    @Theory
    public void testJson_basicTypes(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("null", void.class, templateProvider);
        assertTemplate("0", byte.class, templateProvider);
        assertTemplate("0", Short.class, templateProvider);
        assertTemplate("0", int.class, templateProvider);
        assertTemplate("0", Integer.class, templateProvider);
        assertTemplate("0", long.class, templateProvider);
        assertTemplate("0.0", Float.class, templateProvider);
        assertTemplate("0.0", double.class, templateProvider);
        assertTemplate("true", boolean.class, templateProvider);
        assertTemplate("\" \"", char.class, templateProvider);
        assertTemplate("\"\"", String.class, templateProvider);
    }

    @Theory
    public void testJson_complexTypes(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("{}", Object.class, templateProvider);
        assertTemplate("{\"id\":\"\",\"name\":\"\"}", TemplateSubSubObject.class, templateProvider);
        assertTemplate("{\"id\":0,\"subSubObj\":{\"id\":\"\",\"name\":\"\"},\"test\":\"\"}", TemplateSubObject.class,
                templateProvider);
    }

    @Theory
    public void testJson_basicArrays(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("[0]", int[].class, templateProvider);
        assertTemplate("[0]", Integer[].class, templateProvider);
        assertTemplate("[[0]]", int[][].class, templateProvider);
        assertTemplate("[0]", long[].class, templateProvider);
        assertTemplate("[0.0]", Double[].class, templateProvider);
        assertTemplate("[true]", boolean[].class, templateProvider);
        assertTemplate("[\" \"]", Character[].class, templateProvider);
        assertTemplate("[\"\"]", String[].class, templateProvider);
        assertTemplate("[[\"\"]]", String[][].class, templateProvider);
    }

    @Theory
    public void testJson_collections(TemplateProvider templateProvider) throws JsonProcessingException {
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

        assertTemplate("[]", List.class, templateProvider);
        assertTemplate("[]", Set.class, templateProvider);
        assertTemplate("[]", Collection.class, templateProvider);

        assertTemplate("[0]", integerList, templateProvider);
        assertTemplate("[0]", integerSet, templateProvider);
        assertTemplate("[0]", integerCollection, templateProvider);
        assertTemplate("[[0]]", integerListList, templateProvider);
        assertTemplate("[[0]]", integerSetList, templateProvider);

        assertTemplate("[0]", longArrayList, templateProvider);
        assertTemplate("[0.0]", floatHashSet, templateProvider);
        assertTemplate("[0.0]", doubleArrayList, templateProvider);
        assertTemplate("[true]", booleanLinkedList, templateProvider);
        assertTemplate("[\"\"]", stringList, templateProvider);
        assertTemplate("[[\"\"]]", stringArrayList, templateProvider);

        assertTemplate("[{}]", objectList, templateProvider);
        assertTemplate("[{}]", wildCardList, templateProvider);
        assertTemplate("[{\"id\":\"\",\"name\":\"\"}]", customObjectList, templateProvider);

        assertTemplate("[\"A\"]", enumList, templateProvider);
        // a null value is ugly in a list, an empty list looks better in this case
        assertTemplate("[]", emptyEnumList, templateProvider);
    }

    @Theory
    public void testJson_maps(TemplateProvider templateProvider) throws JsonProcessingException {
        Type mapIntegerString = new TypeToken<Map<Integer, String>>() {}.getType();
        Type mapIntegerListString = new TypeToken<Map<Integer, List<String>>>() {}.getType();
        Type mapStringObject = new TypeToken<Map<String, Object>>() {}.getType();
        Type mapStringCustomObj = new TypeToken<Map<String, TemplateSubSubObject>>() {}.getType();
        Type mapCustomObjInteger = new TypeToken<Map<TemplateSubSubObject, Integer>>() {}.getType();
        Type mapEnumString = new TypeToken<Map<MyEnum, String>>() {}.getType();
        Type mapEmptyEnumString = new TypeToken<Map<EmptyEnum, String>>() {}.getType();

        assertTemplate("{}", Map.class, templateProvider);
        assertTemplate("{\"0\":\"\"}", mapIntegerString, templateProvider);
        assertTemplate("{\"0\":[\"\"]}", mapIntegerListString, templateProvider);
        assertTemplate("{\"\":{}}", mapStringObject, templateProvider);
        assertTemplate("{\"\":{\"id\":\"\",\"name\":\"\"}}", mapStringCustomObj, templateProvider);
        assertTemplate("{\"{id=, name=}\":0}", mapCustomObjInteger, templateProvider);
        assertTemplate("{\"A\":\"\"}", mapEnumString, templateProvider);
        // JSON does not support null keys, so we don't want 'null' as key in the map
        // (which is what an empty enum yields as template)
        assertTemplate("{}", mapEmptyEnumString, templateProvider);
    }

    @SuppressWarnings("unused")
    private enum MyEnum {
        A,
        B
    }

    @Theory
    public void testJson_enum_isFirstValue(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("\"A\"", MyEnum.class, templateProvider);
    }

    private enum EmptyEnum {}

    @Theory
    public void testJson_emptyEnum_isNull(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("null", EmptyEnum.class, templateProvider);
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

    @Theory
    public void testJson_objectCustomOrder(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("{\"aField\":\"\",\"xField\":\"\"}", DefaultOrder.class, templateProvider);
        assertTemplate("{\"xField\":\"\",\"aField\":\"\",\"bField\":\"\"}", CustomOrder.class, templateProvider);
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

    @Theory
    public void testJson_parameterized(TemplateProvider templateProvider) throws JsonProcessingException {
        Type typeParamIntStr = new TypeToken<Param<Integer, String>>() {}.getType();
        // TODO support this
        //        assertTemplate("{\"list\":[0],\"map\":{\"0\":\"\"},\"obj\":0}", typeParamIntStr);
        assertTemplate("{\"list\":[{}],\"map\":{\"{}\":{}},\"obj\":{}}", typeParamIntStr, templateProvider);

        Type typeParamStrObj = new TypeToken<Param<String, TemplateSubSubObject>>() {}.getType();
        assertTemplate("{\"list\":[{}],\"map\":{\"{}\":{}},\"obj\":{}}", typeParamStrObj, templateProvider);
    }

    @SuppressWarnings("unused")
    @ApiType
    static class SelfRef {
        @ApiTypeProperty
        public SelfRef self;
    }

    @Theory
    public void testJson_recursiveLoop_selfRef(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("{\"self\":null}", SelfRef.class, templateProvider);
    }

    @SuppressWarnings("unused")
    @ApiType
    static class Left {
        @ApiTypeProperty
        public Right right;
    }

    @SuppressWarnings("unused")
    @ApiType
    static class Right {
        @ApiTypeProperty
        public Left left;
    }

    @Theory
    public void testJson_recursiveLoop_doubleRef1(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("{\"right\":{\"left\":null}}", Left.class, templateProvider);
    }

    @Theory
    public void testJson_recursiveLoop_doubleRef2(TemplateProvider templateProvider) throws JsonProcessingException {
        assertTemplate("{\"left\":{\"right\":null}}", Right.class, templateProvider);
    }
}
