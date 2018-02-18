package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SpringResponseStatusBuilderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping("/status-one")
        @ResponseStatus(HttpStatus.CREATED)
        public String statusOne() {
            return "";
        }

        @RequestMapping("/status-two")
        public void statusTwo() {

        }

    }

    @Test
    public void testApiVerb() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/status-one")) {
                Assert.assertEquals("201 - Created", apiMethodDoc.getResponseStatusCode());
            }
            if (apiMethodDoc.getPaths().contains("/status-two")) {
                Assert.assertEquals("200 - OK", apiMethodDoc.getResponseStatusCode());
            }
        }
    }

}
