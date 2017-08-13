package org.hildan.livedoc.core.scanner.readers;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ApiMethodPathReaderTest {

    private DocAnnotationScanner scanner = new DefaultDocAnnotationScanner();

    @Api(name = "test-path", description = "test-path")
    private class Controller {

        @ApiMethod(path = {"/path1", "/path2"})
        public void path() {

        }

    }

    @Test
    public void testPathWithMethodDisplayURI() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(Controller.class), MethodDisplay.URI)
                               .iterator()
                               .next();

        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPath().contains("/path1") && input.getPath()
                                                                                               .contains("/path2")
                                         && input.getDisplayedMethodString().contains("/path1")
                                         && input.getDisplayedMethodString().contains("/path2"));

        Assert.assertTrue(allRight);
    }

    @Test
    public void testPathWithMethodDisplayMethod() {
        ApiDoc apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(Controller.class), MethodDisplay.METHOD)
                               .iterator()
                               .next();

        boolean allRight = apiDoc.getMethods()
                                 .stream()
                                 .anyMatch(input -> input.getPath().contains("/path1") && input.getPath()
                                                                                               .contains("/path2")
                                         && input.getDisplayedMethodString().contains("path")
                                         && !input.getDisplayedMethodString().contains("/path1"));

        Assert.assertTrue(allRight);
    }

}
