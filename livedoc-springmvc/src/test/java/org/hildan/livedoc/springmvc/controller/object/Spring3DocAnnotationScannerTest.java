package org.hildan.livedoc.springmvc.controller.object;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.hildan.livedoc.springmvc.scanner.Spring3DocAnnotationScanner;
import org.junit.Test;

import static org.junit.Assert.fail;

public class Spring3DocAnnotationScannerTest {

    private static final String VERSION = "1.0";

    private static final String BASE_PATH = "http://localhost:8080/api";

    private static final List<String> PACKAGES = Collections.singletonList("org.hildan.livedoc.springmvc.controller");

    @Test
    public void getJSONDoc() throws IOException {
        DocAnnotationScanner scanner = new Spring3DocAnnotationScanner();
        Livedoc doc = scanner.getLivedoc(VERSION, BASE_PATH, PACKAGES, true, MethodDisplay.URI);

        Map<String, Set<ApiObjectDoc>> objects = doc.getObjects();
        for (Set<ApiObjectDoc> values : objects.values()) {
            for (ApiObjectDoc apiObjectDoc : values) {
                System.out.println(apiObjectDoc.getName());
            }
        }

    }

    @Test
    public void findsNestedObject() throws Exception {
        DocAnnotationScanner scanner = new Spring3DocAnnotationScanner();
        Livedoc jsondoc = scanner.getLivedoc(VERSION, BASE_PATH, PACKAGES, true, MethodDisplay.URI);

        Map<String, Set<ApiObjectDoc>> objects = jsondoc.getObjects();
        for (Set<ApiObjectDoc> values : objects.values()) {
            assertContainsDoc(values, "NestedObject1");
        }
    }

    @Test
    public void findsDeeplyNestedObjects() throws Exception {
        DocAnnotationScanner scanner = new Spring3DocAnnotationScanner();
        Livedoc jsondoc = scanner.getLivedoc(VERSION, BASE_PATH, PACKAGES, true, MethodDisplay.URI);

        Map<String, Set<ApiObjectDoc>> objects = jsondoc.getObjects();
        for (Set<ApiObjectDoc> values : objects.values()) {
            assertContainsDoc(values, "NestedObject2");
            assertContainsDoc(values, "NestedObject3");
        }
    }

    private void assertContainsDoc(Set<ApiObjectDoc> values, String name) {
        for (ApiObjectDoc apiObjectDoc : values) {
            if (apiObjectDoc.getName().equals(name)) {
                return;
            }
        }
        fail("Could not find ApiObjectDoc with name " + name);
    }
}
