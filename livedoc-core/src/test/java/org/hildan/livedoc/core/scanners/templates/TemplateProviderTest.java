package org.hildan.livedoc.core.scanners.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.LivedocPropertyScannerWrapper;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.junit.Assert;
import org.junit.Test;

public class TemplateProviderTest {

    @Test
    public void getTemplate_generalTest() {
        TemplateProvider templateProvider = createTemplateProvider();
        @SuppressWarnings("unchecked")
        Map<String, Object> template = (Map<String, Object>) templateProvider.getTemplate(TemplateObject.class);

        Map<String, Object> subSubObjectTemplate = new HashMap<>();
        subSubObjectTemplate.put("id", "");
        subSubObjectTemplate.put("name", "");

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
        Assert.assertEquals(new ArrayList(), template.get("intarrarr"));
        Assert.assertEquals(subObjectTemplate, template.get("sub_obj"));
        Assert.assertEquals(new ArrayList(), template.get("untypedlist"));
        Assert.assertEquals(new ArrayList(), template.get("subsubobjarr"));
        Assert.assertEquals(new ArrayList(), template.get("stringlist"));
        Assert.assertEquals(new ArrayList(), template.get("stringarrarr"));
        Assert.assertEquals(new ArrayList(), template.get("integerarr"));
        Assert.assertEquals(new ArrayList(), template.get("stringarr"));
        Assert.assertEquals(new ArrayList(), template.get("intarr"));
        Assert.assertEquals(new ArrayList(), template.get("subobjlist"));
        Assert.assertEquals(new ArrayList(), template.get("wildcardlist"));
        Assert.assertEquals(new ArrayList(), template.get("longlist"));
        Assert.assertEquals(' ', template.get("namechar"));
        Assert.assertEquals(new HashMap(), template.get("map"));
        Assert.assertEquals(new HashMap(), template.get("mapstringinteger"));
        Assert.assertEquals(new HashMap(), template.get("mapsubobjinteger"));
        Assert.assertEquals(new HashMap(), template.get("mapintegersubobj"));
        Assert.assertEquals(new HashMap(), template.get("mapintegerlistsubsubobj"));
    }

    @SuppressWarnings("unused")
    @ApiObject(name = "defaultOrder")
    static class DefaultOrder {

        @ApiObjectProperty(name = "xField")
        public String x;

        @ApiObjectProperty(name = "aField")
        public String a;
    }

    @SuppressWarnings("unused")
    @ApiObject(name = "ordered")
    static class CustomOrder {

        @ApiObjectProperty(name = "bField", order = 2)
        public String b;

        @ApiObjectProperty(name = "xField", order = 1)
        public String x;

        @ApiObjectProperty(name = "aField", order = 2)
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

    private static TemplateProvider createTemplateProvider() {
        PropertyScanner propertyScanner = new LivedocPropertyScannerWrapper(new FieldPropertyScanner());
        return new RecursiveTemplateProvider(propertyScanner, c -> true);
    }
}
