package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
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

        @RequestMapping(value = "/status-one")
        @ResponseStatus(value = HttpStatus.CREATED)
        public String statusOne() {
            return "";
        }

        @RequestMapping(value = "/status-two")
        public void statusTwo() {

        }

    }

    @Test
    public void testApiVerb() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/status-one")) {
                Assert.assertEquals("201 - Created", apiMethodDoc.getResponsestatuscode());
            }
            if (apiMethodDoc.getPath().contains("/status-two")) {
                Assert.assertEquals("200 - OK", apiMethodDoc.getResponsestatuscode());
            }
        }
    }

}
