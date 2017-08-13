package org.hildan.livedoc.core.scanner.builder;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

public class JSONDocApiMethodPathBuilderTest {

    JSONDocScanner jsondocScanner = new DefaultJSONDocScanner();

    @Api(name = "test-path", description = "test-path")
    private class Controller {

        @ApiMethod(path = {"/path1", "/path2"})
        public void path() {

        }

    }

    @Test
    public void testPathWithMethodDisplayURI() {
        ApiDoc apiDoc = jsondocScanner.getApiDocs(Sets.<Class<?>>newHashSet(Controller.class), MethodDisplay.URI)
                                      .iterator()
                                      .next();

        boolean allRight = FluentIterable.from(apiDoc.getMethods()).anyMatch(new Predicate<ApiMethodDoc>() {
            @Override
            public boolean apply(ApiMethodDoc input) {
                return input.getPath().contains("/path1") && input.getPath().contains("/path2")
                        && input.getDisplayedMethodString().contains("/path1") && input.getDisplayedMethodString()
                                                                                       .contains("/path2");
            }

        });

        Assert.assertTrue(allRight);
    }

    @Test
    public void testPathWithMethodDisplayMethod() {
        ApiDoc apiDoc = jsondocScanner.getApiDocs(Sets.<Class<?>>newHashSet(Controller.class), MethodDisplay.METHOD)
                                      .iterator()
                                      .next();

        boolean allRight = FluentIterable.from(apiDoc.getMethods()).anyMatch(new Predicate<ApiMethodDoc>() {
            @Override
            public boolean apply(ApiMethodDoc input) {
                return input.getPath().contains("/path1") && input.getPath().contains("/path2")
                        && input.getDisplayedMethodString().contains("path") && !input.getDisplayedMethodString()
                                                                                      .contains("/path1");
            }

        });

        Assert.assertTrue(allRight);
    }

}
