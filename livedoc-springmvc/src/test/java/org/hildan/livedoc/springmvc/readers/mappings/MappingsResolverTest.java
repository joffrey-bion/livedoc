package org.hildan.livedoc.springmvc.readers.mappings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class MappingsResolverTest {

    @SuppressWarnings("unused")
    @Controller
    private static class StandardController {

        @RequestMapping
        public void none() {
        }

        @RequestMapping("/")
        public void slash() {
        }

        @RequestMapping("/path")
        public void slashPath() {
        }

        @RequestMapping("/long/path")
        public void slashLongPath() {
        }

        @RequestMapping(path = "/path/attr")
        public void pathAttribute() {
        }
    }

    @Test
    public void getRequestMappings_noClassAnnotation() throws NoSuchMethodException {
        Class<?> ctrl = StandardController.class;
        Method none = ctrl.getDeclaredMethod("none");
        Method slash = ctrl.getDeclaredMethod("slash");
        Method slashPath = ctrl.getDeclaredMethod("slashPath");
        Method longPath = ctrl.getDeclaredMethod("slashLongPath");
        Method pathAttribute = ctrl.getDeclaredMethod("pathAttribute");

        assertEquals(singletonList("/"), MappingsResolver.getRequestMappings(none, ctrl));
        assertEquals(singletonList("/"), MappingsResolver.getRequestMappings(slash, ctrl));
        assertEquals(singletonList("/path"), MappingsResolver.getRequestMappings(slashPath, ctrl));
        assertEquals(singletonList("/long/path"), MappingsResolver.getRequestMappings(longPath, ctrl));
        assertEquals(singletonList("/path/attr"), MappingsResolver.getRequestMappings(pathAttribute, ctrl));
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    private static class EmptyAnnotatedController {

        @RequestMapping
        public void none() {
        }

        @RequestMapping("/test")
        public void test() {
        }
    }

    @Test
    public void getRequestMappings_emptyClassAnnotation() throws NoSuchMethodException {
        Class<?> ctrl = EmptyAnnotatedController.class;
        Method none = ctrl.getDeclaredMethod("none");
        Method test = ctrl.getDeclaredMethod("test");

        assertEquals(singletonList("/"), MappingsResolver.getRequestMappings(none, ctrl));
        assertEquals(singletonList("/test"), MappingsResolver.getRequestMappings(test, ctrl));
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping("/prefix")
    private static class PrefixAnnotatedController {

        @RequestMapping
        public void none() {
        }

        @RequestMapping("/test")
        public void test() {
        }
    }

    @Test
    public void getRequestMappings_classAnnotationWithPrefix() throws NoSuchMethodException {
        Class<?> ctrl = PrefixAnnotatedController.class;
        Method none = ctrl.getDeclaredMethod("none");
        Method test = ctrl.getDeclaredMethod("test");

        assertEquals(singletonList("/prefix"), MappingsResolver.getRequestMappings(none, ctrl));
        assertEquals(singletonList("/prefix/test"), MappingsResolver.getRequestMappings(test, ctrl));
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping("/child")
    private static class PrefixOverrideChildController extends PrefixAnnotatedController {

    }

    @Test
    public void getRequestMappings_childController_prefixOverride() throws NoSuchMethodException {
        Class<?> ctrl = PrefixOverrideChildController.class;
        Method none = ctrl.getMethod("none");
        Method test = ctrl.getMethod("test");

        assertEquals(singletonList("/child"), MappingsResolver.getRequestMappings(none, ctrl));
        assertEquals(singletonList("/child/test"), MappingsResolver.getRequestMappings(test, ctrl));
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping({"/path1", "/path2/"})
    private static class MultiPathController {

        @RequestMapping({"/path3", "path4"})
        public void test() {
        }
    }

    @Test
    public void getRequestMappings_multiplePaths() throws NoSuchMethodException {
        Class<?> ctrl = MultiPathController.class;
        Method test = ctrl.getMethod("test");

        List<String> expectedPaths = Arrays.asList("/path1/path3", "/path1/path4", "/path2/path3", "/path2/path4");
        assertEquals(expectedPaths, MappingsResolver.getRequestMappings(test, ctrl));
    }
}
