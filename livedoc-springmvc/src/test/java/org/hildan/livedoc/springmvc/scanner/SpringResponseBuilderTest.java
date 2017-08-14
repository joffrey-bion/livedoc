package org.hildan.livedoc.springmvc.scanner;

import java.util.Map;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

public class SpringResponseBuilderTest {

    private DocAnnotationScanner scanner = new SpringDocAnnotationScanner();

    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping(value = "/response-one")
        public String string() {
            return "";
        }

        @RequestMapping(value = "/response-two")
        public ResponseEntity<String> responseEntityString() {
            return ResponseEntity.ok("");
        }

        @RequestMapping(value = "/response-three")
        public ResponseEntity<Map<String, Integer>> responseEntityMap() {
            return ResponseEntity.ok(null);
        }

    }

    @Test
    public void testApiVerb() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(SpringController.class), MethodDisplay.URI)
                                      .iterator()
                                      .next();
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/response-one")) {
                Assert.assertEquals("String", apiMethodDoc.getResponse().getJsondocType().getOneLineText());
            }
            if (apiMethodDoc.getPath().contains("/response-two")) {
                Assert.assertEquals("String", apiMethodDoc.getResponse().getJsondocType().getOneLineText());
            }
            if (apiMethodDoc.getPath().contains("/response-three")) {
                Assert.assertEquals("Map[String, Integer]",
                        apiMethodDoc.getResponse().getJsondocType().getOneLineText());
            }
        }
    }

}
