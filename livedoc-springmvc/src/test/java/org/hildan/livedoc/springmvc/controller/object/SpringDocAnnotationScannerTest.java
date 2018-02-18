package org.hildan.livedoc.springmvc.controller.object;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Test;

import static org.junit.Assert.fail;

public class SpringDocAnnotationScannerTest {

    private static final String VERSION = "1.0";

    private static final String BASE_PATH = "http://localhost:8080/api";

    private static final List<String> PACKAGES = Collections.singletonList("org.hildan.livedoc.springmvc.controller");

    @Test
    public void findsNestedObject() throws Exception {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        Livedoc doc = builder.read(VERSION, BASE_PATH, true, MethodDisplay.URI);

        Map<String, Set<ApiTypeDoc>> objects = doc.getObjects();
        for (Set<ApiTypeDoc> values : objects.values()) {
            assertContainsDoc(values, "NestedObject1");
        }
    }

    @Test
    public void findsDeeplyNestedObjects() throws Exception {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        Livedoc doc = builder.read(VERSION, BASE_PATH, true, MethodDisplay.URI);

        Map<String, Set<ApiTypeDoc>> objects = doc.getObjects();
        for (Set<ApiTypeDoc> values : objects.values()) {
            assertContainsDoc(values, "NestedObject2");
            assertContainsDoc(values, "NestedObject3");
        }
    }

    private void assertContainsDoc(Set<ApiTypeDoc> values, String name) {
        for (ApiTypeDoc apiTypeDoc : values) {
            if (apiTypeDoc.getName().equals(name)) {
                return;
            }
        }
        fail("Could not find ApiTypeDoc with name " + name);
    }
}
