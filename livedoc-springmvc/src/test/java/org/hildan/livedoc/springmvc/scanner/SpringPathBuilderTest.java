package org.hildan.livedoc.springmvc.scanner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringPathBuilderTest {

    private LivedocReader builder;

    @Before
    public void setUp() {
        builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping(value = "/path")
        public void slashPath() {
        }

        @RequestMapping(value = "/")
        public void path() {
        }

        @RequestMapping()
        public void none() {
        }
    }

    @Test
    public void testPath() {
        ApiDoc apiDoc = builder.readApiDoc(SpringController.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController", apiDoc.getName());

        boolean slashPath = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/path"));
        Assert.assertTrue(slashPath);

        boolean slash = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/"));
        Assert.assertTrue(slash);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController2 {

        @RequestMapping
        public void none() {
        }

        @RequestMapping(value = "/test")
        public void test() {
        }
    }

    @Test
    public void testPath2() {
        ApiDoc apiDoc = builder.readApiDoc(SpringController2.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController2", apiDoc.getName());

        boolean none = apiDoc.getMethods().stream().anyMatch(input -> {
            System.out.println(input.getPath());
            return input.getPath().contains("/");
        });
        Assert.assertTrue(none);

        boolean test = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/test"));
        Assert.assertTrue(test);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(value = {"/path1", "/path2/"})
    public class SpringController3 {

        @RequestMapping(value = {"/path3", "path4"})
        public void none() {
        }
    }

    @Test
    public void testPath3() {
        ApiDoc apiDoc = builder.readApiDoc(SpringController3.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController3", apiDoc.getName());

        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPath()
                                                         .containsAll(Arrays.asList("/path1/path3", "/path1/path4",
                                                                 "/path2/path3", "/path2/path4")));
        Assert.assertTrue(allRight);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(value = "/path")
    public class SpringController4 {

        @RequestMapping
        public void none() {
        }
    }

    @Test
    public void testPath4() {
        ApiDoc apiDoc = builder.readApiDoc(SpringController4.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController4", apiDoc.getName());

        boolean allRight = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/path"));
        Assert.assertTrue(allRight);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(path = {"/path", "/path2"}, value = "/val1")
    public class SpringController5 {

        @RequestMapping
        public void none() {
        }
    }

    @Test
    public void testPath5() {
        ApiDoc apiDoc = builder.readApiDoc(SpringController5.class, MethodDisplay.URI, Collections.emptyMap());
        Assert.assertEquals("SpringController5", apiDoc.getName());

        List<String> expectedPaths = Arrays.asList("/path", "/path2", "/val1");
        boolean allRight = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().containsAll(expectedPaths));
        Assert.assertTrue(allRight);
    }

    @Test
    public void testPathWithMethodDisplayMethod() {
        ApiDoc apiDoc = builder.readApiDoc(SpringController5.class, MethodDisplay.METHOD, Collections.emptyMap());
        List<String> expectedPaths = Arrays.asList("/path", "/path2", "/val1");
        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPath().containsAll(expectedPaths)
                                         && input.getDisplayedMethodString().contains("none"));
        Assert.assertTrue(allRight);
    }
}
