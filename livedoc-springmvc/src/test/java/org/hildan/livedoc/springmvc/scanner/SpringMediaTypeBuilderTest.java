package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringMediaTypeBuilderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping(value = "/produces-one", produces = MediaType.APPLICATION_JSON_VALUE)
        public void producesOne() {

        }

        @RequestMapping(value = "/produces-two",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
        public void producesTwo() {

        }

        @RequestMapping(value = "/produces-three")
        public void producesThree() {

        }

    }

    @Test
    public void testApiConsumes_methodLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/produces-one")) {
                Assert.assertEquals(1, apiMethodDoc.getProduces().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiMethodDoc.getProduces().iterator().next());
            }
            if (apiMethodDoc.getPath().contains("/produces-two")) {
                Assert.assertEquals(2, apiMethodDoc.getProduces().size());
                Iterator<String> iterator = apiMethodDoc.getProduces().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiMethodDoc.getPath().contains("/produces-three")) {
                Assert.assertEquals(1, apiMethodDoc.getProduces().size());
                String produces = apiMethodDoc.getProduces().iterator().next();
                Assert.assertEquals("application/json", produces);
            }
        }

    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public class SpringController2 {

        @RequestMapping(value = "/produces-one")
        public void producesOne() {

        }

        @RequestMapping(value = "/produces-two",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
        public void producesTwo() {

        }

        @RequestMapping(value = "/produces-three", produces = MediaType.APPLICATION_XML_VALUE)
        public void producesThree() {

        }

    }

    @Test
    public void testApiConsumes_typeLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController2.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController2", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/produces-one")) {
                Assert.assertEquals(1, apiMethodDoc.getProduces().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiMethodDoc.getProduces().iterator().next());
            }
            if (apiMethodDoc.getPath().contains("/produces-two")) {
                Assert.assertEquals(2, apiMethodDoc.getProduces().size());
                Iterator<String> iterator = apiMethodDoc.getProduces().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiMethodDoc.getPath().contains("/produces-three")) {
                Assert.assertEquals(1, apiMethodDoc.getProduces().size());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, apiMethodDoc.getProduces().iterator().next());
            }
        }
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController3 {

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

    @Test
    public void testApiMethodConsumes_methodLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController3.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController3", apiDoc.getName());
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

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public class SpringController4 {

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
    public void testApiMethodConsumes_typeLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController4.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController4", apiDoc.getName());
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
