package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
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

        @RequestMapping(value = "/body-one")
        public void bodyOne(@RequestBody String string) {

        }

        @RequestMapping(value = "/body-two")
        public void bodyTwo(@RequestBody Body body) {

        }

    }

    @SuppressWarnings("unused")
    @ApiObject
    private class Body {
        @ApiObjectProperty
        private String name;

        @ApiObjectProperty
        private Integer age;
    }

    @Test
    public void testBodyOne() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class, MethodDisplay.URI);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/body-one")) {
                Assert.assertNotNull(apiMethodDoc.getBodyobject());
                Assert.assertEquals("String", apiMethodDoc.getBodyobject().getType().getOneLineText());
            }
            if (apiMethodDoc.getPath().contains("/body-two")) {
                Assert.assertNotNull(apiMethodDoc.getBodyobject());
                Assert.assertEquals("Body", apiMethodDoc.getBodyobject().getType().getOneLineText());
            }
        }
    }

}
