package org.hildan.livedoc.springmvc.scanner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PlainSpringDocAnnotationScannerTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(name = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
    private class SpringController {

        @RequestMapping(name = "/string/{name}", headers = "header=test", params = "delete",
                method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        public @ResponseBody
        String string(@PathVariable("test") String name, @RequestParam("id") Integer id,
                @RequestParam Long query,
                @RequestHeader(name = "header-two", defaultValue = "header-test") String header,
                @RequestBody String requestBody) {
            return "ok";
        }

        private void someUtilityMethod() {
            // should not be documented
        }
    }

    private ApiDoc buildDocFor(Class<?> controller, MethodDisplay methodDisplay) {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        Optional<ApiDoc> apiDoc = builder.readApiDoc(controller, methodDisplay);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }

    @Test
    public void testMergeApiDoc() {
        ApiDoc apiDoc = buildDocFor(SpringController.class, MethodDisplay.URI);
        assertEquals("SpringController", apiDoc.getName());
        assertNotNull(apiDoc.getGroup());
        assertEquals(1, apiDoc.getMethods().size());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            assertEquals(MethodDisplay.URI, apiMethodDoc.getDisplayMethodAs());
            assertNull(apiMethodDoc.getAuth());
            assertNull(apiMethodDoc.getSupportedVersions());
            assertTrue(apiMethodDoc.getApiErrors().isEmpty());
            assertNull(apiMethodDoc.getId());
            assertEquals("", apiMethodDoc.getSummary());
            assertEquals("", apiMethodDoc.getDescription());

            if (apiMethodDoc.getPaths().contains("/api/string/{name}")) {
                assertEquals(2, apiMethodDoc.getHeaders().size());
                Set<ApiHeaderDoc> headers = apiMethodDoc.getHeaders();
                Iterator<ApiHeaderDoc> headersIterator = headers.iterator();
                ApiHeaderDoc headerTest = headersIterator.next();
                assertEquals("header", headerTest.getName());
                assertEquals("test", headerTest.getValues().get(0));
                ApiHeaderDoc headerTwo = headersIterator.next();
                assertEquals("header-two", headerTwo.getName());
                assertEquals("header-test", headerTwo.getValues().get(0));

                assertEquals("String", apiMethodDoc.getRequestBody().getType().getOneLineText());
                assertEquals("String", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("POST", apiMethodDoc.getVerbs().iterator().next().name());
                assertEquals("application/json", apiMethodDoc.getProduces().iterator().next());
                assertEquals("application/json", apiMethodDoc.getConsumes().iterator().next());
                assertEquals("201 - Created", apiMethodDoc.getResponseStatusCode());

                Set<ApiParamDoc> queryparameters = apiMethodDoc.getQueryParameters();
                assertEquals(3, queryparameters.size());
                Iterator<ApiParamDoc> qpIterator = queryparameters.iterator();
                ApiParamDoc apiParamDoc = qpIterator.next();
                assertEquals("delete", apiParamDoc.getName());
                assertEquals("true", apiParamDoc.getRequired());
                assertNull(apiParamDoc.getDefaultValue());
                assertEquals(0, apiParamDoc.getAllowedValues().length);
                apiParamDoc = qpIterator.next();
                assertEquals("id", apiParamDoc.getName());
                assertEquals("true", apiParamDoc.getRequired());
                assertTrue(apiParamDoc.getDefaultValue().isEmpty());
                apiParamDoc = qpIterator.next();
                assertEquals("query", apiParamDoc.getName());
                assertEquals("true", apiParamDoc.getRequired());
                assertEquals("", apiParamDoc.getDefaultValue());

                Set<ApiParamDoc> pathparameters = apiMethodDoc.getPathParameters();
                Iterator<ApiParamDoc> ppIterator = pathparameters.iterator();
                ppIterator.next();
                apiParamDoc = apiMethodDoc.getPathParameters().iterator().next();
                assertEquals("test", apiParamDoc.getName());
            }
        }

    }

}
