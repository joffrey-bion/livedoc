package org.hildan.livedoc.springmvc.scanner;

import java.util.Collections;
import java.util.HashMap;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class SpringApiVerbDocTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(value = "/api-verb")
    public class SpringApiVerbController {

        @RequestMapping(value = "/spring-api-verb-controller-method-one")
        public void apiVerbOne() {

        }

        @RequestMapping(value = "/spring-api-verb-controller-method-two",
                method = {RequestMethod.POST, RequestMethod.GET})
        public void apiVerbTwo() {

        }

    }

    @Test
    public void testApiVerb() {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        ApiDoc apiDoc = builder.readApiDoc(SpringApiVerbController.class, MethodDisplay.URI, new HashMap<>());
        Assert.assertEquals("SpringApiVerbController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api-verb/spring-api-verb-controller-method-one")) {
                Assert.assertEquals(1, apiMethodDoc.getVerb().size());
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
            }
            if (apiMethodDoc.getPath().contains("/api-verb/spring-api-verb-controller-method-two")) {
                Assert.assertEquals(2, apiMethodDoc.getVerb().size());
            }
        }
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(value = "/api-verb-2", method = {RequestMethod.POST, RequestMethod.PUT})
    public class SpringApiVerbController2 {

        @RequestMapping(value = "/spring-api-verb-controller-method-one")
        public void apiVerbOne() {

        }

    }

    @Test
    public void testApiVerb2() {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        ApiDoc apiDoc = builder.readApiDoc(SpringApiVerbController2.class, MethodDisplay.URI, new HashMap<>());
        Assert.assertEquals("SpringApiVerbController2", apiDoc.getName());
        Assert.assertEquals(1, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api-verb-2/spring-api-verb-controller-method-one")) {
                Assert.assertEquals(2, apiMethodDoc.getVerb().size());
            }
        }

    }

}
