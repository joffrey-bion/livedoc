package org.hildan.livedoc.springmvc.scanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.test.TestUtils;
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
        ApiDoc apiDoc = TestUtils.buildDoc(SpringApiVerbController.class, MethodDisplay.URI);
        Assert.assertEquals("SpringApiVerbController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api-verb/spring-api-verb-controller-method-one")) {
                Set<ApiVerb> expectedVerbs = new HashSet<>(Arrays.asList(ApiVerb.values()));
                expectedVerbs.remove(ApiVerb.UNDEFINED);
                Assert.assertEquals(expectedVerbs, apiMethodDoc.getVerb());
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
        ApiDoc apiDoc = TestUtils.buildDoc(SpringApiVerbController2.class, MethodDisplay.URI);
        Assert.assertEquals("SpringApiVerbController2", apiDoc.getName());
        Assert.assertEquals(1, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api-verb-2/spring-api-verb-controller-method-one")) {
                Assert.assertEquals(2, apiMethodDoc.getVerb().size());
            }
        }
    }
}
