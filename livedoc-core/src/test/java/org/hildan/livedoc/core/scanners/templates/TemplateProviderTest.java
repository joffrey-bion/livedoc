package org.hildan.livedoc.core.scanners.templates;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.LivedocPropertyScannerWrapper;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;

public class TemplateProviderTest {

    @Test
    public void getTemplate_generalTest() {
        TemplateProvider templateProvider = createTemplateProvider();
        @SuppressWarnings("unchecked")
        Map<String, Object> template = (Map<String, Object>) templateProvider.getTemplate(TemplateObject.class);

        Map<String, Object> subSubObjectTemplate = createSubSubObjExpectedTemplate();

        Map<String, Object> subObjectTemplate = new HashMap<>();
        subObjectTemplate.put("test", "");
        subObjectTemplate.put("id", 0);
        subObjectTemplate.put("subSubObj", subSubObjectTemplate);

        Assert.assertEquals(0, template.get("my_id"));
        Assert.assertEquals(0, template.get("idint"));
        Assert.assertEquals(0L, template.get("idlong"));
        Assert.assertEquals("", template.get("name"));
        Assert.assertEquals("MALE", template.get("gender"));
        Assert.assertEquals(true, template.get("bool"));
        Assert.assertEquals(list(list(0)), template.get("intarrarr"));
        Assert.assertEquals(subObjectTemplate, template.get("sub_obj"));
        Assert.assertEquals(Collections.emptyList(), template.get("rawlist"));
        Assert.assertEquals(list(subSubObjectTemplate), template.get("subsubobjarr"));
        Assert.assertEquals(list(""), template.get("stringlist"));
        Assert.assertEquals(list(list("")), template.get("stringarrarr"));
        Assert.assertEquals(list(0), template.get("integerarr"));
        Assert.assertEquals(list(""), template.get("stringarr"));
        Assert.assertEquals(list(0), template.get("intarr"));
        Assert.assertEquals(list(subObjectTemplate), template.get("subobjlist"));
        Assert.assertEquals(list(Collections.emptyMap()), template.get("wildcardlist"));
        Assert.assertEquals(list(0L), template.get("longlist"));
        Assert.assertEquals(' ', template.get("namechar"));
        Assert.assertEquals(Collections.emptyMap(), template.get("map"));
        Assert.assertEquals(map("", 0), template.get("mapstringinteger"));
        Assert.assertEquals(map(subObjectTemplate, 0), template.get("mapsubobjinteger"));
        Assert.assertEquals(map(0, subObjectTemplate), template.get("mapintegersubobj"));
        Assert.assertEquals(map(0, list(subSubObjectTemplate)), template.get("mapintegerlistsubsubobj"));
    }

    private static Map<String, Object> createSubSubObjExpectedTemplate() {
        Map<String, Object> subSubObjectTemplate = new HashMap<>();
        subSubObjectTemplate.put("id", "");
        subSubObjectTemplate.put("name", "");
        return subSubObjectTemplate;
    }

    private static List<?> list(Object elt) {
        return Collections.singletonList(elt);
    }

    private static Map<?, ?> map(Object key, Object val) {
        return Collections.singletonMap(key, val);
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
    public void getTemplate_customOrder() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        TemplateProvider templateProvider = createTemplateProvider();
        Object unorderedTemplate = templateProvider.getTemplate(DefaultOrder.class);
        Assert.assertEquals("{\"aField\":\"\",\"xField\":\"\"}", mapper.writeValueAsString(unorderedTemplate));

        Object orderedTemplate = templateProvider.getTemplate(CustomOrder.class);
        Assert.assertEquals("{\"xField\":\"\",\"aField\":\"\",\"bField\":\"\"}",
                mapper.writeValueAsString(orderedTemplate));
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
    public void getTemplate_parameterized() {
        TemplateProvider templateProvider = createTemplateProvider();

        Type typeParamIntStr = new TypeToken<Param<Integer, String>>() {}.getType();
        @SuppressWarnings("unchecked")
        Map<String, Object> templateIntStr = (Map<String, Object>) templateProvider.getTemplate(typeParamIntStr);

        // TODO support this
//        Assert.assertEquals(0, templateIntStr.get("obj"));
//        Assert.assertEquals(list(0), templateIntStr.get("list"));
//        Assert.assertEquals(map(0, ""), templateIntStr.get("map"));
        Assert.assertEquals(Collections.emptyMap(), templateIntStr.get("obj"));
        Assert.assertEquals(list(Collections.emptyMap()), templateIntStr.get("list"));
        Assert.assertEquals(map(Collections.emptyMap(), Collections.emptyMap()), templateIntStr.get("map"));

        Type typeParamStrObj = new TypeToken<Param<String, TemplateSubSubObject>>() {}.getType();
        @SuppressWarnings("unchecked")
        Map<String, Object> templateStrObj = (Map<String, Object>) templateProvider.getTemplate(typeParamStrObj);

        // TODO support this
//        Map<String, Object> subSubObjectTemplate = createSubSubObjExpectedTemplate();
//        Assert.assertEquals("", template.get("obj"));
//        Assert.assertEquals(list(""), template.get("list"));
//        Assert.assertEquals(map("", subSubObjectTemplate), template.get("map"));
        Assert.assertEquals(Collections.emptyMap(), templateStrObj.get("obj"));
        Assert.assertEquals(list(Collections.emptyMap()), templateStrObj.get("list"));
        Assert.assertEquals(map(Collections.emptyMap(), Collections.emptyMap()), templateStrObj.get("map"));
    }

    private static TemplateProvider createTemplateProvider() {
        PropertyScanner propertyScanner = new LivedocPropertyScannerWrapper(new FieldPropertyScanner());
        return new RecursiveTemplateProvider(propertyScanner, c -> true);
    }
}
