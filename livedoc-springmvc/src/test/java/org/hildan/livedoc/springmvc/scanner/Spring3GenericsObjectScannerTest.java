package org.hildan.livedoc.springmvc.scanner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Test;

public class Spring3GenericsObjectScannerTest {

    @Test
    public void getLivedoc() throws IOException {
        DocAnnotationScanner scanner = new SpringDocAnnotationScanner();
        String version = "1.0";
        String basePath = "http://localhost:8080/api";
        List<String> packages = Collections.singletonList("org.hildan.livedoc.springmvc.issues.issue174");
        Livedoc doc = scanner.getLivedoc(version, basePath, packages, true, MethodDisplay.URI);

        Map<String, Set<ApiObjectDoc>> objects = doc.getObjects();
        for (Set<ApiObjectDoc> values : objects.values()) {
            for (ApiObjectDoc apiObjectDoc : values) {
                System.out.println(apiObjectDoc.getName());
            }
        }
    }
}
