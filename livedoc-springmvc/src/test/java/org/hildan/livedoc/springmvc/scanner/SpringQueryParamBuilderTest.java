package org.hildan.livedoc.springmvc.scanner;

import java.util.Iterator;

import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/param-one")) {
                Assert.assertEquals(1, apiOperationDoc.getQueryParameters().size());
            }
            if (apiOperationDoc.getPaths().contains("/param-two")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
            }
            if (apiOperationDoc.getPaths().contains("/param-three")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
                Iterator<ApiParamDoc> iterator = apiOperationDoc.getQueryParameters().iterator();
                ApiParamDoc param = iterator.next();
                Assert.assertEquals("param", param.getName());
                Assert.assertEquals("value", param.getAllowedValues()[0]);
                ApiParamDoc param2 = iterator.next();
                Assert.assertEquals("param2", param2.getName());
                Assert.assertEquals("value2", param2.getAllowedValues()[0]);
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
        Assert.assertEquals("SpringController2", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/param-one")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
            }
            if (apiOperationDoc.getPaths().contains("/param-two")) {
                Assert.assertEquals(3, apiOperationDoc.getQueryParameters().size());
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
        Assert.assertEquals("SpringController3", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/param-one")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
                Iterator<ApiParamDoc> iterator = apiOperationDoc.getQueryParameters().iterator();
                iterator.next();
                ApiParamDoc queryParam = iterator.next();
                Assert.assertEquals("name", queryParam.getName());
                Assert.assertEquals("true", queryParam.getRequired());
                Assert.assertEquals("String", queryParam.getType().getOneLineText());
                Assert.assertEquals("", queryParam.getDefaultValue());
            }
            if (apiOperationDoc.getPaths().contains("/param-two")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
                Iterator<ApiParamDoc> iterator = apiOperationDoc.getQueryParameters().iterator();
                iterator.next();
                ApiParamDoc queryParam = iterator.next();
                Assert.assertEquals("otherName", queryParam.getName());
                Assert.assertEquals("false", queryParam.getRequired());
                Assert.assertEquals("String", queryParam.getType().getOneLineText());
                Assert.assertEquals("test", queryParam.getDefaultValue());
            }
            if (apiOperationDoc.getPaths().contains("/param-three")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
                Iterator<ApiParamDoc> iterator = apiOperationDoc.getQueryParameters().iterator();
                iterator.next();
                ApiParamDoc queryParam = iterator.next();
                Assert.assertEquals("name", queryParam.getName());
                Assert.assertEquals("true", queryParam.getRequired());
                Assert.assertEquals("String", queryParam.getType().getOneLineText());
                Assert.assertEquals("", queryParam.getDefaultValue());
            }
        }
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
        Assert.assertEquals("SpringController4", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/")) {
                Assert.assertEquals(1, apiOperationDoc.getQueryParameters().size());
                ApiParamDoc param = apiOperationDoc.getQueryParameters().iterator().next();
                Assert.assertEquals("name", param.getName());
            }
            if (apiOperationDoc.getPaths().contains("/two")) {
                Assert.assertEquals(2, apiOperationDoc.getQueryParameters().size());
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
        Assert.assertEquals("SpringController5", apiDoc.getName());
        Assert.assertEquals(1, apiDoc.getOperations().size());
        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            if (apiOperationDoc.getPaths().contains("/")) {
                Assert.assertEquals(1, apiOperationDoc.getQueryParameters().size());
                ApiParamDoc param = apiOperationDoc.getQueryParameters().iterator().next();
                Assert.assertEquals("modelAttributePojo", param.getName());
                Assert.assertEquals("ModelAttributePojo", param.getType().getOneLineText());
            }
        }

    }


}
