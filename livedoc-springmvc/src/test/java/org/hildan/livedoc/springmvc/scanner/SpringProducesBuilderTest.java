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

public class SpringProducesBuilderTest {

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

}
