package org.hildan.livedoc.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.pojo.flow.ApiFlowDoc;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        Map<String, Set<ApiObjectDoc>> objects = livedoc.getObjects();
        assertEquals(2, objects.size());

        Set<ApiObjectDoc> apiObjectDocs = objects.get("");
        assertTrue(apiObjectDocs != null);
        assertEquals(3, apiObjectDocs.size());

        Set<String> objectNames = apiObjectDocs.stream().map(ApiObjectDoc::getName).collect(Collectors.toSet());
        Set<String> expectedObjectNames = new HashSet<>();
        expectedObjectNames.add("parent");
        expectedObjectNames.add("child");
        expectedObjectNames.add("gender");
        assertEquals(expectedObjectNames, objectNames);

        Set<ApiObjectDoc> apiObjectDocsRestaurant = objects.get("Restaurant");
        assertTrue(apiObjectDocsRestaurant != null);
        assertEquals(1, apiObjectDocsRestaurant.size());

        Set<String> restaurantObjectNames = apiObjectDocsRestaurant.stream()
                                                                   .map(ApiObjectDoc::getName)
                                                                   .collect(Collectors.toSet());
        Set<String> expectedRestaurantObjectNames = new HashSet<>();
        expectedRestaurantObjectNames.add("customPizzaObject");
        assertEquals(expectedRestaurantObjectNames, restaurantObjectNames);
    }

    private static void checkFlows(Livedoc livedoc) {
        Map<String, Set<ApiFlowDoc>> flows = livedoc.getFlows();
        assertEquals(1, flows.size());

        Set<ApiFlowDoc> apiFlowDocs = flows.get("");
        assertTrue(apiFlowDocs != null);
        assertEquals(2, apiFlowDocs.size());
    }

    private static void checkAllVerbsUsed(Livedoc livedoc) {
        List<ApiVerb> verbs = livedoc.getApis()
                                     .values()
                                     .stream()
                                     .flatMap(Collection::stream)
                                     .distinct()
                                     .map(ApiDoc::getMethods)
                                     .flatMap(Collection::stream)
                                     .map(ApiMethodDoc::getVerb)
                                     .flatMap(Collection::stream)
                                     .distinct()
                                     .sorted()
                                     .collect(Collectors.toList());
        List<ApiVerb> expectedVerbs = new ArrayList<>();
        Collections.addAll(expectedVerbs, ApiVerb.values());
        expectedVerbs.remove(ApiVerb.UNDEFINED); // we should always have a valid default value, not UNDEFINED
        assertEquals(expectedVerbs, verbs);
    }

}
