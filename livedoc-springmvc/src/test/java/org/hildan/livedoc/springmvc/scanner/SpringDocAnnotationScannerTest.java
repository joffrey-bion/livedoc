package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;
import java.util.Set;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.errors.ApiError;
import org.hildan.livedoc.core.annotations.errors.ApiErrors;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SpringDocAnnotationScannerTest {

    @SuppressWarnings("unused")
    @Api(description = "A spring controller", name = "Spring controller")
    @RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiAuthNone
    @ApiVersion(since = "1.0")
    @ApiErrors({@ApiError(code = "100", description = "error-100")})
    private class SpringController {

        @ApiOperation(description = "Gets a string", path = "/wrongOnPurpose", verbs = ApiVerb.GET)
        @RequestMapping(value = "/string/{name}", headers = "header=test", params = "delete",
                method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        public @ApiResponseBodyType
        @ResponseBody
        String string(@ApiPathParam(name = "name") @PathVariable("test") String name,
                @ApiQueryParam @RequestParam("id") Integer id,
                @ApiQueryParam(name = "query", defaultValue = "test") @RequestParam("myquery") Long query,
                @RequestBody @ApiRequestBodyType String requestBody) {
            return "ok";
        }
    }

    @Test
    public void testMergeApiDoc() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        Assert.assertEquals("A spring controller", apiDoc.getDescription());
        Assert.assertEquals("Spring controller", apiDoc.getName());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/api/string/{name}")) {
                Assert.assertNotNull(apiOperationDoc.getAuth());
                Assert.assertNotNull(apiOperationDoc.getSupportedVersions());
                Assert.assertFalse(apiOperationDoc.getApiErrors().isEmpty());

                Assert.assertEquals("String", apiOperationDoc.getRequestBody().getType().getOneLineText());
                Assert.assertEquals("String", apiOperationDoc.getResponseBodyType().getOneLineText());
                Assert.assertEquals("/api/string/{name}", apiOperationDoc.getPaths().iterator().next());
                Assert.assertEquals("POST", apiOperationDoc.getVerbs().iterator().next().name());
                Assert.assertEquals("application/json", apiOperationDoc.getProduces().iterator().next());
                Assert.assertEquals("application/json", apiOperationDoc.getConsumes().iterator().next());
                Assert.assertEquals("201 - Created", apiOperationDoc.getResponseStatusCode());

                Set<ApiHeaderDoc> headers = apiOperationDoc.getHeaders();
                ApiHeaderDoc header = headers.iterator().next();
                Assert.assertEquals("header", header.getName());
                Assert.assertEquals("test", header.getValues().get(0));

                Set<ApiParamDoc> queryparameters = apiOperationDoc.getQueryParameters();
                Assert.assertEquals(3, queryparameters.size());
                Iterator<ApiParamDoc> qpIterator = queryparameters.iterator();
                ApiParamDoc apiParamDoc = qpIterator.next();
                Assert.assertEquals("delete", apiParamDoc.getName());
                Assert.assertEquals("true", apiParamDoc.getRequired());
                Assert.assertEquals(null, apiParamDoc.getDefaultValue());
                Assert.assertEquals(0, apiParamDoc.getAllowedValues().length);
                apiParamDoc = qpIterator.next();
                Assert.assertEquals("id", apiParamDoc.getName());
                Assert.assertEquals("true", apiParamDoc.getRequired());
                Assert.assertTrue(apiParamDoc.getDefaultValue().isEmpty());
                apiParamDoc = qpIterator.next();
                Assert.assertEquals("myquery", apiParamDoc.getName());
                Assert.assertEquals("true", apiParamDoc.getRequired());
                Assert.assertEquals("", apiParamDoc.getDefaultValue());

                Set<ApiParamDoc> pathparameters = apiOperationDoc.getPathParameters();
                Iterator<ApiParamDoc> ppIterator = pathparameters.iterator();
                ppIterator.next();
                apiParamDoc = apiOperationDoc.getPathParameters().iterator().next();
                Assert.assertEquals("test", apiParamDoc.getName());
            }
        }

    }

}
