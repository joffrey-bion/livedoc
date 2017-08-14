package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

public class SpringRequestBodyBuilderTest {

    private DocAnnotationScanner scanner = new SpringDocAnnotationScanner();

    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping(value = "/body-one")
        public void bodyOne(@RequestBody String string) {

        }

        @RequestMapping(value = "/body-two")
        public void bodyTwo(@RequestBody Body body) {

        }

    }

    @ApiObject
    private class Body {
        @ApiObjectField
        private String name;

        @ApiObjectField
        private Integer age;
    }

    @Test
    public void testBodyOne() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(SpringController.class), MethodDisplay.URI)
                                      .iterator()
                                      .next();
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/body-one")) {
                Assert.assertNotNull(apiMethodDoc.getBodyobject());
                Assert.assertEquals("String", apiMethodDoc.getBodyobject().getJsondocType().getOneLineText());
            }
            if (apiMethodDoc.getPath().contains("/body-two")) {
                Assert.assertNotNull(apiMethodDoc.getBodyobject());
                Assert.assertEquals("Body", apiMethodDoc.getBodyobject().getJsondocType().getOneLineText());
            }
        }
    }

}
