package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringRequestBodyBuilderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping("/body-one")
        public void bodyOne(@RequestBody String string) {

        }

        @RequestMapping("/body-two")
        public void bodyTwo(@RequestBody Body body) {

        }

    }

    @SuppressWarnings("unused")
    @ApiType
    private class Body {
        @ApiTypeProperty
        private String name;

        @ApiTypeProperty
        private Integer age;
    }

    @Test
    public void testBodyOne() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/body-one")) {
                Assert.assertNotNull(apiMethodDoc.getRequestBody());
                Assert.assertEquals("String", apiMethodDoc.getRequestBody().getType().getOneLineText());
            }
            if (apiMethodDoc.getPaths().contains("/body-two")) {
                Assert.assertNotNull(apiMethodDoc.getRequestBody());
                Assert.assertEquals("Body", apiMethodDoc.getRequestBody().getType().getOneLineText());
            }
        }
    }

}
