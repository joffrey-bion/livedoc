package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;
import java.util.List;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.doc.headers.HeaderDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
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

        @RequestMapping(name = "/string/{name}",
                headers = "header=test",
                params = "delete",
                method = RequestMethod.POST,
                consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        public @ResponseBody
        String string(@PathVariable("test") String name, @RequestParam("id") Integer id, @RequestParam Long query,
                @RequestHeader(name = "header-two", defaultValue = "header-test") String header,
                @RequestBody String requestBody) {
            return "ok";
        }

        private void someUtilityMethod() {
            // should not be documented
        }
    }

    @Test
    public void testMergeApiDoc() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        assertEquals("SpringController", apiDoc.getName());
        assertNotNull(apiDoc.getGroup());
        assertEquals(1, apiDoc.getOperations().size());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            assertNull(apiOperationDoc.getAuth());
            assertNull(apiOperationDoc.getSupportedVersions());
            assertTrue(apiOperationDoc.getApiErrors().isEmpty());
            assertNull(apiOperationDoc.getId());
            assertNull(apiOperationDoc.getSummary());
            assertNull(apiOperationDoc.getDescription());

            if (apiOperationDoc.getPaths().contains("/api/string/{name}")) {
                assertEquals(2, apiOperationDoc.getHeaders().size());
                List<HeaderDoc> headers = apiOperationDoc.getHeaders();
                Iterator<HeaderDoc> headersIterator = headers.iterator();
                HeaderDoc headerTest = headersIterator.next();
                assertEquals("header", headerTest.getName());
                assertEquals("test", headerTest.getValues().get(0));
                HeaderDoc headerTwo = headersIterator.next();
                assertEquals("header-two", headerTwo.getName());
                assertEquals("header-test", headerTwo.getValues().get(0));

                assertEquals("String", apiOperationDoc.getRequestBody().getType().getOneLineText());
                assertEquals("String", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("POST", apiOperationDoc.getVerbs().iterator().next().name());
                assertEquals("application/json", apiOperationDoc.getProduces().iterator().next());
                assertEquals("application/json", apiOperationDoc.getConsumes().iterator().next());
                assertEquals("201 - Created", apiOperationDoc.getResponseStatusCode());

                List<ParamDoc> queryparameters = apiOperationDoc.getQueryParameters();
                assertEquals(3, queryparameters.size());
                ParamDoc paramDoc = queryparameters.get(0);
                assertEquals("delete", paramDoc.getName());
                assertEquals("true", paramDoc.getRequired());
                assertNull(paramDoc.getDefaultValue());
                assertEquals(0, paramDoc.getAllowedValues().length);

                paramDoc = queryparameters.get(1);
                assertEquals("id", paramDoc.getName());
                assertEquals("true", paramDoc.getRequired());
                assertTrue(paramDoc.getDefaultValue().isEmpty());

                paramDoc = queryparameters.get(2);
                assertEquals("query", paramDoc.getName());
                assertEquals("true", paramDoc.getRequired());
                assertNull(paramDoc.getDefaultValue());

                paramDoc = apiOperationDoc.getPathParameters().get(0);
                assertEquals("test", paramDoc.getName());
            }
        }

    }

}
