package org.hildan.livedoc.springmvc.readers.request;

import java.util.Iterator;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class HttpMediaTypeReaderTest {

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
    public void testProduces_methodLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/produces-one")) {
                Assert.assertEquals(1, apiOperationDoc.getProduces().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiOperationDoc.getProduces().iterator().next());
            }
            if (apiOperationDoc.getPaths().contains("/produces-two")) {
                Assert.assertEquals(2, apiOperationDoc.getProduces().size());
                Iterator<String> iterator = apiOperationDoc.getProduces().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiOperationDoc.getPaths().contains("/produces-three")) {
                Assert.assertEquals(1, apiOperationDoc.getProduces().size());
                String produces = apiOperationDoc.getProduces().iterator().next();
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
    public void testProduces_typeLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController2.class);
        Assert.assertEquals("SpringController2", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/produces-one")) {
                Assert.assertEquals(1, apiOperationDoc.getProduces().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiOperationDoc.getProduces().iterator().next());
            }
            if (apiOperationDoc.getPaths().contains("/produces-two")) {
                Assert.assertEquals(2, apiOperationDoc.getProduces().size());
                Iterator<String> iterator = apiOperationDoc.getProduces().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiOperationDoc.getPaths().contains("/produces-three")) {
                Assert.assertEquals(1, apiOperationDoc.getProduces().size());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, apiOperationDoc.getProduces().iterator().next());
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
    public void testConsumes_methodLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController3.class);
        Assert.assertEquals("SpringController3", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/consumes-one")) {
                Assert.assertEquals(1, apiOperationDoc.getConsumes().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiOperationDoc.getConsumes().iterator().next());
            }
            if (apiOperationDoc.getPaths().contains("/consumes-two")) {
                Assert.assertEquals(2, apiOperationDoc.getConsumes().size());
                Iterator<String> iterator = apiOperationDoc.getConsumes().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiOperationDoc.getPaths().contains("/consumes-three")) {
                Assert.assertEquals(1, apiOperationDoc.getConsumes().size());
                String consumes = apiOperationDoc.getConsumes().iterator().next();
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
    public void testConsumes_typeLevel() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController4.class);
        Assert.assertEquals("SpringController4", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/consumes-one")) {
                Assert.assertEquals(1, apiOperationDoc.getConsumes().size());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, apiOperationDoc.getConsumes().iterator().next());
            }
            if (apiOperationDoc.getPaths().contains("/consumes-two")) {
                Assert.assertEquals(2, apiOperationDoc.getConsumes().size());
                Iterator<String> iterator = apiOperationDoc.getConsumes().iterator();
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, iterator.next());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, iterator.next());
            }
            if (apiOperationDoc.getPaths().contains("/consumes-three")) {
                Assert.assertEquals(1, apiOperationDoc.getConsumes().size());
                Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, apiOperationDoc.getConsumes().iterator().next());
            }
        }
    }

}
