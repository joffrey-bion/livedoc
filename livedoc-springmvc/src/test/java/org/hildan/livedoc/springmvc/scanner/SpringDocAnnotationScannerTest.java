package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiAuthNone;
import org.hildan.livedoc.core.annotation.ApiBodyObject;
import org.hildan.livedoc.core.annotation.ApiError;
import org.hildan.livedoc.core.annotation.ApiErrors;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.annotation.ApiPathParam;
import org.hildan.livedoc.core.annotation.ApiQueryParam;
import org.hildan.livedoc.core.annotation.ApiResponseObject;
import org.hildan.livedoc.core.annotation.ApiVersion;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiHeaderDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiParamDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
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

    private DocAnnotationScanner scanner = new SpringDocAnnotationScanner();

    @Api(description = "A spring controller", name = "Spring controller")
    @RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiAuthNone
    @ApiVersion(since = "1.0")
    @ApiErrors(apierrors = {@ApiError(code = "100", description = "error-100")})
    private class SpringController {

        @ApiMethod(description = "Gets a string", path = "/wrongOnPurpose", verb = ApiVerb.GET)
        @RequestMapping(value = "/string/{name}", headers = "header=test", params = "delete",
                method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(value = HttpStatus.CREATED)
        public @ApiResponseObject
        @ResponseBody
        String string(@ApiPathParam(name = "name") @PathVariable(value = "test") String name,
                @ApiQueryParam @RequestParam("id") Integer id,
                @ApiQueryParam(name = "query", defaultvalue = "test") @RequestParam(value = "myquery") Long query,
                @RequestBody @ApiBodyObject String requestBody) {
            return "ok";
        }
    }

    @Test
    public void testMergeApiDoc() {
        Set<Class<?>> controllers = new LinkedHashSet<>();
        controllers.add(SpringController.class);
        Set<ApiDoc> apiDocs = scanner.getApiDocs(controllers, MethodDisplay.URI);

        ApiDoc apiDoc = apiDocs.iterator().next();
        Assert.assertEquals("A spring controller", apiDoc.getDescription());
        Assert.assertEquals("Spring controller", apiDoc.getName());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api/string/{name}")) {
                Assert.assertNotNull(apiMethodDoc.getAuth());
                Assert.assertNotNull(apiMethodDoc.getSupportedversions());
                Assert.assertFalse(apiMethodDoc.getApierrors().isEmpty());

                Assert.assertEquals("String", apiMethodDoc.getBodyobject().getLivedocType().getOneLineText());
                Assert.assertEquals("String", apiMethodDoc.getResponse().getLivedocType().getOneLineText());
                Assert.assertEquals("/api/string/{name}", apiMethodDoc.getPath().iterator().next());
                Assert.assertEquals("POST", apiMethodDoc.getVerb().iterator().next().name());
                Assert.assertEquals("application/json", apiMethodDoc.getProduces().iterator().next());
                Assert.assertEquals("application/json", apiMethodDoc.getConsumes().iterator().next());
                Assert.assertEquals("201 - Created", apiMethodDoc.getResponsestatuscode());

                Set<ApiHeaderDoc> headers = apiMethodDoc.getHeaders();
                ApiHeaderDoc header = headers.iterator().next();
                Assert.assertEquals("header", header.getName());
                Assert.assertEquals("test", header.getAllowedvalues()[0]);

                Set<ApiParamDoc> queryparameters = apiMethodDoc.getQueryparameters();
                Assert.assertEquals(3, queryparameters.size());
                Iterator<ApiParamDoc> qpIterator = queryparameters.iterator();
                ApiParamDoc apiParamDoc = qpIterator.next();
                Assert.assertEquals("delete", apiParamDoc.getName());
                Assert.assertEquals("true", apiParamDoc.getRequired());
                Assert.assertEquals(null, apiParamDoc.getDefaultvalue());
                Assert.assertEquals(0, apiParamDoc.getAllowedvalues().length);
                apiParamDoc = qpIterator.next();
                Assert.assertEquals("id", apiParamDoc.getName());
                Assert.assertEquals("true", apiParamDoc.getRequired());
                Assert.assertTrue(apiParamDoc.getDefaultvalue().isEmpty());
                apiParamDoc = qpIterator.next();
                Assert.assertEquals("myquery", apiParamDoc.getName());
                Assert.assertEquals("true", apiParamDoc.getRequired());
                Assert.assertEquals("", apiParamDoc.getDefaultvalue());

                Set<ApiParamDoc> pathparameters = apiMethodDoc.getPathparameters();
                Iterator<ApiParamDoc> ppIterator = pathparameters.iterator();
                apiParamDoc = ppIterator.next();
                apiParamDoc = apiMethodDoc.getPathparameters().iterator().next();
                Assert.assertEquals("test", apiParamDoc.getName());
            }
        }

    }

}
