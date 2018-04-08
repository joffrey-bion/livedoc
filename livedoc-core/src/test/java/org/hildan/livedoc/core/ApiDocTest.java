package org.hildan.livedoc.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.ApiStage;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.hildan.livedoc.core.model.doc.ApiAuthType;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiErrorDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.Stage;
import org.hildan.livedoc.core.readers.annotation.ApiAuthDocReader;
import org.hildan.livedoc.core.test.controller.Test3Controller;
import org.hildan.livedoc.core.test.pojo.Child;
import org.hildan.livedoc.core.test.pojo.Pizza;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ApiDocTest {

    @SuppressWarnings("unused")
    @Api(name = "test-controller", description = "a-test-controller")
    @ApiVersion(since = "1.0", until = "2.12")
    @ApiAuthNone
    private class TestController {

        @ApiOperation(path = "/name", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public String name(@ApiPathParam(name = "name") String name, @ApiRequestBodyType String body) {
            return null;
        }

        @ApiOperation(path = "/age", verbs = ApiVerb.GET, description = "a-test-method", responseStatusCode = "204")
        @ApiResponseBodyType
        public Integer age(@ApiPathParam(name = "age") Integer age, @ApiRequestBodyType Integer body) {
            return null;
        }

        @ApiOperation(path = "/avg", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public Long avg(@ApiPathParam(name = "avg") Long avg, @ApiRequestBodyType Long body) {
            return null;
        }

        @ApiOperation(path = "/map", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public Map<String, Integer> map(@ApiPathParam(name = "map") Map<String, Integer> map,
                @ApiRequestBodyType Map<String, Integer> body) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        @ApiOperation(path = "/unparametrizedList", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public List unparametrizedList(@ApiPathParam(name = "unparametrizedList") List unparametrizedList,
                @ApiRequestBodyType List body) {
            return null;
        }

        @ApiOperation(path = "/parametrizedList", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public List<String> parametrizedList(@ApiPathParam(name = "parametrizedList") List<String> parametrizedList,
                @ApiRequestBodyType List<String> body) {
            return null;
        }

        @ApiOperation(path = "/wildcardParametrizedList", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public List<?> wildcardParametrizedList(
                @ApiPathParam(name = "wildcardParametrizedList") List<?> wildcardParametrizedList,
                @ApiRequestBodyType List<?> body) {
            return null;
        }

        @ApiOperation(path = "/LongArray", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public Long[] longObjArray(@ApiPathParam(name = "LongArray") Long[] longObjArray,
                @ApiRequestBodyType Long[] body) {
            return null;
        }

        @ApiOperation(path = "/longArray", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public long[] longArray(@ApiPathParam(name = "longArray") long[] longArray, @ApiRequestBodyType long[] body) {
            return null;
        }

        @ApiOperation(path = "/version", verbs = ApiVerb.GET, description = "a-test-method for api version feature")
        @ApiVersion(since = "1.0", until = "2.12")
        @ApiResponseBodyType
        public String version(@ApiPathParam(name = "version") String version, @ApiRequestBodyType String body) {
            return null;
        }

        @ApiOperation(path = "/child", description = "A method returning a child", verbs = ApiVerb.GET)
        @ApiResponseBodyType
        public Child child(@ApiPathParam(name = "child") Child child, @ApiRequestBodyType Child body) {
            return null;
        }

        @ApiOperation(path = "/pizza", description = "A method returning a pizza", verbs = ApiVerb.GET)
        @ApiResponseBodyType
        public Pizza pizza(@ApiPathParam(name = "pizza") Pizza pizza, @ApiRequestBodyType Pizza body) {
            return null;
        }

        @ApiOperation(path = "/multiple-request-methods",
                verbs = {ApiVerb.GET, ApiVerb.POST},
                description = "a-test-method-with-multiple-request-methods")
        @ApiResponseBodyType
        public Integer multipleRequestMethods(
                @ApiPathParam(name = "multiple-request-methods") Integer multipleRequestMethods,
                @ApiRequestBodyType Integer body) {
            return null;
        }

    }

    @Test
    public void testApiErrorsDoc() {
        ApiDoc apiDoc = buildDoc(Test3Controller.class);

        List<ApiOperationDoc> methods = apiDoc.getOperations();
        ApiOperationDoc apiOperationDoc = methods.get(0);
        List<ApiErrorDoc> apiErrors = apiOperationDoc.getApiErrors();

        assertEquals(1, methods.size());
        assertEquals(3, apiErrors.size());
        assertEquals("1000", apiErrors.get(0).getCode());
        assertEquals("method-level annotation should be applied", "A test error #1", apiErrors.get(0).getDescription());
        assertEquals("2000", apiErrors.get(1).getCode());
        assertEquals("400", apiErrors.get(2).getCode());

    }

    @Test
    public void testGeneral() {
        ApiDoc apiDoc = buildDoc(TestController.class);
        assertEquals("test-controller", apiDoc.getName());
        assertEquals("a-test-controller", apiDoc.getDescription());
        assertEquals("1.0", apiDoc.getSupportedVersions().getSince());
        assertEquals("2.12", apiDoc.getSupportedVersions().getUntil());
        assertEquals(ApiAuthType.NONE, apiDoc.getAuth().getType());
        assertEquals(ApiAuthDocReader.ANONYMOUS, apiDoc.getAuth().getRoles().get(0));

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {

            if (apiOperationDoc.getPaths().contains("/name")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("String", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("String", apiOperationDoc.getRequestBody().getType().getOneLineText());
                assertEquals("200 - OK", apiOperationDoc.getResponseStatusCode());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("name")) {
                        assertEquals("String", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/age")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("204", apiOperationDoc.getResponseStatusCode());
                assertEquals("Integer", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("Integer", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("age")) {
                        assertEquals("Integer", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/avg")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("Long", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("Long", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("avg")) {
                        assertEquals("Long", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/map")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("Map<String, Integer>", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("Map<String, Integer>", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("Map")) {
                        assertEquals("Map<String, Integer>", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/parametrizedList")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("List<String>", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("List<String>", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("parametrizedList")) {
                        assertEquals("List<String>", apiParamDoc.getType().getOneLineText());
                    }
                }

            }

            if (apiOperationDoc.getPaths().contains("/wildcardParametrizedList")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("List<?>", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("List<?>", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("wildcardParametrizedList")) {
                        assertEquals("List<?>", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/LongArray")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("Long[]", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("Long[]", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("LongArray")) {
                        assertEquals("Long[]", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/longArray")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("long[]", apiOperationDoc.getResponseBodyType().getOneLineText());
                assertEquals("long[]", apiOperationDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("longArray")) {
                        assertEquals("long[]", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiOperationDoc.getPaths().contains("/version")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("1.0", apiOperationDoc.getSupportedVersions().getSince());
                assertEquals("2.12", apiOperationDoc.getSupportedVersions().getUntil());
            }

            if (apiOperationDoc.getPaths().contains("/child")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("child", apiOperationDoc.getResponseBodyType().getOneLineText());
            }

            if (apiOperationDoc.getPaths().contains("/pizza")) {
                assertEquals(ApiVerb.GET, apiOperationDoc.getVerbs().iterator().next());
                assertEquals("customPizzaObject", apiOperationDoc.getResponseBodyType().getOneLineText());
            }

            if (apiOperationDoc.getPaths().contains("/multiple-request-methods")) {
                assertEquals(2, apiOperationDoc.getVerbs().size());
                Iterator<ApiVerb> iterator = apiOperationDoc.getVerbs().iterator();
                assertEquals(ApiVerb.GET, iterator.next());
                assertEquals(ApiVerb.POST, iterator.next());
            }

        }
    }

    @SuppressWarnings("unused")
    @Api(name = "test-controller-with-basic-auth", description = "a-test-controller with basic auth annotation")
    @ApiAuthBasic(roles = {"ROLE_USER", "ROLE_ADMIN"},
            testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
    private class TestControllerWithBasicAuth {

        @ApiOperation(path = "/basicAuth", description = "A method with basic auth", verbs = ApiVerb.GET)
        @ApiAuthBasic(roles = {"ROLE_USER"},
                testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
        public String basicAuth() {
            return null;
        }

        @ApiOperation(path = "/noAuth", description = "A method with no auth", verbs = ApiVerb.GET)
        @ApiAuthNone
        public String noAuth() {
            return null;
        }

        @ApiOperation(path = "/undefinedAuthWithAuthOnClass",
                description = "A method with undefined auth but with auth info on class declaration",
                verbs = ApiVerb.GET)
        public String undefinedAuthWithAuthOnClass() {
            return null;
        }

    }

    @Test
    public void testControllerWithBasicAuth() {
        ApiDoc apiDoc = buildDoc(TestControllerWithBasicAuth.class);
        assertEquals("test-controller-with-basic-auth", apiDoc.getName());
        assertEquals(ApiAuthType.BASIC_AUTH, apiDoc.getAuth().getType());
        assertEquals("ROLE_USER", apiDoc.getAuth().getRoles().get(0));
        assertEquals("ROLE_ADMIN", apiDoc.getAuth().getRoles().get(1));
        assertTrue(apiDoc.getAuth().getTestusers().size() > 0);

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/basicAuth")) {
                assertEquals(ApiAuthType.BASIC_AUTH, apiOperationDoc.getAuth().getType());
                assertEquals("ROLE_USER", apiOperationDoc.getAuth().getRoles().get(0));
                assertTrue(apiOperationDoc.getAuth().getTestusers().size() > 0);
            }

            if (apiOperationDoc.getPaths().contains("/noAuth")) {
                assertEquals(ApiAuthType.NONE, apiOperationDoc.getAuth().getType());
                assertEquals(ApiAuthDocReader.ANONYMOUS, apiOperationDoc.getAuth().getRoles().get(0));
            }

            if (apiOperationDoc.getPaths().contains("/undefinedAuthWithAuthOnClass")) {
                assertEquals(ApiAuthType.BASIC_AUTH, apiOperationDoc.getAuth().getType());
                assertEquals("ROLE_USER", apiOperationDoc.getAuth().getRoles().get(0));
                assertEquals("ROLE_ADMIN", apiOperationDoc.getAuth().getRoles().get(1));
            }

        }
    }

    @SuppressWarnings("unused")
    @Api(name = "test-token-auth", description = "Test token auth")
    @ApiAuthToken(roles = {""}, testTokens = {"abc", "cde"})
    private class TestControllerWithAuthToken {

        @ApiOperation(path = "/inherit")
        public void inherit() {

        }

        @ApiOperation(path = "/override")
        @ApiAuthToken(roles = {""}, scheme = "Bearer", testTokens = {"xyz"})
        public void override() {

        }

    }

    @Test
    public void testApiAuthToken() {
        ApiDoc apiDoc = buildDoc(TestControllerWithAuthToken.class);
        assertEquals(ApiAuthType.TOKEN, apiDoc.getAuth().getType());
        assertNull(apiDoc.getAuth().getScheme());
        assertEquals("abc", apiDoc.getAuth().getTesttokens().iterator().next());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/inherit")) {
                assertEquals(ApiAuthType.TOKEN, apiOperationDoc.getAuth().getType());
                assertNull(apiOperationDoc.getAuth().getScheme());
                assertEquals("abc", apiOperationDoc.getAuth().getTesttokens().iterator().next());
            }
            if (apiOperationDoc.getPaths().contains("/override")) {
                assertEquals(ApiAuthType.TOKEN, apiOperationDoc.getAuth().getType());
                assertEquals("Bearer", apiOperationDoc.getAuth().getScheme());
                assertEquals("xyz", apiOperationDoc.getAuth().getTesttokens().iterator().next());
            }
        }

    }

    @SuppressWarnings("unused")
    @Api(name = "test-controller-with-no-auth-annotation", description = "a-test-controller with no auth annotation")
    private class TestControllerWithNoAuthAnnotation {

        @ApiOperation(path = "/basicAuth", description = "A method with basic auth", verbs = ApiVerb.GET)
        @ApiAuthBasic(roles = {"ROLE_USER"},
                testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
        public String basicAuth() {
            return null;
        }

        @ApiOperation(path = "/noAuth", description = "A method with no auth", verbs = ApiVerb.GET)
        @ApiAuthNone
        public String noAuth() {
            return null;
        }

        @ApiOperation(path = "/undefinedAuthWithoutAuthOnClass",
                description = "A method with undefined auth and without auth info on class declaration",
                verbs = ApiVerb.GET)
        public String undefinedAuthWithoutAuthOnClass() {
            return null;
        }

    }

    @Test
    public void testControllerWithNoAuthAnnotation() {
        ApiDoc apiDoc = buildDoc(TestControllerWithNoAuthAnnotation.class);
        assertEquals("test-controller-with-no-auth-annotation", apiDoc.getName());
        assertNull(apiDoc.getAuth());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/basicAuth")) {
                assertEquals(ApiAuthType.BASIC_AUTH, apiOperationDoc.getAuth().getType());
                assertEquals("ROLE_USER", apiOperationDoc.getAuth().getRoles().get(0));
                assertTrue(apiOperationDoc.getAuth().getTestusers().size() > 0);
            }

            if (apiOperationDoc.getPaths().contains("/noAuth")) {
                assertEquals(ApiAuthType.NONE, apiOperationDoc.getAuth().getType());
                assertEquals(ApiAuthDocReader.ANONYMOUS, apiOperationDoc.getAuth().getRoles().get(0));
            }

            if (apiOperationDoc.getPaths().contains("/undefinedAuthWithoutAuthOnClass")) {
                assertNull(apiOperationDoc.getAuth());
            }

        }
    }

    @SuppressWarnings("unused")
    @Api(name = "test-old-style-servlets", description = "a-test-old-style-servlet")
    private class TestOldStyleServlets {

        @ApiOperation(path = "/oldStyle", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(pathParams = {@ApiPathParam(name = "name", type = String.class)})
        public String oldStyle() {
            return null;
        }

        @ApiOperation(path = "/oldStyleWithList", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(pathParams = {@ApiPathParam(name = "name", type = List.class)})
        public String oldStyleWithList() {
            return null;
        }

        @ApiOperation(path = "/oldStyleWithMap", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(pathParams = {@ApiPathParam(name = "name", type = Map.class)})
        public String oldStyleWithMap() {
            return null;
        }

        @ApiOperation(path = "/oldStyleMixed", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(//
                pathParams = {
                        @ApiPathParam(name = "name", type = String.class),
                        @ApiPathParam(name = "age", type = Integer.class),
                        @ApiPathParam(name = "undefined")
                }, //
                queryParams = {
                        @ApiQueryParam(name = "q", type = String.class, defaultValue = "qTest")
                })
        public String oldStyleMixed(@ApiPathParam(name = "age") Integer age) {
            return null;
        }

        @ApiOperation(path = "/oldStyleResponseObject",
                description = "A method with populated ApiResponseBodyType annotation",
                verbs = ApiVerb.GET)
        @ApiResponseBodyType(List.class)
        public void oldStyleResponseObject() {
        }

        @ApiOperation(path = "/oldStyleBodyObject",
                description = "A method with populated ApiRequestBodyType annotation",
                verbs = ApiVerb.GET)
        @ApiRequestBodyType(List.class)
        public void oldStyleBodyObject() {
        }

    }

    @Test
    public void testOldStyleServlets() {
        ApiDoc apiDoc = buildDoc(TestOldStyleServlets.class);
        assertEquals("test-old-style-servlets", apiDoc.getName());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/oldStyle")) {
                assertEquals(1, apiOperationDoc.getPathParameters().size());
            }

            if (apiOperationDoc.getPaths().contains("/oldStyleWithList")) {
                assertEquals(1, apiOperationDoc.getPathParameters().size());
            }

            if (apiOperationDoc.getPaths().contains("/oldStyleWithMap")) {
                assertEquals(1, apiOperationDoc.getPathParameters().size());
            }

            if (apiOperationDoc.getPaths().contains("/oldStyleMixed")) {
                assertEquals(3, apiOperationDoc.getPathParameters().size());
                assertEquals(1, apiOperationDoc.getQueryParameters().size());
                assertEquals("qTest", apiOperationDoc.getQueryParameters().iterator().next().getDefaultValue());
            }

            if (apiOperationDoc.getPaths().contains("/oldStyleResponseObject")) {
                assertEquals("List", apiOperationDoc.getResponseBodyType().getOneLineText());
            }

            if (apiOperationDoc.getPaths().contains("/oldStyleBodyObject")) {
                assertEquals("List", apiOperationDoc.getRequestBody().getType().getOneLineText());
            }
        }
    }

    @SuppressWarnings("unused")
    @Api(name = "test-errors-warnings-hints", description = "a-test-for-incomplete-documentation")
    private class TestErrorsAndWarningsAndHints {

        @ApiOperation
        public String oldStyle() {
            return null;
        }
    }

    @Test
    public void testErrorsAndWarningsAndHints() {
        ApiDoc apiDoc = buildDoc(TestErrorsAndWarningsAndHints.class);
        assertEquals("test-errors-warnings-hints", apiDoc.getName());
        ApiOperationDoc apiOperationDoc = apiDoc.getOperations().iterator().next();
        assertEquals(1, apiOperationDoc.getJsondocErrors().size());
        assertEquals(1, apiOperationDoc.getJsondocWarnings().size());
        assertEquals(2, apiOperationDoc.getJsondocHints().size());
    }

    @SuppressWarnings("unused")
    @Api(name = "test-errors-warnings-hints-method-display-as-summary",
            description = "a-test-for-incomplete-documentation-for-method-display-summary")
    private class TestErrorsAndWarningsAndHintsMethodSummary {

        @ApiOperation
        public String summary() {
            return null;
        }
    }

    @Test
    public void testErrorsAndWarningsAnsHintsSummary() {
        ApiDoc apiDoc = buildDoc(TestErrorsAndWarningsAndHintsMethodSummary.class);
        ApiOperationDoc apiOperationDoc = apiDoc.getOperations().iterator().next();
        assertEquals(1, apiOperationDoc.getJsondocErrors().size());
        assertEquals(1, apiOperationDoc.getJsondocWarnings().size());
        assertEquals(2, apiOperationDoc.getJsondocHints().size());
    }

    @SuppressWarnings("unused")
    @Api(description = "An interface controller", name = "interface-controller")
    private interface InterfaceController {

        @ApiOperation(path = "/interface", verbs = ApiVerb.GET)
        String inter();
    }

    @SuppressWarnings("unused")
    private class InterfaceControllerImpl implements InterfaceController {

        @Override
        public String inter() {
            return null;
        }

    }

    @Test
    public void testInterfaceController() {
        ApiDoc apiDoc;
        ApiOperationDoc apiOperationDoc;
        apiDoc = buildDoc(InterfaceController.class);
        assertEquals("interface-controller", apiDoc.getName());
        apiOperationDoc = apiDoc.getOperations().iterator().next();
        Assert.assertNotNull(apiOperationDoc);
        assertEquals("/interface", apiOperationDoc.getPaths().iterator().next());
    }

    @SuppressWarnings("unused")
    @Api(name = "test-declared-methods", description = "a-test-for-declared-methods")
    private class TestDeclaredMethods {

        @ApiOperation(path = "/protectedMethod")
        protected String protectedMethod() {
            return null;
        }

        @ApiOperation(path = "/privateMethod")
        private String privateMethod() {
            return null;
        }
    }

    @Test
    public void testDeclaredMethods() {
        ApiDoc apiDoc;
        apiDoc = buildDoc(TestDeclaredMethods.class);
        assertEquals("test-declared-methods", apiDoc.getName());
        assertEquals(2, apiDoc.getOperations().size());
    }

    @SuppressWarnings("unused")
    @Api(name = "ISSUE-110", description = "ISSUE-110")
    private class TestMultipleParamsWithSameMethod {

        @ApiOperation(path = "/search", description = "search one by title")
        @ApiResponseBodyType
        public List findByTitle(@ApiQueryParam(name = "title") String title) {
            return null;
        }

        @ApiOperation(path = "/search", description = "search one by content")
        @ApiResponseBodyType
        public List findByContent(@ApiQueryParam(name = "content") String content) {
            return null;
        }

        @ApiOperation(path = "/search", description = "search one by content and field")
        @ApiResponseBodyType
        public List findByContent(@ApiQueryParam(name = "content") String content,
                @ApiQueryParam(name = "field") String field) {
            return null;
        }
    }

    @Test
    public void testMultipleParamsSameMethod() {
        ApiDoc apiDoc = buildDoc(TestMultipleParamsWithSameMethod.class);
        assertEquals(3, apiDoc.getOperations().size());
    }

    @SuppressWarnings("unused")
    @Api(description = "ApiHeadersController", name = "ApiHeadersController")
    @ApiHeaders(headers = {
            @ApiHeader(name = "H1", description = "h1-description"),
            @ApiHeader(name = "H2", description = "h2-description")
    })
    private class ApiHeadersController {

        @ApiOperation(path = "/api-headers-controller-method-one")
        public void apiHeadersMethodOne() {
        }

        @ApiOperation(path = "/api-headers-controller-method-two")
        @ApiHeaders(headers = {
                @ApiHeader(name = "H4", description = "h4-description"),
                // duplicate H1 should be ignored (already on class level)
                @ApiHeader(name = "H1", description = "h1-description")
        })
        public void apiHeadersMethodTwo() {
        }
    }

    @Test
    public void testApiHeadersOnClass() {
        ApiDoc apiDoc = buildDoc(ApiHeadersController.class);
        assertEquals("ApiHeadersController", apiDoc.getName());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/api-headers-controller-method-one")) {
                assertEquals(2, apiOperationDoc.getHeaders().size());
            }
            if (apiOperationDoc.getPaths().contains("/api-headers-controller-method-two")) {
                assertEquals(3, apiOperationDoc.getHeaders().size());
            }
        }
    }

    @SuppressWarnings("unused")
    @Api(name = "test-path", description = "test-path")
    private class ControllerWithMethodPaths {

        @ApiOperation(path = {"/path1", "/path2"})
        public void path() {
        }
    }

    @Test
    public void testPaths() {
        ApiDoc apiDoc = buildDoc(ControllerWithMethodPaths.class);

        assertEquals(1, apiDoc.getOperations().size());
        assertTrue(apiDoc.getOperations().get(0).getPaths().containsAll(Arrays.asList("/path1", "/path2")));
    }

    @SuppressWarnings("unused")
    @Api(name = "test-type-level-stage", description = "Test type level stage attributes")
    @ApiStage(Stage.BETA)
    private class ControllerWithStage {

        @ApiOperation(path = "/inherit")
        public void inherit() {
        }

        @ApiOperation(path = "/override")
        @ApiStage(Stage.GA)
        public void override() {
        }
    }

    @Test
    public void testApiStage_typeLevel() {
        ApiDoc apiDoc = buildDoc(ControllerWithStage.class);
        assertEquals(Stage.BETA, apiDoc.getStage());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/inherit")) {
                assertEquals(Stage.BETA, apiOperationDoc.getStage());
            }
            if (apiOperationDoc.getPaths().contains("/override")) {
                assertEquals(Stage.GA, apiOperationDoc.getStage());
            }
        }
    }

    @SuppressWarnings("unused")
    @Api(name = "test-method-level-stage",
            description = "Test method level stage attributes")
    private class ControllerWithMethodStage {

        @ApiOperation(path = "/only-method")
        @ApiStage(Stage.DEPRECATED)
        public void testStage() {
        }
    }

    @Test
    public void testApiStage_methodLevel() {
        ApiDoc apiDoc = buildDoc(ControllerWithMethodStage.class);
        assertNull(apiDoc.getStage());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/only-method")) {
                assertEquals(Stage.DEPRECATED, apiOperationDoc.getStage());
            }
        }
    }

    private static ApiDoc buildDoc(Class<?> controller) {
        LivedocReader reader = LivedocReader.basicAnnotationReader(Collections.emptyList());
        Optional<ApiDoc> apiDoc = reader.readApiDoc(controller);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }
}
