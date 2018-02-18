package org.hildan.livedoc.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LivedocReaderTest {

    @Test
    public void getLivedoc() {
        String version = "1.0";
        String basePath = "http://localhost:8080/api";
        List<String> packages = Collections.singletonList("org.hildan.livedoc.core.test");
        LivedocReader livedocReader = LivedocReader.basicAnnotationReader(packages);
        Livedoc livedoc = livedocReader.read(version, basePath, true, MethodDisplay.URI);

        checkApis(livedoc);
        checkObjects(livedoc);
        checkFlows(livedoc);
        checkAllVerbsUsed(livedoc);
    }

    private static void checkApis(Livedoc livedoc) {
        Map<String, Set<ApiDoc>> apis = livedoc.getApis();
        assertEquals(1, apis.size());

        Set<ApiDoc> apiDocs = apis.get("");
        assertTrue(apiDocs != null);
        assertEquals(5, apiDocs.size());

        Set<String> controllerNames = apiDocs.stream().map(ApiDoc::getName).collect(Collectors.toSet());
        Set<String> expectedControllers = new HashSet<>();
        expectedControllers.add("interface services");
        expectedControllers.add("Test1Controller");
        expectedControllers.add("Test2Controller");
        expectedControllers.add("Test3Controller");
        expectedControllers.add("TestChildController");
        assertEquals(expectedControllers, controllerNames);
    }

    private static void checkObjects(Livedoc livedoc) {
        Map<String, Set<ApiTypeDoc>> objects = livedoc.getObjects();
        assertEquals(2, objects.size());

        Set<ApiTypeDoc> apiTypeDocs = objects.get("");
        assertTrue(apiTypeDocs != null);

        Set<String> objectNames = apiTypeDocs.stream().map(ApiTypeDoc::getName).collect(Collectors.toSet());
        Set<String> expectedObjectNames = new HashSet<>();
        expectedObjectNames.add("parent");
        expectedObjectNames.add("child");
        expectedObjectNames.add("gender");
        assertEquals(expectedObjectNames, objectNames);

        Set<ApiTypeDoc> apiTypeDocsRestaurant = objects.get("Restaurant");
        assertTrue(apiTypeDocsRestaurant != null);
        assertEquals(1, apiTypeDocsRestaurant.size());

        Set<String> restaurantObjectNames = apiTypeDocsRestaurant.stream()
                                                                 .map(ApiTypeDoc::getName)
                                                                 .collect(Collectors.toSet());
        Set<String> expectedRestaurantObjectNames = new HashSet<>();
        expectedRestaurantObjectNames.add("customPizzaObject");
        assertEquals(expectedRestaurantObjectNames, restaurantObjectNames);
    }

    private static void checkFlows(Livedoc livedoc) {
        Map<String, Set<ApiFlowDoc>> flows = livedoc.getFlows();
        assertEquals(1, flows.size());

        Set<ApiFlowDoc> apiFlowDocs = flows.get("");
        assertNotNull(apiFlowDocs);
        assertEquals(2, apiFlowDocs.size());
    }

    private static void checkAllVerbsUsed(Livedoc livedoc) {
        Set<ApiVerb> verbs = livedoc.getApis()
                                    .values()
                                    .stream()
                                    .flatMap(Collection::stream)
                                    .distinct()
                                    .map(ApiDoc::getMethods)
                                    .flatMap(Collection::stream)
                                    .map(ApiMethodDoc::getVerbs)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toSet());
        Set<ApiVerb> expectedVerbs = new HashSet<>(Arrays.asList(ApiVerb.values()));
        assertEquals(expectedVerbs, verbs);
    }

}
