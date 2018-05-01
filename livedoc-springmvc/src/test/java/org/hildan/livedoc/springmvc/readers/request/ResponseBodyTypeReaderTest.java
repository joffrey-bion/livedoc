package org.hildan.livedoc.springmvc.readers.request;

import java.util.Map;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class ResponseBodyTypeReaderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping("/response-one")
        public String string() {
            return "";
        }

        @RequestMapping("/response-two")
        public ResponseEntity<String> responseEntityString() {
            return ResponseEntity.ok("");
        }

        @RequestMapping("/response-three")
        public ResponseEntity<Map<String, Integer>> responseEntityMap() {
            return ResponseEntity.ok(null);
        }

    }

    @Test
    public void testApiVerb() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/response-one")) {
                Assert.assertEquals("String", apiOperationDoc.getResponseBodyType().getOneLineText());
            }
            if (apiOperationDoc.getPaths().contains("/response-two")) {
                Assert.assertEquals("String", apiOperationDoc.getResponseBodyType().getOneLineText());
            }
            if (apiOperationDoc.getPaths().contains("/response-three")) {
                Assert.assertEquals("Map<String, Integer>", apiOperationDoc.getResponseBodyType().getOneLineText());
            }
        }
    }

}
