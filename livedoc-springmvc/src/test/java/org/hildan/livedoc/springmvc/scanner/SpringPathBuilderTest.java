package org.hildan.livedoc.springmvc.scanner;

import java.util.Arrays;
import java.util.List;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

public class SpringPathBuilderTest {

    private DocAnnotationScanner scanner = new SpringDocAnnotationScanner();

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

    @Controller
    @RequestMapping(value = {"/path1", "/path2/"})
    public class SpringController3 {

        @RequestMapping(value = {"/path3", "path4"})
        public void none() {
        }
    }

    @Controller
    @RequestMapping(value = "/path")
    public class SpringController4 {

        @RequestMapping
        public void none() {
        }
    }

    @Controller
    @RequestMapping(path = {"/path", "/path2"}, value = "/val1")
    public class SpringController5 {

        @RequestMapping
        public void none() {
        }
    }

    @Test
    public void testPath() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.newHashSet(SpringController.class), MethodDisplay.URI)
                               .iterator()
                               .next();
        Assert.assertEquals("SpringController", apiDoc.getName());

        boolean slashPath = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/path"));
        Assert.assertTrue(slashPath);

        boolean slash = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/"));
        Assert.assertTrue(slash);
    }

    @Test
    public void testPath2() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.newHashSet(SpringController2.class), MethodDisplay.URI)
                               .iterator()
                               .next();
        Assert.assertEquals("SpringController2", apiDoc.getName());

        boolean none = apiDoc.getMethods().stream().anyMatch(input -> {
            System.out.println(input.getPath());
            return input.getPath().contains("/");
        });
        Assert.assertTrue(none);

        boolean test = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/test"));
        Assert.assertTrue(test);
    }

    @Test
    public void testPath3() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.newHashSet(SpringController3.class), MethodDisplay.URI)
                               .iterator()
                               .next();
        Assert.assertEquals("SpringController3", apiDoc.getName());

        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPath()
                                                         .containsAll(Arrays.asList("/path1/path3", "/path1/path4",
                                                                 "/path2/path3", "/path2/path4")));
        Assert.assertTrue(allRight);
    }

    @Test
    public void testPath4() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.newHashSet(SpringController4.class), MethodDisplay.URI)
                               .iterator()
                               .next();
        Assert.assertEquals("SpringController4", apiDoc.getName());

        boolean allRight = apiDoc.getMethods().stream().anyMatch(input -> input.getPath().contains("/path"));
        Assert.assertTrue(allRight);
    }

    @Test
    public void testPath5() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.newHashSet(SpringController5.class), MethodDisplay.URI)
                               .iterator()
                               .next();
        Assert.assertEquals("SpringController5", apiDoc.getName());

        List<String> expectedPaths = Arrays.asList("/path", "/path2", "/val1");
        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPath().containsAll(expectedPaths));
        Assert.assertTrue(allRight);
    }

    @Test
    public void testPathWithMethodDisplayMethod() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.newHashSet(SpringController5.class), MethodDisplay.METHOD)
                               .iterator()
                               .next();
        List<String> expectedPaths = Arrays.asList("/path", "/path2", "/val1");
        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(
                                         input -> input.getPath().containsAll(expectedPaths)
                                                 && input.getDisplayedMethodString().contains("none"));
        Assert.assertTrue(allRight);
    }
}
