package org.hildan.livedoc.springmvc.scanner;

import java.util.List;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.hildan.livedoc.core.model.doc.headers.HeaderFilterType;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SpringApiHeadersDocTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(headers = {"h1", "h2=val2"})
    public class SpringApiHeadersController {

        @RequestMapping("/two")
        public void onlyClass() {
        }

        @RequestMapping(value = "/three", headers = {"h3!=forbiddenVal"})
        public void classAndMethodAnnotation() {
        }

        @RequestMapping(value = "/four", headers = {"!h4"})
        public void classAndMethodAndParamAnnotation(@RequestHeader(name = "h5", required = false) String header5) {
        }

        @RequestMapping(value = "/override", headers = {"h2=prefix/*"})
        public void overrideWithMethodAnnotation(@RequestHeader(name = "h1", defaultValue = "def") String header1) {
        }
    }

    @Test
    public void testHeaders() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringApiHeadersController.class);
        assertEquals("SpringApiHeadersController", apiDoc.getName());
        assertEquals(4, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/two")) {
                List<ApiHeaderDoc> headers = apiOperationDoc.getHeaders();
                assertEquals(2, headers.size());
                checkH1(headers.get(0));
                checkH2(headers.get(1));
            }
            if (apiOperationDoc.getPaths().contains("/three")) {
                List<ApiHeaderDoc> headers = apiOperationDoc.getHeaders();
                assertEquals(3, headers.size());
                checkH1(headers.get(0));
                checkH2(headers.get(1));
                checkH3(headers.get(2));
            }
            if (apiOperationDoc.getPaths().contains("/four")) {
                List<ApiHeaderDoc> headers = apiOperationDoc.getHeaders();
                assertEquals(4, headers.size());
                checkH1(headers.get(0));
                checkH2(headers.get(1));
                checkH4(headers.get(2));
                checkH5(headers.get(3));
            }
            if (apiOperationDoc.getPaths().contains("/override")) {
                List<ApiHeaderDoc> headers = apiOperationDoc.getHeaders();
                assertEquals(2, headers.size());
                checkH1Overridden(headers.get(0));
                checkH2Overridden(headers.get(1));
            }
        }
    }

    private static void checkH1(ApiHeaderDoc header) {
        assertEquals("h1", header.getName());
        assertEquals(HeaderFilterType.REQUIRED_MATCHING, header.getType());
        assertEquals("*", header.getValues().get(0));
        assertNull(header.getDefaultValue());
    }

    private static void checkH2(ApiHeaderDoc header) {
        assertEquals("h2", header.getName());
        assertEquals(HeaderFilterType.REQUIRED_MATCHING, header.getType());
        assertEquals("val2", header.getValues().get(0));
        assertNull(header.getDefaultValue());
    }

    private static void checkH3(ApiHeaderDoc header) {
        assertEquals("h3", header.getName());
        assertEquals(HeaderFilterType.DIFFERENT, header.getType());
        assertEquals("forbiddenVal", header.getValues().get(0));
        assertNull(header.getDefaultValue());
    }

    private static void checkH4(ApiHeaderDoc header) {
        assertEquals("h4", header.getName());
        assertEquals(HeaderFilterType.FORBIDDEN, header.getType());
        assertTrue(header.getValues().isEmpty());
        assertNull(header.getDefaultValue());
    }

    private static void checkH5(ApiHeaderDoc header) {
        assertEquals("h5", header.getName());
        assertEquals(HeaderFilterType.OPTIONAL, header.getType());
        assertTrue(header.getValues().isEmpty());
        assertNull(header.getDefaultValue());
    }

    private static void checkH1Overridden(ApiHeaderDoc header) {
        assertEquals("h1", header.getName());
        assertEquals(HeaderFilterType.OPTIONAL, header.getType());
        assertTrue(header.getValues().isEmpty());
        assertEquals("def", header.getDefaultValue());
    }

    private static void checkH2Overridden(ApiHeaderDoc header) {
        assertEquals("h2", header.getName());
        assertEquals(HeaderFilterType.REQUIRED_MATCHING, header.getType());
        assertEquals("prefix/*", header.getValues().get(0));
        assertNull(header.getDefaultValue());
    }
}
