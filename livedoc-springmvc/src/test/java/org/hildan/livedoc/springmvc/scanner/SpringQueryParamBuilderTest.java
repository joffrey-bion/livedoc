package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;
import java.util.List;

import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SpringQueryParamBuilderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping(value = "/param-one", params = "param")
        public void paramOne() {
        }

        @RequestMapping(value = "/param-two", params = {"param", "param2"})
        public void paramTwo() {
        }

        @RequestMapping(value = "/param-three", params = {"param=value", "param2=value2"})
        public void paramThree() {
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Test
    public void testQueryParam() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        assertEquals("SpringController", apiDoc.getName());
        assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/param-one")) {
                assertEquals(1, apiOperationDoc.getQueryParameters().size());
            }
            if (apiOperationDoc.getPaths().contains("/param-two")) {
                assertEquals(2, apiOperationDoc.getQueryParameters().size());
            }
            if (apiOperationDoc.getPaths().contains("/param-three")) {
                assertEquals(2, apiOperationDoc.getQueryParameters().size());
                Iterator<ApiParamDoc> iterator = apiOperationDoc.getQueryParameters().iterator();
                ApiParamDoc param = iterator.next();
                assertEquals("param", param.getName());
                assertEquals("value", param.getAllowedValues()[0]);
                ApiParamDoc param2 = iterator.next();
                assertEquals("param2", param2.getName());
                assertEquals("value2", param2.getAllowedValues()[0]);
            }
        }
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(params = {"param", "param2=value2"})
    public class SpringController2 {

        @RequestMapping("/param-one")
        public void paramOne() {
        }

        @RequestMapping(value = "/param-two", params = "param3")
        public void paramTwo() {
        }
    }

    @Test
    public void testQueryParam_mergeWithParent() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController2.class);
        assertEquals("SpringController2", apiDoc.getName());
        assertEquals(2, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/param-one")) {
                assertEquals(2, apiOperationDoc.getQueryParameters().size());
            }
            if (apiOperationDoc.getPaths().contains("/param-two")) {
                assertEquals(3, apiOperationDoc.getQueryParameters().size());
            }
        }
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(params = "param")
    public class SpringController3 {

        @RequestMapping("/param-one")
        public void paramOne(@RequestParam("name") String name) {
        }

        @RequestMapping("/param-two")
        public void paramTwo(@RequestParam(name = "otherName", defaultValue = "test", required = false) String name) {
        }

        @RequestMapping("/param-three")
        public void paramThree(@RequestParam String name) {
        }
    }

    @Test
    public void testQueryParam_springAnnotationOnParam() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController3.class);
        assertEquals("SpringController3", apiDoc.getName());
        assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            List<ApiParamDoc> queryParameters = apiOperationDoc.getQueryParameters();
            if (apiOperationDoc.getPaths().contains("/param-one")) {
                assertEquals(2, queryParameters.size());
                checkNameParam(queryParameters.get(0));
                checkParam(queryParameters.get(1));
            }
            if (apiOperationDoc.getPaths().contains("/param-two")) {
                assertEquals(2, queryParameters.size());
                checkOverriddenNameParam(queryParameters.get(0));
                checkParam(queryParameters.get(1));
            }
            if (apiOperationDoc.getPaths().contains("/param-three")) {
                assertEquals(2, queryParameters.size());
                checkNameParam(queryParameters.get(0));
                checkParam(queryParameters.get(1));
            }
        }
    }

    private static void checkParam(ApiParamDoc param) {
        assertEquals("param", param.getName());
        assertEquals("true", param.getRequired());
        assertEquals("String", param.getType().getOneLineText());
        assertNull(param.getDefaultValue());
    }

    private static void checkNameParam(ApiParamDoc queryParam) {
        assertEquals("name", queryParam.getName());
        assertEquals("true", queryParam.getRequired());
        assertEquals("String", queryParam.getType().getOneLineText());
        assertNull(queryParam.getDefaultValue());
    }

    private static void checkOverriddenNameParam(ApiParamDoc queryParam) {
        assertEquals("otherName", queryParam.getName());
        assertEquals("false", queryParam.getRequired());
        assertEquals("String", queryParam.getType().getOneLineText());
        assertEquals("test", queryParam.getDefaultValue());
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController4 {

        @RequestMapping("/")
        public void paramOne(@RequestParam @ApiQueryParam(name = "name") String name) {
        }

        @RequestMapping("/two")
        public void paramOne(@RequestParam @ApiQueryParam(name = "name") String name,
                @RequestParam @ApiQueryParam(name = "test") String test) {
        }
    }

    @Test
    public void testQueryParam_livedocAnnotationOnParam() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController4.class);
        assertEquals("SpringController4", apiDoc.getName());
        assertEquals(2, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/")) {
                assertEquals(1, apiOperationDoc.getQueryParameters().size());
                ApiParamDoc param = apiOperationDoc.getQueryParameters().iterator().next();
                assertEquals("name", param.getName());
            }
            if (apiOperationDoc.getPaths().contains("/two")) {
                assertEquals(2, apiOperationDoc.getQueryParameters().size());
            }
        }
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController5 {

        @RequestMapping("/")
        public void paramOne(@ModelAttribute("modelAttributePojo") ModelAttributePojo modelAttributePojo) {
        }

        public class ModelAttributePojo {}
    }

    @Test
    public void testQueryParam_complexType() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController5.class);
        assertEquals("SpringController5", apiDoc.getName());
        assertEquals(1, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/")) {
                assertEquals(1, apiOperationDoc.getQueryParameters().size());
                ApiParamDoc param = apiOperationDoc.getQueryParameters().iterator().next();
                assertEquals("modelAttributePojo", param.getName());
                assertEquals("ModelAttributePojo", param.getType().getOneLineText());
            }
        }
    }
}
