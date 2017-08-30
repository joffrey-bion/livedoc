package org.hildan.livedoc.springmvc.scanner;

import java.util.Collections;
import java.util.Iterator;

import org.hildan.livedoc.core.LivedocBuilder;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiParamDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocBuilderFactory;
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

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(params = {"param", "param2=value2"})
    public class SpringController2 {

        @RequestMapping(value = "/param-one")
        public void paramOne() {

        }

        @RequestMapping(value = "/param-two", params = "param3")
        public void paramTwo() {

        }

    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(params = "param")
    public class SpringController3 {

        @RequestMapping(value = "/param-one")
        public void paramOne(@RequestParam(value = "name") String name) {

        }

        @RequestMapping(value = "/param-two")
        public void paramTwo(@RequestParam(name = "otherName", defaultValue = "test", required = false) String name) {

        }

        @RequestMapping(value = "/param-three")
        public void paramThree(@RequestParam String name) {

        }

    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController4 {

        @RequestMapping(value = "/")
        public void paramOne(@RequestParam @ApiQueryParam(name = "name") String name) {

        }

        @RequestMapping(value = "/two")
        public void paramOne(@RequestParam @ApiQueryParam(name = "name") String name,
                @RequestParam @ApiQueryParam(name = "test") String test) {

        }

    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController5 {

        @RequestMapping(value = "/")
        public void paramOne(@ModelAttribute(value = "modelAttributePojo") ModelAttributePojo modelAttributePojo) {

        }

        @SuppressWarnings("WeakerAccess")
        public class ModelAttributePojo {}
    }

    @SuppressWarnings("unused")
    @Test
    public void testQueryParam() {
        LivedocBuilder builder = SpringLivedocBuilderFactory.springLivedocBuilder(Collections.emptyList());
        ApiDoc apiDoc = builder.readApiDoc(SpringController.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/param-one")) {
                Assert.assertEquals(1, apiMethodDoc.getQueryparameters().size());
            }
            if (apiMethodDoc.getPath().contains("/param-two")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
            }
            if (apiMethodDoc.getPath().contains("/param-three")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
                Iterator<ApiParamDoc> iterator = apiMethodDoc.getQueryparameters().iterator();
                ApiParamDoc param = iterator.next();
                Assert.assertEquals("param", param.getName());
                Assert.assertEquals("value", param.getAllowedvalues()[0]);
                ApiParamDoc param2 = iterator.next();
                Assert.assertEquals("param2", param2.getName());
                Assert.assertEquals("value2", param2.getAllowedvalues()[0]);
            }
        }

        apiDoc = builder.readApiDoc(SpringController2.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController2", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/param-one")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
            }
            if (apiMethodDoc.getPath().contains("/param-two")) {
                Assert.assertEquals(3, apiMethodDoc.getQueryparameters().size());
            }
        }

        apiDoc = builder.readApiDoc(SpringController3.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController3", apiDoc.getName());
        Assert.assertEquals(3, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/param-one")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
                Iterator<ApiParamDoc> iterator = apiMethodDoc.getQueryparameters().iterator();
                ApiParamDoc param = iterator.next();
                ApiParamDoc queryParam = iterator.next();
                Assert.assertEquals("name", queryParam.getName());
                Assert.assertEquals("true", queryParam.getRequired());
                Assert.assertEquals("String", queryParam.getLivedocType().getOneLineText());
                Assert.assertEquals("", queryParam.getDefaultvalue());
            }
            if (apiMethodDoc.getPath().contains("/param-two")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
                Iterator<ApiParamDoc> iterator = apiMethodDoc.getQueryparameters().iterator();
                ApiParamDoc param = iterator.next();
                ApiParamDoc queryParam = iterator.next();
                Assert.assertEquals("otherName", queryParam.getName());
                Assert.assertEquals("false", queryParam.getRequired());
                Assert.assertEquals("String", queryParam.getLivedocType().getOneLineText());
                Assert.assertEquals("test", queryParam.getDefaultvalue());
            }
            if (apiMethodDoc.getPath().contains("/param-three")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
                Iterator<ApiParamDoc> iterator = apiMethodDoc.getQueryparameters().iterator();
                ApiParamDoc param = iterator.next();
                ApiParamDoc queryParam = iterator.next();
                Assert.assertEquals("", queryParam.getName());
                Assert.assertEquals("true", queryParam.getRequired());
                Assert.assertEquals("String", queryParam.getLivedocType().getOneLineText());
                Assert.assertEquals("", queryParam.getDefaultvalue());
            }
        }

        apiDoc = builder.readApiDoc(SpringController4.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController4", apiDoc.getName());
        Assert.assertEquals(2, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/")) {
                Assert.assertEquals(1, apiMethodDoc.getQueryparameters().size());
                ApiParamDoc param = apiMethodDoc.getQueryparameters().iterator().next();
                Assert.assertEquals("name", param.getName());
            }
            if (apiMethodDoc.getPath().contains("/two")) {
                Assert.assertEquals(2, apiMethodDoc.getQueryparameters().size());
            }
        }

        apiDoc = builder.readApiDoc(SpringController5.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController5", apiDoc.getName());
        Assert.assertEquals(1, apiDoc.getMethods().size());
        for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
            if (apiMethodDoc.getPath().contains("/")) {
                Assert.assertEquals(1, apiMethodDoc.getQueryparameters().size());
                ApiParamDoc param = apiMethodDoc.getQueryparameters().iterator().next();
                Assert.assertEquals("modelAttributePojo", param.getName());
                Assert.assertEquals("ModelAttributePojo", param.getLivedocType().getOneLineText());
            }
        }

    }

}
