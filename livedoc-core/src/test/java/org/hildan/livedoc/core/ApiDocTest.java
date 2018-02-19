package org.hildan.livedoc.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.ApiStage;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.annotations.ApiVisibility;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.hildan.livedoc.core.builders.doc.ApiAuthDocReader;
import org.hildan.livedoc.core.model.doc.ApiAuthType;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiErrorDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.model.doc.Stage;
import org.hildan.livedoc.core.model.doc.Visibility;
import org.hildan.livedoc.core.test.controller.Test3Controller;
import org.hildan.livedoc.core.test.pojo.Child;
import org.hildan.livedoc.core.test.pojo.Pizza;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ApiDocTest {

    @Api(name = "test-controller", description = "a-test-controller")
    @ApiVersion(since = "1.0", until = "2.12")
    @ApiAuthNone
    private class TestController {

        @ApiMethod(path = "/name", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public String name(@ApiPathParam(name = "name") String name, @ApiRequestBodyType String body) {
            return null;
        }

        @ApiMethod(path = "/age", verbs = ApiVerb.GET, description = "a-test-method", responseStatusCode = "204")
        @ApiResponseBodyType
        public Integer age(@ApiPathParam(name = "age") Integer age, @ApiRequestBodyType Integer body) {
            return null;
        }

        @ApiMethod(path = "/avg", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public Long avg(@ApiPathParam(name = "avg") Long avg, @ApiRequestBodyType Long body) {
            return null;
        }

        @ApiMethod(path = "/map", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public Map<String, Integer> map(@ApiPathParam(name = "map") Map<String, Integer> map,
                @ApiRequestBodyType Map<String, Integer> body) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        @ApiMethod(path = "/unparametrizedList", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public List unparametrizedList(@ApiPathParam(name = "unparametrizedList") List unparametrizedList,
                @ApiRequestBodyType List body) {
            return null;
        }

        @ApiMethod(path = "/parametrizedList", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public List<String> parametrizedList(@ApiPathParam(name = "parametrizedList") List<String> parametrizedList,
                @ApiRequestBodyType List<String> body) {
            return null;
        }

        @ApiMethod(path = "/wildcardParametrizedList", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public List<?> wildcardParametrizedList(
                @ApiPathParam(name = "wildcardParametrizedList") List<?> wildcardParametrizedList,
                @ApiRequestBodyType List<?> body) {
            return null;
        }

        @ApiMethod(path = "/LongArray", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public Long[] longObjArray(@ApiPathParam(name = "LongArray") Long[] longObjArray,
                @ApiRequestBodyType Long[] body) {
            return null;
        }

        @ApiMethod(path = "/longArray", verbs = ApiVerb.GET, description = "a-test-method")
        @ApiResponseBodyType
        public long[] longArray(@ApiPathParam(name = "longArray") long[] longArray, @ApiRequestBodyType long[] body) {
            return null;
        }

        @ApiMethod(path = "/version", verbs = ApiVerb.GET, description = "a-test-method for api version feature")
        @ApiVersion(since = "1.0", until = "2.12")
        @ApiResponseBodyType
        public String version(@ApiPathParam(name = "version") String version, @ApiRequestBodyType String body) {
            return null;
        }

        @ApiMethod(path = "/child", description = "A method returning a child", verbs = ApiVerb.GET)
        @ApiResponseBodyType
        public Child child(@ApiPathParam(name = "child") Child child, @ApiRequestBodyType Child body) {
            return null;
        }

        @ApiMethod(path = "/pizza", description = "A method returning a pizza", verbs = ApiVerb.GET)
        @ApiResponseBodyType
        public Pizza pizza(@ApiPathParam(name = "pizza") Pizza pizza, @ApiRequestBodyType Pizza body) {
            return null;
        }

        @ApiMethod(path = "/multiple-request-methods",
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
        ApiDoc apiDoc = buildDoc(Test3Controller.class, MethodDisplay.URI);

        List<ApiMethodDoc> methods = apiDoc.getMethods();
        ApiMethodDoc apiMethodDoc = methods.get(0);
        List<ApiErrorDoc> apiErrors = apiMethodDoc.getApiErrors();

        assertEquals(1, methods.size());
        assertEquals(3, apiErrors.size());
        assertEquals("1000", apiErrors.get(0).getCode());
        assertEquals("method-level annotation should be applied", "A test error #1",
                apiErrors.get(0).getDescription());
        assertEquals("2000", apiErrors.get(1).getCode());
        assertEquals("400", apiErrors.get(2).getCode());

    }

    @Test
    public void testGeneral() {
        ApiDoc apiDoc = buildDoc(TestController.class, MethodDisplay.URI);
        assertEquals("test-controller", apiDoc.getName());
        assertEquals("a-test-controller", apiDoc.getDescription());
        assertEquals("1.0", apiDoc.getSupportedVersions().getSince());
        assertEquals("2.12", apiDoc.getSupportedVersions().getUntil());
        assertEquals(ApiAuthType.NONE, apiDoc.getAuth().getType());
        assertEquals(ApiAuthDocReader.ANONYMOUS, apiDoc.getAuth().getRoles().get(0));

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {

            if (apiMethodDoc.getPaths().contains("/name")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("String", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("String", apiMethodDoc.getRequestBody().getType().getOneLineText());
                assertEquals("200 - OK", apiMethodDoc.getResponseStatusCode());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("name")) {
                        assertEquals("String", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/age")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("204", apiMethodDoc.getResponseStatusCode());
                assertEquals("Integer", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("Integer", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("age")) {
                        assertEquals("Integer", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/avg")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("Long", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("Long", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("avg")) {
                        assertEquals("Long", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/map")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("Map<String, Integer>", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("Map<String, Integer>", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("Map")) {
                        assertEquals("Map<String, Integer>", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/parametrizedList")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("List<String>", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("List<String>", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("parametrizedList")) {
                        assertEquals("List<String>", apiParamDoc.getType().getOneLineText());
                    }
                }

            }

            if (apiMethodDoc.getPaths().contains("/wildcardParametrizedList")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("List<?>", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("List<?>", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("wildcardParametrizedList")) {
                        assertEquals("List<?>", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/LongArray")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("Long[]", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("Long[]", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("LongArray")) {
                        assertEquals("Long[]", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/longArray")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("long[]", apiMethodDoc.getResponseBodyType().getOneLineText());
                assertEquals("long[]", apiMethodDoc.getRequestBody().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathParameters()) {
                    if (apiParamDoc.getName().equals("longArray")) {
                        assertEquals("long[]", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPaths().contains("/version")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("1.0", apiMethodDoc.getSupportedVersions().getSince());
                assertEquals("2.12", apiMethodDoc.getSupportedVersions().getUntil());
            }

            if (apiMethodDoc.getPaths().contains("/child")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("child", apiMethodDoc.getResponseBodyType().getOneLineText());
            }

            if (apiMethodDoc.getPaths().contains("/pizza")) {
                assertEquals(ApiVerb.GET, apiMethodDoc.getVerbs().iterator().next());
                assertEquals("customPizzaObject", apiMethodDoc.getResponseBodyType().getOneLineText());
            }

            if (apiMethodDoc.getPaths().contains("/multiple-request-methods")) {
                assertEquals(2, apiMethodDoc.getVerbs().size());
                Iterator<ApiVerb> iterator = apiMethodDoc.getVerbs().iterator();
                assertEquals(ApiVerb.GET, iterator.next());
                assertEquals(ApiVerb.POST, iterator.next());
            }

        }
    }

    @Api(name = "test-controller-with-basic-auth", description = "a-test-controller with basic auth annotation")
    @ApiAuthBasic(roles = {"ROLE_USER", "ROLE_ADMIN"},
            testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
    private class TestControllerWithBasicAuth {

        @ApiMethod(path = "/basicAuth", description = "A method with basic auth", verbs = ApiVerb.GET)
        @ApiAuthBasic(roles = {"ROLE_USER"},
                testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
        public String basicAuth() {
            return null;
        }

        @ApiMethod(path = "/noAuth", description = "A method with no auth", verbs = ApiVerb.GET)
        @ApiAuthNone
        public String noAuth() {
            return null;
        }

        @ApiMethod(path = "/undefinedAuthWithAuthOnClass",
                description = "A method with undefined auth but with auth info on class declaration",
                verbs = ApiVerb.GET)
        public String undefinedAuthWithAuthOnClass() {
            return null;
        }

    }

    @Test
    public void testControllerWithBasicAuth() {
        ApiDoc apiDoc = buildDoc(TestControllerWithBasicAuth.class, MethodDisplay.URI);
        assertEquals("test-controller-with-basic-auth", apiDoc.getName());
        assertEquals(ApiAuthType.BASIC_AUTH, apiDoc.getAuth().getType());
        assertEquals("ROLE_USER", apiDoc.getAuth().getRoles().get(0));
        assertEquals("ROLE_ADMIN", apiDoc.getAuth().getRoles().get(1));
        assertTrue(apiDoc.getAuth().getTestusers().size() > 0);

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/basicAuth")) {
                assertEquals(ApiAuthType.BASIC_AUTH, apiMethodDoc.getAuth().getType());
                assertEquals("ROLE_USER", apiMethodDoc.getAuth().getRoles().get(0));
                assertTrue(apiMethodDoc.getAuth().getTestusers().size() > 0);
            }

            if (apiMethodDoc.getPaths().contains("/noAuth")) {
                assertEquals(ApiAuthType.NONE, apiMethodDoc.getAuth().getType());
                assertEquals(ApiAuthDocReader.ANONYMOUS, apiMethodDoc.getAuth().getRoles().get(0));
            }

            if (apiMethodDoc.getPaths().contains("/undefinedAuthWithAuthOnClass")) {
                assertEquals(ApiAuthType.BASIC_AUTH, apiMethodDoc.getAuth().getType());
                assertEquals("ROLE_USER", apiMethodDoc.getAuth().getRoles().get(0));
                assertEquals("ROLE_ADMIN", apiMethodDoc.getAuth().getRoles().get(1));
            }

        }
    }

    @Api(name = "test-token-auth", description = "Test token auth")
    @ApiAuthToken(roles = {""}, testTokens = {"abc", "cde"})
    private class TestControllerWithAuthToken {

        @ApiMethod(path = "/inherit")
        public void inherit() {

        }

        @ApiMethod(path = "/override")
        @ApiAuthToken(roles = {""}, scheme = "Bearer", testTokens = {"xyz"})
        public void override() {

        }

    }

    @Test
    public void testApiAuthToken() {
        ApiDoc apiDoc = buildDoc(TestControllerWithAuthToken.class, MethodDisplay.URI);
        assertEquals(ApiAuthType.TOKEN, apiDoc.getAuth().getType());
        assertEquals("", apiDoc.getAuth().getScheme());
        assertEquals("abc", apiDoc.getAuth().getTesttokens().iterator().next());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/inherit")) {
                assertEquals(ApiAuthType.TOKEN, apiMethodDoc.getAuth().getType());
                assertEquals("", apiMethodDoc.getAuth().getScheme());
                assertEquals("abc", apiMethodDoc.getAuth().getTesttokens().iterator().next());
            }
            if (apiMethodDoc.getPaths().contains("/override")) {
                assertEquals(ApiAuthType.TOKEN, apiMethodDoc.getAuth().getType());
                assertEquals("Bearer", apiMethodDoc.getAuth().getScheme());
                assertEquals("xyz", apiMethodDoc.getAuth().getTesttokens().iterator().next());
            }
        }

    }

    @Api(name = "test-controller-with-no-auth-annotation", description = "a-test-controller with no auth annotation")
    private class TestControllerWithNoAuthAnnotation {

        @ApiMethod(path = "/basicAuth", description = "A method with basic auth", verbs = ApiVerb.GET)
        @ApiAuthBasic(roles = {"ROLE_USER"},
                testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
        public String basicAuth() {
            return null;
        }

        @ApiMethod(path = "/noAuth", description = "A method with no auth", verbs = ApiVerb.GET)
        @ApiAuthNone
        public String noAuth() {
            return null;
        }

        @ApiMethod(path = "/undefinedAuthWithoutAuthOnClass",
                description = "A method with undefined auth and without auth info on class declaration",
                verbs = ApiVerb.GET)
        public String undefinedAuthWithoutAuthOnClass() {
            return null;
        }

    }

    @Test
    public void testControllerWithNoAuthAnnotation() {
        ApiDoc apiDoc = buildDoc(TestControllerWithNoAuthAnnotation.class, MethodDisplay.URI);
        assertEquals("test-controller-with-no-auth-annotation", apiDoc.getName());
        assertNull(apiDoc.getAuth());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/basicAuth")) {
                assertEquals(ApiAuthType.BASIC_AUTH, apiMethodDoc.getAuth().getType());
                assertEquals("ROLE_USER", apiMethodDoc.getAuth().getRoles().get(0));
                assertTrue(apiMethodDoc.getAuth().getTestusers().size() > 0);
            }

            if (apiMethodDoc.getPaths().contains("/noAuth")) {
                assertEquals(ApiAuthType.NONE, apiMethodDoc.getAuth().getType());
                assertEquals(ApiAuthDocReader.ANONYMOUS, apiMethodDoc.getAuth().getRoles().get(0));
            }

            if (apiMethodDoc.getPaths().contains("/undefinedAuthWithoutAuthOnClass")) {
                assertNull(apiMethodDoc.getAuth());
            }

        }
    }

    @Api(name = "test-old-style-servlets", description = "a-test-old-style-servlet")
    private class TestOldStyleServlets {

        @ApiMethod(path = "/oldStyle", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(pathParams = {@ApiPathParam(name = "name", type = String.class)})
        public String oldStyle() {
            return null;
        }

        @ApiMethod(path = "/oldStyleWithList", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(pathParams = {@ApiPathParam(name = "name", type = List.class)})
        public String oldStyleWithList() {
            return null;
        }

        @ApiMethod(path = "/oldStyleWithMap", description = "A method params on method level", verbs = ApiVerb.GET)
        @ApiParams(pathParams = {@ApiPathParam(name = "name", type = Map.class)})
        public String oldStyleWithMap() {
            return null;
        }

        @ApiMethod(path = "/oldStyleMixed", description = "A method params on method level", verbs = ApiVerb.GET)
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

        @ApiMethod(path = "/oldStyleResponseObject",
                description = "A method with populated ApiResponseBodyType annotation",
                verbs = ApiVerb.GET)
        @ApiResponseBodyType(List.class)
        public void oldStyleResponseObject() {
        }

        @ApiMethod(path = "/oldStyleBodyObject",
                description = "A method with populated ApiRequestBodyType annotation",
                verbs = ApiVerb.GET)
        @ApiRequestBodyType(List.class)
        public void oldStyleBodyObject() {
        }

    }

    @Test
    public void testOldStyleServlets() {
        ApiDoc apiDoc = buildDoc(TestOldStyleServlets.class, MethodDisplay.URI);
        assertEquals("test-old-style-servlets", apiDoc.getName());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/oldStyle")) {
                assertEquals(1, apiMethodDoc.getPathParameters().size());
            }

            if (apiMethodDoc.getPaths().contains("/oldStyleWithList")) {
                assertEquals(1, apiMethodDoc.getPathParameters().size());
            }

            if (apiMethodDoc.getPaths().contains("/oldStyleWithMap")) {
                assertEquals(1, apiMethodDoc.getPathParameters().size());
            }

            if (apiMethodDoc.getPaths().contains("/oldStyleMixed")) {
                assertEquals(3, apiMethodDoc.getPathParameters().size());
                assertEquals(1, apiMethodDoc.getQueryParameters().size());
                assertEquals("qTest", apiMethodDoc.getQueryParameters().iterator().next().getDefaultValue());
            }

            if (apiMethodDoc.getPaths().contains("/oldStyleResponseObject")) {
                assertEquals("List", apiMethodDoc.getResponseBodyType().getOneLineText());
            }

            if (apiMethodDoc.getPaths().contains("/oldStyleBodyObject")) {
                assertEquals("List", apiMethodDoc.getRequestBody().getType().getOneLineText());
            }
        }
    }

    @Api(name = "test-errors-warnings-hints", description = "a-test-for-incomplete-documentation")
    private class TestErrorsAndWarningsAndHints {

        @ApiMethod
        public String oldStyle() {
            return null;
        }

    }

    @Test
    public void testErrorsAndWarningsAndHints() {
        ApiDoc apiDoc = buildDoc(TestErrorsAndWarningsAndHints.class, MethodDisplay.URI);
        assertEquals("test-errors-warnings-hints", apiDoc.getName());
        ApiMethodDoc apiMethodDoc = apiDoc.getMethods().iterator().next();
        assertEquals(1, apiMethodDoc.getJsondocErrors().size());
        assertEquals(1, apiMethodDoc.getJsondocWarnings().size());
        assertEquals(2, apiMethodDoc.getJsondocHints().size());
    }

    @Api(name = "test-errors-warnings-hints-method-display-as-summary",
            description = "a-test-for-incomplete-documentation-for-method-display-summary")
    private class TestErrorsAndWarningsAndHintsMethodSummary {

        @ApiMethod
        public String summary() {
            return null;
        }

    }

    @Test
    public void testErrorsAndWarningsAnsHintsSummary() {
        ApiDoc apiDoc = buildDoc(TestErrorsAndWarningsAndHintsMethodSummary.class, MethodDisplay.SUMMARY);
        ApiMethodDoc apiMethodDoc = apiDoc.getMethods().iterator().next();
        assertEquals(1, apiMethodDoc.getJsondocErrors().size());
        assertEquals(1, apiMethodDoc.getJsondocWarnings().size());
        assertEquals(3, apiMethodDoc.getJsondocHints().size());
    }

    @Api(description = "An interface controller", name = "interface-controller")
    private interface InterfaceController {

        @ApiMethod(path = "/interface", verbs = ApiVerb.GET)
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
        ApiMethodDoc apiMethodDoc;
        apiDoc = buildDoc(InterfaceController.class, MethodDisplay.URI);
        assertEquals("interface-controller", apiDoc.getName());
        apiMethodDoc = apiDoc.getMethods().iterator().next();
        Assert.assertNotNull(apiMethodDoc);
        assertEquals("/interface", apiMethodDoc.getPaths().iterator().next());
    }

    @Api(name = "test-declared-methods", description = "a-test-for-declared-methods")
    private class TestDeclaredMethods {

        @ApiMethod(path = "/protectedMethod")
        protected String protectedMethod() {
            return null;
        }

        @ApiMethod(path = "/privateMethod")
        private String privateMethod() {
            return null;
        }

    }

    @Test
    public void testDeclaredMethods() {
        ApiDoc apiDoc;
        apiDoc = buildDoc(TestDeclaredMethods.class, MethodDisplay.URI);
        assertEquals("test-declared-methods", apiDoc.getName());
        assertEquals(2, apiDoc.getMethods().size());
    }

    @Api(name = "ISSUE-110", description = "ISSUE-110")
    private class TestMultipleParamsWithSameMethod {

        @ApiMethod(path = "/search", description = "search one by title")
        @ApiResponseBodyType
        public List findByTitle(@ApiQueryParam(name = "title") String title) {
            return null;
        }

        @ApiMethod(path = "/search", description = "search one by content")
        @ApiResponseBodyType
        public List findByContent(@ApiQueryParam(name = "content") String content) {
            return null;
        }

        @ApiMethod(path = "/search", description = "search one by content and field")
        @ApiResponseBodyType
        public List findByContent(@ApiQueryParam(name = "content") String content,
                @ApiQueryParam(name = "field") String field) {
            return null;
        }

    }

    @Test
    public void testMultipleParamsSameMethod() {
        ApiDoc apiDoc = buildDoc(TestMultipleParamsWithSameMethod.class, MethodDisplay.URI);
        assertEquals(3, apiDoc.getMethods().size());
    }

    @Api(description = "ApiHeadersController", name = "ApiHeadersController")
    @ApiHeaders(headers = {
            @ApiHeader(name = "H1", description = "h1-description"),
            @ApiHeader(name = "H2", description = "h2-description")
    })
    private class ApiHeadersController {

        @ApiMethod(path = "/api-headers-controller-method-one")
        public void apiHeadersMethodOne() {

        }

        @ApiMethod(path = "/api-headers-controller-method-two")
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
        ApiDoc apiDoc = buildDoc(ApiHeadersController.class, MethodDisplay.URI);
        assertEquals("ApiHeadersController", apiDoc.getName());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/api-headers-controller-method-one")) {
                assertEquals(2, apiMethodDoc.getHeaders().size());
            }
            if (apiMethodDoc.getPaths().contains("/api-headers-controller-method-two")) {
                assertEquals(3, apiMethodDoc.getHeaders().size());
            }
        }
    }

    @Api(name = "test-path", description = "test-path")
    private class ControllerWithMethodPaths {

        @ApiMethod(path = {"/path1", "/path2"})
        public void path() {

        }

    }

    @Test
    public void testPath_methodDisplayURI() {
        ApiDoc apiDoc = buildDoc(ControllerWithMethodPaths.class, MethodDisplay.URI);

        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPaths().contains("/path1") && input.getPaths()
                                                                                                .contains("/path2")
                                         && input.getDisplayedMethodString().contains("/path1")
                                         && input.getDisplayedMethodString().contains("/path2"));

        assertTrue(allRight);
    }

    @Test
    public void testPath_methodDisplayMethod() {
        ApiDoc apiDoc = buildDoc(ControllerWithMethodPaths.class, MethodDisplay.METHOD);

        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPaths().contains("/path1") && input.getPaths()
                                                                                                .contains("/path2")
                                         && input.getDisplayedMethodString().contains("path")
                                         && !input.getDisplayedMethodString().contains("/path1"));

        assertTrue(allRight);
    }

    @Api(name = "test-type-level-visibility-and-stage",
            description = "Test type level visibility and stage attributes")
    @ApiVisibility(Visibility.PUBLIC)
    @ApiStage(Stage.BETA)
    private class ControllerWithTypeVisibility {

        @ApiMethod(path = "/inherit")
        public void inherit() {
        }

        @ApiMethod(path = "/override")
        @ApiVisibility(Visibility.PRIVATE)
        @ApiStage(Stage.GA)
        public void override() {
        }
    }

    @Test
    public void testApiVisibility_typeLevel() {
        ApiDoc apiDoc = buildDoc(ControllerWithTypeVisibility.class, MethodDisplay.URI);
        assertEquals(Visibility.PUBLIC, apiDoc.getVisibility());
        assertEquals(Stage.BETA, apiDoc.getStage());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/inherit")) {
                assertEquals(Visibility.PUBLIC, apiMethodDoc.getVisibility());
                assertEquals(Stage.BETA, apiMethodDoc.getStage());
            }
            if (apiMethodDoc.getPaths().contains("/override")) {
                assertEquals(Visibility.PRIVATE, apiMethodDoc.getVisibility());
                assertEquals(Stage.GA, apiMethodDoc.getStage());
            }
        }
    }

    @Api(name = "test-method-level-visibility-and-stage",
            description = "Test method level visibility and stage attributes")
    private class ControllerWithMethodVisibility {

        @ApiMethod(path = "/only-method")
        @ApiVisibility(Visibility.PRIVATE)
        @ApiStage(Stage.DEPRECATED)
        public void testVisibilityAndStage() {
        }
    }

    @Test
    public void testApiVisibility_methodLevel() {
        ApiDoc apiDoc = buildDoc(ControllerWithMethodVisibility.class, MethodDisplay.URI);
        assertNull(apiDoc.getVisibility());
        assertNull(apiDoc.getStage());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPaths().contains("/only-method")) {
                assertEquals(Visibility.PRIVATE, apiMethodDoc.getVisibility());
                assertEquals(Stage.DEPRECATED, apiMethodDoc.getStage());
            }
        }

    }

    private static ApiDoc buildDoc(Class<?> controller, MethodDisplay methodDisplay) {
        LivedocReader reader = LivedocReader.basicAnnotationReader(Collections.emptyList());
        Optional<ApiDoc> apiDoc = reader.readApiDoc(controller, methodDisplay);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }
}
