package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringApiHeadersDocTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(headers = {"h1", "h2"})
    public class SpringApiHeadersController {

        @RequestMapping("/spring-api-headers-controller-method-one")
        public void apiHeadersMethodOne() {

        }

        @RequestMapping(value = "/spring-api-headers-controller-method-two", headers = {"h3"})
        public void apiHeadersMethodTwo() {

        }

        @RequestMapping(value = "/spring-api-headers-controller-method-three", headers = {"h4"})
        public void apiHeadersMethodThree(@RequestHeader("h5") String h5) {

        }

    }

    @SuppressWarnings("unused")
    @Test
    public void testApiHeadersOnClass() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringApiHeadersController.class);
        Assert.assertEquals("SpringApiHeadersController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/spring-api-headers-controller-method-one")) {
                Assert.assertEquals(2, apiMethodDoc.getHeaders().size());
            }
            if (apiMethodDoc.getPaths().contains("/spring-api-headers-controller-method-two")) {
                Assert.assertEquals(3, apiMethodDoc.getHeaders().size());
            }
            if (apiMethodDoc.getPaths().contains("/spring-api-headers-controller-method-three")) {
                Assert.assertEquals(4, apiMethodDoc.getHeaders().size());
                Iterator<ApiHeaderDoc> headers = apiMethodDoc.getHeaders().iterator();
                ApiHeaderDoc h1 = headers.next();
                ApiHeaderDoc h2 = headers.next();
                ApiHeaderDoc h4 = headers.next();
                Assert.assertEquals("h4", h4.getName());
                ApiHeaderDoc h5 = headers.next();
                Assert.assertEquals("h5", h5.getName());
            }
        }
    }

}
