package org.hildan.livedoc.springmvc.controller.object;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.LivedocBuilder;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocBuilderFactory;
import org.junit.Test;

import static org.junit.Assert.fail;

public class SpringDocAnnotationScannerTest {

    private static final String VERSION = "1.0";

    private static final String BASE_PATH = "http://localhost:8080/api";

    private static final List<String> PACKAGES = Collections.singletonList("org.hildan.livedoc.springmvc.controller");

    @Test
    public void findsNestedObject() throws Exception {
        LivedocBuilder builder = SpringLivedocBuilderFactory.springLivedocBuilder(Collections.emptyList());
        Livedoc doc = builder.build(VERSION, BASE_PATH, PACKAGES, true, MethodDisplay.URI);

        Map<String, Set<ApiObjectDoc>> objects = doc.getObjects();
        for (Set<ApiObjectDoc> values : objects.values()) {
            assertContainsDoc(values, "NestedObject1");
        }
    }

    @Test
    public void findsDeeplyNestedObjects() throws Exception {
        LivedocBuilder builder = SpringLivedocBuilderFactory.springLivedocBuilder(Collections.emptyList());
        Livedoc doc = builder.build(VERSION, BASE_PATH, PACKAGES, true, MethodDisplay.URI);

        Map<String, Set<ApiObjectDoc>> objects = doc.getObjects();
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
