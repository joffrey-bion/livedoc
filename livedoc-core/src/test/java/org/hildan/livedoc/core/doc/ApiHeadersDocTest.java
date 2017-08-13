package org.hildan.livedoc.core.doc;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiHeader;
import org.hildan.livedoc.core.annotation.ApiHeaders;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ApiHeadersDocTest {

    private DocAnnotationScanner scanner = new DefaultDocAnnotationScanner();

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
        final ApiDoc apiDoc = scanner.getApiDocs(Sets.<Class<?>>newHashSet(ApiHeadersController.class),
                MethodDisplay.URI).iterator().next();
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

}
