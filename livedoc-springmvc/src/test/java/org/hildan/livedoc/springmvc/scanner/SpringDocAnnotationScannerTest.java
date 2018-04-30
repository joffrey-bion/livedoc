package org.hildan.livedoc.springmvc.scanner;

import java.util.List;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.errors.ApiError;
import org.hildan.livedoc.core.annotations.errors.ApiErrors;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.AuthType;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.doc.auth.AuthDoc;
import org.hildan.livedoc.core.model.doc.headers.HeaderDoc;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class SpringDocAnnotationScannerTest {

    /**
     * Test controller.
     */
    @SuppressWarnings("unused")
    @Api(description = "A spring controller", name = "Spring controller")
    @RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiAuthNone
    @ApiVersion(since = "1.0")
    @ApiErrors({@ApiError(code = "100", description = "error-100")})
    private class SpringController {

        /**
         * Gets an integer.
         *
         * @param input
         *         a test input
         *
         * @return something
         */
        @GetMapping
        @ResponseBody
        public Integer integer(@RequestParam String input) {
            return 0;
        }

        @ApiOperation(description = "Gets a string", path = "/overridden", verbs = ApiVerb.GET)
        @RequestMapping(value = "/string/{name}",
                headers = "header=test",
                params = "delete",
                method = RequestMethod.POST,
                consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        @ApiResponseBodyType
        @ResponseBody
        public String string(@ApiPathParam(name = "name") @PathVariable("test") String name,
                @ApiQueryParam @RequestParam("id") Integer id,
                @ApiQueryParam(name = "query", defaultValue = "test") @RequestParam("myquery") Long query,
                @RequestBody @ApiRequestBodyType String requestBody) {
            return "ok";
        }
    }

    @Test
    public void testMergeApiDoc() {
        if (!JavadocHelper.getJavadocDescription(SpringController.class).isPresent()) {
            fail("Javadoc processor was not enabled");
        }
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        assertEquals("A spring controller", apiDoc.getDescription());
        assertEquals("Spring controller", apiDoc.getName());

        List<ApiOperationDoc> operations = apiDoc.getOperations();
        assertEquals(2, operations.size());

        checkIntegerOperation(operations.get(0));
        checkStringOperation(operations.get(1));
    }

    private static void checkIntegerOperation(ApiOperationDoc opDoc) {
        assertEquals("integer", opDoc.getName());
        assertEquals("Gets an integer.", opDoc.getDescription());

        assertAuthNone(opDoc.getAuth());
        assertVersion(opDoc.getSupportedVersions());
        assertFalse(opDoc.getApiErrors().isEmpty());

        assertNull(opDoc.getRequestBody());
        assertEquals("Integer", opDoc.getResponseBodyType().getOneLineText());

        assertEquals(1, opDoc.getVerbs().size());
        assertEquals(ApiVerb.GET, opDoc.getVerbs().get(0));

        List<ParamDoc> queryParameters = opDoc.getQueryParameters();
        assertEquals(1, queryParameters.size());

        ParamDoc paramDoc = queryParameters.get(0);
        assertEquals("input", paramDoc.getName());
        assertEquals("a test input", paramDoc.getDescription());
        assertEquals("true", paramDoc.getRequired());
        assertNull(paramDoc.getDefaultValue());
        assertEquals(0, paramDoc.getAllowedValues().length);
    }

    private static void checkStringOperation(ApiOperationDoc opDoc) {
        assertEquals("string", opDoc.getName());
        assertEquals("Gets a string", opDoc.getDescription());

        assertAuthNone(opDoc.getAuth());
        assertVersion(opDoc.getSupportedVersions());
        assertFalse(opDoc.getApiErrors().isEmpty());

        assertEquals("String", opDoc.getRequestBody().getType().getOneLineText());
        assertEquals("String", opDoc.getResponseBodyType().getOneLineText());

        // livedoc annotations override Spring config
        assertEquals("/overridden", opDoc.getPaths().get(0));
        assertEquals(1, opDoc.getVerbs().size());
        assertEquals(ApiVerb.GET, opDoc.getVerbs().get(0));

        assertEquals("application/json", opDoc.getProduces().get(0));
        assertEquals("application/json", opDoc.getConsumes().get(0));
        assertEquals("201 - Created", opDoc.getResponseStatusCode());

        List<HeaderDoc> headers = opDoc.getHeaders();
        HeaderDoc header = headers.get(0);
        assertEquals("header", header.getName());
        assertEquals("test", header.getValues().get(0));

        List<ParamDoc> queryParameters = opDoc.getQueryParameters();
        assertEquals(3, queryParameters.size());

        ParamDoc paramDoc = queryParameters.get(0);
        assertEquals("delete", paramDoc.getName());
        assertEquals("true", paramDoc.getRequired());
        assertNull(paramDoc.getDefaultValue());
        assertEquals(0, paramDoc.getAllowedValues().length);

        paramDoc = queryParameters.get(1);
        assertEquals("id", paramDoc.getName());
        assertEquals("true", paramDoc.getRequired());
        assertNull(paramDoc.getDefaultValue());

        paramDoc = queryParameters.get(2);
        assertEquals("query", paramDoc.getName());
        assertEquals("true", paramDoc.getRequired());
        assertEquals("test", paramDoc.getDefaultValue());

        paramDoc = opDoc.getPathParameters().get(0);
        assertEquals("name", paramDoc.getName());
    }

    private static void assertAuthNone(AuthDoc authDoc) {
        assertEquals(AuthType.NONE, authDoc.getType());
    }

    private static void assertVersion(VersionDoc supportedVersions) {
        assertEquals("1.0", supportedVersions.getSince());
        assertNull(supportedVersions.getUntil());
    }
}
