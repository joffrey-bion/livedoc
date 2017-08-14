package org.hildan.livedoc.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.pojo.LivedocTemplate;
import org.hildan.livedoc.core.util.pojo.MyEnum;
import org.hildan.livedoc.core.util.pojo.NoAnnotationPojo;
import org.hildan.livedoc.core.util.pojo.TemplateObject;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

public class LivedocTemplateBuilderTest {

    @Test
    public void testTemplate()
            throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        Set<Class<?>> classes = Sets.newHashSet(TemplateObject.class);

        Map<String, Object> template = LivedocTemplateBuilder.build(TemplateObject.class, classes);

        Assert.assertEquals(0, template.get("my_id"));
        Assert.assertEquals(0, template.get("idint"));
        Assert.assertEquals(0, template.get("idlong"));
        Assert.assertEquals("", template.get("name"));
        Assert.assertEquals("", template.get("gender"));
        Assert.assertEquals(true, template.get("bool"));
        Assert.assertEquals(new ArrayList(), template.get("intarrarr"));
        Assert.assertEquals(new LivedocTemplate(), template.get("sub_obj"));
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
        Assert.assertEquals("", template.get("namechar"));
        Assert.assertEquals(new HashMap(), template.get("map"));
        Assert.assertEquals(new HashMap(), template.get("mapstringinteger"));
        Assert.assertEquals(new HashMap(), template.get("mapsubobjinteger"));
        Assert.assertEquals(new HashMap(), template.get("mapintegersubobj"));
        Assert.assertEquals(new HashMap(), template.get("mapintegerlistsubsubobj"));

        System.out.println(mapper.writeValueAsString(template));
    }

    @Test
    public void testEnum()
            throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        Set<Class<?>> classes = Sets.newHashSet(MyEnum.class);

        Map<String, Object> template = LivedocTemplateBuilder.build(MyEnum.class, classes);
        System.out.println(mapper.writeValueAsString(template));
    }

    @Test
    public void testNoAnnotation()
            throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        Set<Class<?>> classes = Sets.newHashSet(NoAnnotationPojo.class);

        Map<String, Object> template = LivedocTemplateBuilder.build(NoAnnotationPojo.class, classes);
        System.out.println(mapper.writeValueAsString(template));
    }
}
