package org.hildan.livedoc.springmvc.scanner;

import java.util.Arrays;
import java.util.List;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.Assert.assertTrue;

public class MappingsResolverTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    private static class SpringController {

        @RequestMapping("/path")
        public void slashPath() {
        }

        @RequestMapping("/")
        public void path() {
        }

        @RequestMapping()
        public void none() {
        }
    }

    @Test
    public void testPath() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        Assert.assertEquals("SpringController", apiDoc.getName());

        boolean slashPath = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().contains("/path"));
        assertTrue(slashPath);

        boolean slash = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().contains("/"));
        assertTrue(slash);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    private static class SpringController2 {

        @RequestMapping
        public void none() {
        }

        @RequestMapping("/test")
        public void test() {
        }
    }

    @Test
    public void testPath2() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController2.class);
        Assert.assertEquals("SpringController2", apiDoc.getName());

        boolean none = apiDoc.getOperations().stream().anyMatch(input -> {
            System.out.println(input.getPaths());
            return input.getPaths().contains("/");
        });
        assertTrue(none);

        boolean test = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().contains("/test"));
        assertTrue(test);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping("/child")
    private static class SpringControllerChild extends SpringController2 {


    }

    @Test
    public void testPathInherited() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringControllerChild.class);
        Assert.assertEquals("SpringControllerChild", apiDoc.getName());

        boolean none = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().contains("/child"));
        assertTrue(none);

        boolean test = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().contains("/child/test"));
        assertTrue(test);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping({"/path1", "/path2/"})
    private static class SpringController3 {

        @RequestMapping({"/path3", "path4"})
        public void none() {
        }
    }

    @Test
    public void testPath3() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController3.class);
        Assert.assertEquals("SpringController3", apiDoc.getName());

        boolean allRight = apiDoc.getOperations()
                                 .stream()
                                 .anyMatch(input -> input.getPaths()
                                                         .containsAll(Arrays.asList("/path1/path3", "/path1/path4",
                                                                 "/path2/path3", "/path2/path4")));
        assertTrue(allRight);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping("/path")
    private static class SpringController4 {

        @RequestMapping
        public void none() {
        }
    }

    @Test
    public void testPath4() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController4.class);
        Assert.assertEquals("SpringController4", apiDoc.getName());

        boolean allRight = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().contains("/path"));
        assertTrue(allRight);
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(path = {"/path", "/path2"}, value = "/val1")
    private static class SpringController5 {

        @RequestMapping
        public void none() {
        }
    }

    @Test
    public void testPath5() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController5.class);
        Assert.assertEquals("SpringController5", apiDoc.getName());

        List<String> expectedPaths = Arrays.asList("/path", "/path2", "/val1");
        boolean allRight = apiDoc.getOperations().stream().anyMatch(input -> input.getPaths().containsAll(expectedPaths));
        assertTrue(allRight);
    }
}
