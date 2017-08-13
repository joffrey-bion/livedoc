package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Sets;

public class SpringApiVerbDocTest {

    private DocAnnotationScanner scanner = new Spring3DocAnnotationScanner();

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

    @Controller
    @RequestMapping(value = "/api-verb-2", method = {RequestMethod.POST, RequestMethod.PUT})
    public class SpringApiVerbController2 {

        @RequestMapping(value = "/spring-api-verb-controller-method-one")
        public void apiVerbOne() {

        }

    }

    @Test
    public void testApiVerb() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(SpringApiVerbController.class),
                MethodDisplay.URI).iterator().next();
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

        apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(SpringApiVerbController2.class), MethodDisplay.URI)
                               .iterator()
                               .next();
        Assert.assertEquals("SpringApiVerbController2", apiDoc.getName());
        Assert.assertEquals(1, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api-verb-2/spring-api-verb-controller-method-one")) {
                Assert.assertEquals(2, apiMethodDoc.getVerb().size());
            }
        }

    }

}
