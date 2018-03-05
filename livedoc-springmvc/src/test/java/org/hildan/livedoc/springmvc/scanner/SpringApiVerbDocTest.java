package org.hildan.livedoc.springmvc.scanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class SpringApiVerbDocTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping("/api-verb")
    public class SpringApiVerbController {

        @RequestMapping("/spring-api-verb-controller-method-one")
        public void apiVerbOne() {
        }

        @RequestMapping(value = "/spring-api-verb-controller-method-two",
                method = {RequestMethod.POST, RequestMethod.GET})
        public void apiVerbTwo() {
        }
    }

    @Test
    public void testApiVerb() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringApiVerbController.class);
        Assert.assertEquals("SpringApiVerbController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/api-verb/spring-api-verb-controller-method-one")) {
                Set<ApiVerb> expectedVerbs = new HashSet<>(Arrays.asList(ApiVerb.values()));
                Assert.assertEquals(expectedVerbs, apiOperationDoc.getVerbs());
            }
            if (apiOperationDoc.getPaths().contains("/api-verb/spring-api-verb-controller-method-two")) {
                Assert.assertEquals(2, apiOperationDoc.getVerbs().size());
            }
        }
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(value = "/api-verb-2", method = {RequestMethod.POST, RequestMethod.PUT})
    public class SpringApiVerbController2 {

        @RequestMapping("/spring-api-verb-controller-method-one")
        public void apiVerbOne() {
        }
    }

    @Test
    public void testApiVerb2() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringApiVerbController2.class);
        Assert.assertEquals("SpringApiVerbController2", apiDoc.getName());
        Assert.assertEquals(1, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/api-verb-2/spring-api-verb-controller-method-one")) {
                Assert.assertEquals(2, apiOperationDoc.getVerbs().size());
            }
        }
    }
}
