package org.hildan.livedoc.springmvc.scanner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.hildan.livedoc.core.LivedocBuilder;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocBuilderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringConsumesBuilderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping(value = "/consumes-one", consumes = MediaType.APPLICATION_JSON_VALUE)
        public void consumesOne() {

        }

        @RequestMapping(value = "/consumes-two",
                consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
        public void consumesTwo() {

        }

        @RequestMapping(value = "/consumes-three")
        public void consumesThree() {

        }

    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public class SpringController2 {

        @RequestMapping(value = "/consumes-one")
        public void consumesOne() {

        }

        @RequestMapping(value = "/consumes-two",
                consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
        public void consumesTwo() {

        }

        @RequestMapping(value = "/consumes-three", consumes = MediaType.APPLICATION_XML_VALUE)
        public void consumesThree() {

        }

    }

    @Test
    public void testApiMethodConsumes_methodLevel() {
        LivedocBuilder builder = SpringLivedocBuilderFactory.springLivedocBuilder(Collections.emptyList());
        ApiDoc apiDoc = builder.readApiDoc(SpringController.class, MethodDisplay.URI, new HashMap<>());
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/consumes-one")) {
                Assert.assertEquals(1, apiMethodDoc.getConsumes().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiMethodDoc.getConsumes().iterator().next());
            }
            if (apiMethodDoc.getPath().contains("/consumes-two")) {
                Assert.assertEquals(2, apiMethodDoc.getConsumes().size());
                Iterator<String> iterator = apiMethodDoc.getConsumes().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiMethodDoc.getPath().contains("/consumes-three")) {
                Assert.assertEquals(1, apiMethodDoc.getConsumes().size());
                String consumes = apiMethodDoc.getConsumes().iterator().next();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, consumes);
            }
        }
    }

    @Test
    public void testApiMethodConsumes_typeLevel() {
        LivedocBuilder builder = SpringLivedocBuilderFactory.springLivedocBuilder(Collections.emptyList());
        ApiDoc apiDoc = builder.readApiDoc(SpringController2.class, MethodDisplay.URI, new HashMap<>());
        Assert.assertEquals("SpringController2", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/consumes-one")) {
                Assert.assertEquals(1, apiMethodDoc.getConsumes().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiMethodDoc.getConsumes().iterator().next());
            }
            if (apiMethodDoc.getPath().contains("/consumes-two")) {
                Assert.assertEquals(2, apiMethodDoc.getConsumes().size());
                Iterator<String> iterator = apiMethodDoc.getConsumes().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiMethodDoc.getPath().contains("/consumes-three")) {
                Assert.assertEquals(1, apiMethodDoc.getConsumes().size());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, apiMethodDoc.getConsumes().iterator().next());
            }
        }
    }

}
