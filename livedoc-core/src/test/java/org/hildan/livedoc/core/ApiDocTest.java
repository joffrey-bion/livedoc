package org.hildan.livedoc.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.ApiAuthNone;
import org.hildan.livedoc.core.annotations.ApiAuthToken;
import org.hildan.livedoc.core.annotations.ApiBodyObject;
import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.annotations.ApiResponseObject;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.builders.doc.ApiAuthDocReader;
import org.hildan.livedoc.core.pojo.ApiAuthType;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiErrorDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiParamDoc;
import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.ApiVisibility;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.test.controller.Test3Controller;
import org.hildan.livedoc.core.test.pojo.Child;
import org.hildan.livedoc.core.test.pojo.Pizza;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ApiDocTest {

    @Api(name = "test-controller", description = "a-test-controller")
    @ApiVersion(since = "1.0", until = "2.12")
    @ApiAuthNone
    private class TestController {

        @ApiMethod(path = "/name", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        String name(@ApiPathParam(name = "name") String name, @ApiBodyObject String body) {
            return null;
        }

        @ApiMethod(path = "/age", verb = ApiVerb.GET, description = "a-test-method", responsestatuscode = "204")
        public @ApiResponseObject
        Integer age(@ApiPathParam(name = "age") Integer age, @ApiBodyObject Integer body) {
            return null;
        }

        @ApiMethod(path = "/avg", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        Long avg(@ApiPathParam(name = "avg") Long avg, @ApiBodyObject Long body) {
            return null;
        }

        @ApiMethod(path = "/map", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        Map<String, Integer> map(@ApiPathParam(name = "map") Map<String, Integer> map,
                @ApiBodyObject Map<String, Integer> body) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        @ApiMethod(path = "/unparametrizedList", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        List unparametrizedList(@ApiPathParam(name = "unparametrizedList") List unparametrizedList,
                @ApiBodyObject List body) {
            return null;
        }

        @ApiMethod(path = "/parametrizedList", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        List<String> parametrizedList(@ApiPathParam(name = "parametrizedList") List<String> parametrizedList,
                @ApiBodyObject List<String> body) {
            return null;
        }

        @ApiMethod(path = "/wildcardParametrizedList", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        List<?> wildcardParametrizedList(
                @ApiPathParam(name = "wildcardParametrizedList") List<?> wildcardParametrizedList,
                @ApiBodyObject List<?> body) {
            return null;
        }

        @ApiMethod(path = "/LongArray", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        Long[] LongArray(@ApiPathParam(name = "LongArray") Long[] LongArray, @ApiBodyObject Long[] body) {
            return null;
        }

        @ApiMethod(path = "/longArray", verb = ApiVerb.GET, description = "a-test-method")
        public @ApiResponseObject
        long[] longArray(@ApiPathParam(name = "longArray") long[] LongArray, @ApiBodyObject long[] body) {
            return null;
        }

        @ApiMethod(path = "/version", verb = ApiVerb.GET, description = "a-test-method for api version feature")
        @ApiVersion(since = "1.0", until = "2.12")
        public @ApiResponseObject
        String version(@ApiPathParam(name = "version") String version, @ApiBodyObject String body) {
            return null;
        }

        @ApiMethod(path = "/child", description = "A method returning a child", verb = ApiVerb.GET)
        public @ApiResponseObject
        Child child(@ApiPathParam(name = "child") Child child, @ApiBodyObject Child body) {
            return null;
        }

        @ApiMethod(path = "/pizza", description = "A method returning a pizza", verb = ApiVerb.GET)
        public @ApiResponseObject
        Pizza pizza(@ApiPathParam(name = "pizza") Pizza pizza, @ApiBodyObject Pizza body) {
            return null;
        }

        @ApiMethod(path = "/multiple-request-methods", verb = {ApiVerb.GET, ApiVerb.POST},
                description = "a-test-method-with-multiple-request-methods")
        public @ApiResponseObject
        Integer multipleRequestMethods(@ApiPathParam(name = "multiple-request-methods") Integer multipleRequestMethods,
                @ApiBodyObject Integer body) {
            return null;
        }

    }

    @Test
    public void testApiErrorsDoc() {
        ApiDoc apiDoc = buildDoc(Test3Controller.class, MethodDisplay.URI);

        final List<ApiMethodDoc> methods = apiDoc.getMethods();
        final ApiMethodDoc apiMethodDoc = methods.get(0);
        final List<ApiErrorDoc> apiErrors = apiMethodDoc.getApierrors();

        Assert.assertEquals(1, methods.size());
        Assert.assertEquals(3, apiErrors.size());
        Assert.assertEquals("1000", apiErrors.get(0).getCode());
        Assert.assertEquals("method-level annotation should be applied", "A test error #1",
                apiErrors.get(0).getDescription());
        Assert.assertEquals("2000", apiErrors.get(1).getCode());
        Assert.assertEquals("400", apiErrors.get(2).getCode());

    }

    @Test
    public void testGeneral() {
        ApiDoc apiDoc = buildDoc(TestController.class, MethodDisplay.URI);
        Assert.assertEquals("test-controller", apiDoc.getName());
        Assert.assertEquals("a-test-controller", apiDoc.getDescription());
        Assert.assertEquals("1.0", apiDoc.getSupportedversions().getSince());
        Assert.assertEquals("2.12", apiDoc.getSupportedversions().getUntil());
        Assert.assertEquals(ApiAuthType.NONE.name(), apiDoc.getAuth().getType());
        Assert.assertEquals(ApiAuthDocReader.ANONYMOUS, apiDoc.getAuth().getRoles().get(0));

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {

            if (apiMethodDoc.getPath().contains("/name")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("String", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("String", apiMethodDoc.getBodyobject().getType().getOneLineText());
                Assert.assertEquals("200 - OK", apiMethodDoc.getResponsestatuscode());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("name")) {
                        Assert.assertEquals("String", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/age")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("204", apiMethodDoc.getResponsestatuscode());
                Assert.assertEquals("Integer", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("Integer", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("age")) {
                        Assert.assertEquals("Integer", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/avg")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("Long", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("Long", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("avg")) {
                        Assert.assertEquals("Long", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/map")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("Map<String, Integer>",
                        apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("Map<String, Integer>", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("Map")) {
                        Assert.assertEquals("Map<String, Integer>", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/parametrizedList")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("List<String>", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("List<String>", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("parametrizedList")) {
                        Assert.assertEquals("List<String>", apiParamDoc.getType().getOneLineText());
                    }
                }

            }

            if (apiMethodDoc.getPath().contains("/wildcardParametrizedList")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("List<?>", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("List<?>", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("wildcardParametrizedList")) {
                        Assert.assertEquals("List<?>", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/LongArray")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("Long[]", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("Long[]", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("LongArray")) {
                        Assert.assertEquals("Long[]", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/longArray")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("long[]", apiMethodDoc.getResponse().getType().getOneLineText());
                Assert.assertEquals("long[]", apiMethodDoc.getBodyobject().getType().getOneLineText());
                for (ApiParamDoc apiParamDoc : apiMethodDoc.getPathparameters()) {
                    if (apiParamDoc.getName().equals("longArray")) {
                        Assert.assertEquals("long[]", apiParamDoc.getType().getOneLineText());
                    }
                }
            }

            if (apiMethodDoc.getPath().contains("/version")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("1.0", apiMethodDoc.getSupportedversions().getSince());
                Assert.assertEquals("2.12", apiMethodDoc.getSupportedversions().getUntil());
            }

            if (apiMethodDoc.getPath().contains("/child")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("child", apiMethodDoc.getResponse().getType().getOneLineText());
            }

            if (apiMethodDoc.getPath().contains("/pizza")) {
                Assert.assertEquals(ApiVerb.GET, apiMethodDoc.getVerb().iterator().next());
                Assert.assertEquals("customPizzaObject", apiMethodDoc.getResponse().getType().getOneLineText());
            }

            if (apiMethodDoc.getPath().contains("/multiple-request-methods")) {
                Assert.assertEquals(2, apiMethodDoc.getVerb().size());
                Iterator<ApiVerb> iterator = apiMethodDoc.getVerb().iterator();
                Assert.assertEquals(ApiVerb.GET, iterator.next());
                Assert.assertEquals(ApiVerb.POST, iterator.next());
            }

        }
    }

    @Api(name = "test-controller-with-basic-auth", description = "a-test-controller with basic auth annotation")
    @ApiAuthBasic(roles = {"ROLE_USER", "ROLE_ADMIN"},
            testusers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
    private class TestControllerWithBasicAuth {

        @ApiMethod(path = "/basicAuth", description = "A method with basic auth", verb = ApiVerb.GET)
        @ApiAuthBasic(roles = {"ROLE_USER"},
                testusers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
        public String basicAuth() {
            return null;
        }

        @ApiMethod(path = "/noAuth", description = "A method with no auth", verb = ApiVerb.GET)
        @ApiAuthNone
        public String noAuth() {
            return null;
        }

        @ApiMethod(path = "/undefinedAuthWithAuthOnClass",
                description = "A method with undefined auth but with auth info on class declaration",
                verb = ApiVerb.GET)
        public String undefinedAuthWithAuthOnClass() {
            return null;
        }

    }

    @Test
    public void testControllerWithBasicAuth() {
        ApiDoc apiDoc = buildDoc(TestControllerWithBasicAuth.class, MethodDisplay.URI);
        Assert.assertEquals("test-controller-with-basic-auth", apiDoc.getName());
        Assert.assertEquals(ApiAuthType.BASIC_AUTH.name(), apiDoc.getAuth().getType());
        Assert.assertEquals("ROLE_USER", apiDoc.getAuth().getRoles().get(0));
        Assert.assertEquals("ROLE_ADMIN", apiDoc.getAuth().getRoles().get(1));
        assertTrue(apiDoc.getAuth().getTestusers().size() > 0);

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/basicAuth")) {
                Assert.assertEquals(ApiAuthType.BASIC_AUTH.name(), apiMethodDoc.getAuth().getType());
                Assert.assertEquals("ROLE_USER", apiMethodDoc.getAuth().getRoles().get(0));
                assertTrue(apiMethodDoc.getAuth().getTestusers().size() > 0);
            }

            if (apiMethodDoc.getPath().contains("/noAuth")) {
                Assert.assertEquals(ApiAuthType.NONE.name(), apiMethodDoc.getAuth().getType());
                Assert.assertEquals(ApiAuthDocReader.ANONYMOUS, apiMethodDoc.getAuth().getRoles().get(0));
            }

            if (apiMethodDoc.getPath().contains("/undefinedAuthWithAuthOnClass")) {
                Assert.assertEquals(ApiAuthType.BASIC_AUTH.name(), apiMethodDoc.getAuth().getType());
                Assert.assertEquals("ROLE_USER", apiMethodDoc.getAuth().getRoles().get(0));
                Assert.assertEquals("ROLE_ADMIN", apiMethodDoc.getAuth().getRoles().get(1));
            }

        }
    }

    @Api(name = "test-token-auth", description = "Test token auth")
    @ApiAuthToken(roles = {""}, testtokens = {"abc", "cde"})
    private class TestControllerWithAuthToken {

        @ApiMethod(path = "/inherit")
        public void inherit() {

        }

        @ApiMethod(path = "/override")
        @ApiAuthToken(roles = {""}, scheme = "Bearer", testtokens = {"xyz"})
        public void override() {

        }

    }

    @Test
    public void testApiAuthToken() {
        ApiDoc apiDoc = buildDoc(TestControllerWithAuthToken.class, MethodDisplay.URI);
        Assert.assertEquals("TOKEN", apiDoc.getAuth().getType());
        Assert.assertEquals("", apiDoc.getAuth().getScheme());
        Assert.assertEquals("abc", apiDoc.getAuth().getTesttokens().iterator().next());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/inherit")) {
                Assert.assertEquals("TOKEN", apiMethodDoc.getAuth().getType());
                Assert.assertEquals("", apiMethodDoc.getAuth().getScheme());
                Assert.assertEquals("abc", apiMethodDoc.getAuth().getTesttokens().iterator().next());
            }
            if (apiMethodDoc.getPath().contains("/override")) {
                Assert.assertEquals("TOKEN", apiMethodDoc.getAuth().getType());
                Assert.assertEquals("Bearer", apiMethodDoc.getAuth().getScheme());
                Assert.assertEquals("xyz", apiMethodDoc.getAuth().getTesttokens().iterator().next());
            }
        }

    }

    @Api(name = "test-controller-with-no-auth-annotation", description = "a-test-controller with no auth annotation")
    private class TestControllerWithNoAuthAnnotation {

        @ApiMethod(path = "/basicAuth", description = "A method with basic auth", verb = ApiVerb.GET)
        @ApiAuthBasic(roles = {"ROLE_USER"},
                testusers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
        public String basicAuth() {
            return null;
        }

        @ApiMethod(path = "/noAuth", description = "A method with no auth", verb = ApiVerb.GET)
        @ApiAuthNone
        public String noAuth() {
            return null;
        }

        @ApiMethod(path = "/undefinedAuthWithoutAuthOnClass",
                description = "A method with undefined auth and without auth info on class declaration",
                verb = ApiVerb.GET)
        public String undefinedAuthWithoutAuthOnClass() {
            return null;
        }

    }

    @Test
    public void testControllerWithNoAuthAnnotation() {
        ApiDoc apiDoc = buildDoc(TestControllerWithNoAuthAnnotation.class, MethodDisplay.URI);
        Assert.assertEquals("test-controller-with-no-auth-annotation", apiDoc.getName());
        Assert.assertNull(apiDoc.getAuth());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/basicAuth")) {
                Assert.assertEquals(ApiAuthType.BASIC_AUTH.name(), apiMethodDoc.getAuth().getType());
                Assert.assertEquals("ROLE_USER", apiMethodDoc.getAuth().getRoles().get(0));
                assertTrue(apiMethodDoc.getAuth().getTestusers().size() > 0);
            }

            if (apiMethodDoc.getPath().contains("/noAuth")) {
                Assert.assertEquals(ApiAuthType.NONE.name(), apiMethodDoc.getAuth().getType());
                Assert.assertEquals(ApiAuthDocReader.ANONYMOUS, apiMethodDoc.getAuth().getRoles().get(0));
            }

            if (apiMethodDoc.getPath().contains("/undefinedAuthWithoutAuthOnClass")) {
                Assert.assertNull(apiMethodDoc.getAuth());
            }

        }
    }

    @Api(name = "test-old-style-servlets", description = "a-test-old-style-servlet")
    private class TestOldStyleServlets {

        @ApiMethod(path = "/oldStyle", description = "A method params on method level", verb = ApiVerb.GET)
        @ApiParams(pathparams = {@ApiPathParam(name = "name", clazz = String.class)})
        public String oldStyle() {
            return null;
        }

        @ApiMethod(path = "/oldStyleWithList", description = "A method params on method level", verb = ApiVerb.GET)
        @ApiParams(pathparams = {@ApiPathParam(name = "name", clazz = List.class)})
        public String oldStyleWithList() {
            return null;
        }

        @ApiMethod(path = "/oldStyleWithMap", description = "A method params on method level", verb = ApiVerb.GET)
        @ApiParams(pathparams = {@ApiPathParam(name = "name", clazz = Map.class)})
        public String oldStyleWithMap() {
            return null;
        }

        @ApiMethod(path = "/oldStyleMixed", description = "A method params on method level", verb = ApiVerb.GET)
        @ApiParams(pathparams = {@ApiPathParam(name = "name", clazz = String.class),
                @ApiPathParam(name = "age", clazz = Integer.class), @ApiPathParam(name = "undefined")},
                queryparams = {@ApiQueryParam(name = "q", clazz = String.class, defaultvalue = "qTest")})
        public String oldStyleMixed(@ApiPathParam(name = "age") Integer age) {
            return null;
        }

        @ApiMethod(path = "/oldStyleResponseObject",
                description = "A method with populated ApiResponseObject annotation", verb = ApiVerb.GET)
        @ApiResponseObject(clazz = List.class)
        public void oldStyleResponseObject() {
            return;
        }

        @ApiMethod(path = "/oldStyleBodyObject", description = "A method with populated ApiBodyObject annotation",
                verb = ApiVerb.GET)
        @ApiBodyObject(clazz = List.class)
        public void oldStyleBodyObject() {
            return;
        }

    }

    @Test
    public void testOldStyleServlets() {
        ApiDoc apiDoc = buildDoc(TestOldStyleServlets.class, MethodDisplay.URI);
        Assert.assertEquals("test-old-style-servlets", apiDoc.getName());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/oldStyle")) {
                Assert.assertEquals(1, apiMethodDoc.getPathparameters().size());
            }

            if (apiMethodDoc.getPath().contains("/oldStyleWithList")) {
                Assert.assertEquals(1, apiMethodDoc.getPathparameters().size());
            }

            if (apiMethodDoc.getPath().contains("/oldStyleWithMap")) {
                Assert.assertEquals(1, apiMethodDoc.getPathparameters().size());
            }

            if (apiMethodDoc.getPath().contains("/oldStyleMixed")) {
                Assert.assertEquals(3, apiMethodDoc.getPathparameters().size());
                Assert.assertEquals(1, apiMethodDoc.getQueryparameters().size());
                Assert.assertEquals("qTest", apiMethodDoc.getQueryparameters().iterator().next().getDefaultvalue());
            }

            if (apiMethodDoc.getPath().contains("/oldStyleResponseObject")) {
                Assert.assertEquals("List", apiMethodDoc.getResponse().getType().getOneLineText());
            }

            if (apiMethodDoc.getPath().contains("/oldStyleBodyObject")) {
                Assert.assertEquals("List", apiMethodDoc.getBodyobject().getType().getOneLineText());
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
        Assert.assertEquals("test-errors-warnings-hints", apiDoc.getName());
        ApiMethodDoc apiMethodDoc = apiDoc.getMethods().iterator().next();
        Assert.assertEquals(1, apiMethodDoc.getJsondocerrors().size());
        Assert.assertEquals(1, apiMethodDoc.getJsondocwarnings().size());
        Assert.assertEquals(2, apiMethodDoc.getJsondochints().size());
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
        Assert.assertEquals(1, apiMethodDoc.getJsondocerrors().size());
        Assert.assertEquals(1, apiMethodDoc.getJsondocwarnings().size());
        Assert.assertEquals(3, apiMethodDoc.getJsondochints().size());
    }

    @Api(description = "An interface controller", name = "interface-controller")
    private interface InterfaceController {

        @ApiMethod(path = "/interface", verb = ApiVerb.GET)
        public String inter();
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
        Assert.assertEquals("interface-controller", apiDoc.getName());
        apiMethodDoc = apiDoc.getMethods().iterator().next();
        Assert.assertNotNull(apiMethodDoc);
        Assert.assertEquals("/interface", apiMethodDoc.getPath().iterator().next());
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
        Assert.assertEquals("test-declared-methods", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
    }

    @Api(name = "ISSUE-110", description = "ISSUE-110")
    private class TestMultipleParamsWithSameMethod {

        @ApiMethod(path = "/search", description = "search one by title")
        public @ApiResponseObject
        List findByTitle(@ApiQueryParam(name = "title") String title) {
            return null;
        }

        @ApiMethod(path = "/search", description = "search one by content")
        public @ApiResponseObject
        List findByContent(@ApiQueryParam(name = "content") String content) {
            return null;
        }

        @ApiMethod(path = "/search", description = "search one by content and field")
        public @ApiResponseObject
        List findByContent(@ApiQueryParam(name = "content") String content,
                @ApiQueryParam(name = "field") String field) {
            return null;
        }

    }

    @Test
    public void testMultipleParamsSameMethod() {
        ApiDoc apiDoc = buildDoc(TestMultipleParamsWithSameMethod.class, MethodDisplay.URI);
        Assert.assertEquals(3, apiDoc.getMethods().size());
    }

    @Api(description = "ApiHeadersController", name = "ApiHeadersController")
    @ApiHeaders(headers = {@ApiHeader(name = "H1", description = "h1-description"),
            @ApiHeader(name = "H2", description = "h2-description")})
    private class ApiHeadersController {

        @ApiMethod(path = "/api-headers-controller-method-one")
        public void apiHeadersMethodOne() {

        }

        @ApiMethod(path = "/api-headers-controller-method-two")
        @ApiHeaders(headers = {@ApiHeader(name = "H4", description = "h4-description"),
                @ApiHeader(name = "H1", description = "h1-description")
                // this is a duplicate of the one at the class level, it will not be taken into account when building
                // the doc
        })
        public void apiHeadersMethodTwo() {

        }

    }

    @Test
    public void testApiHeadersOnClass() {
        ApiDoc apiDoc = buildDoc(ApiHeadersController.class, MethodDisplay.URI);
        Assert.assertEquals("ApiHeadersController", apiDoc.getName());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/api-headers-controller-method-one")) {
                Assert.assertEquals(2, apiMethodDoc.getHeaders().size());
            }
            if (apiMethodDoc.getPath().contains("/api-headers-controller-method-two")) {
                Assert.assertEquals(3, apiMethodDoc.getHeaders().size());
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
                                 .anyMatch(input -> input.getPath().contains("/path1") && input.getPath()
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
                                 .anyMatch(input -> input.getPath().contains("/path1") && input.getPath()
                                                                                               .contains("/path2")
                                         && input.getDisplayedMethodString().contains("path")
                                         && !input.getDisplayedMethodString().contains("/path1"));

        assertTrue(allRight);
    }

    @Api(name = "test-type-level-visibility-and-stage", description = "Test type level visibility and stage attributes",
            visibility = ApiVisibility.PUBLIC, stage = ApiStage.BETA)
    private class ControllerWithTypeVisibility {

        @ApiMethod(path = "/inherit")
        public void inherit() {
        }

        @ApiMethod(path = "/override", visibility = ApiVisibility.PRIVATE, stage = ApiStage.GA)
        public void override() {
        }
    }

    @Test
    public void testApiVisibility_typeLevel() {
        ApiDoc apiDoc = buildDoc(ControllerWithTypeVisibility.class, MethodDisplay.URI);
        Assert.assertEquals(ApiVisibility.PUBLIC, apiDoc.getVisibility());
        Assert.assertEquals(ApiStage.BETA, apiDoc.getStage());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/inherit")) {
                Assert.assertEquals(ApiVisibility.PUBLIC, apiMethodDoc.getVisibility());
                Assert.assertEquals(ApiStage.BETA, apiMethodDoc.getStage());
            }
            if (apiMethodDoc.getPath().contains("/override")) {
                Assert.assertEquals(ApiVisibility.PRIVATE, apiMethodDoc.getVisibility());
                Assert.assertEquals(ApiStage.GA, apiMethodDoc.getStage());
            }
        }
    }

    @Api(name = "test-method-level-visibility-and-stage",
            description = "Test method level visibility and stage attributes")
    private class ControllerWithMethodVisibility {

        @ApiMethod(path = "/only-method", visibility = ApiVisibility.PRIVATE, stage = ApiStage.DEPRECATED)
        public void testVisibilityAndStage() {
        }
    }

    @Test
    public void testApiVisibility_methodLevel() {
        ApiDoc apiDoc = buildDoc(ControllerWithMethodVisibility.class, MethodDisplay.URI);
        Assert.assertEquals(ApiVisibility.UNDEFINED, apiDoc.getVisibility());
        Assert.assertEquals(ApiStage.UNDEFINED, apiDoc.getStage());

        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/only-method")) {
                Assert.assertEquals(ApiVisibility.PRIVATE, apiMethodDoc.getVisibility());
                Assert.assertEquals(ApiStage.DEPRECATED, apiMethodDoc.getStage());
            }
        }

    }

    private static ApiDoc buildDoc(Class<?> controller, MethodDisplay methodDisplay) {
        LivedocReader builder = LivedocReader.basicAnnotationBuilder(Collections.emptyList());
        Optional<ApiDoc> apiDoc = builder.readApiDoc(controller, methodDisplay);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }
}
