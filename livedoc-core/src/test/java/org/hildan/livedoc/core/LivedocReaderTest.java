package org.hildan.livedoc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        List<Group<ApiDoc>> apis = livedoc.getApis();
        assertEquals(1, apis.size());

        Group<ApiDoc> apiDocsGroup = apis.get(0);
        assertNotNull(apiDocsGroup);
        assertEquals("", apiDocsGroup.getGroupName());

        List<ApiDoc> apiDocs = apiDocsGroup.getElements();
        assertNotNull(apiDocs);
        assertEquals(5, apiDocs.size());

        List<String> controllerNames = apiDocs.stream().map(ApiDoc::getName).collect(Collectors.toList());
        List<String> expectedControllers = new ArrayList<>(5);
        expectedControllers.add("Test1Controller");
        expectedControllers.add("Test2Controller");
        expectedControllers.add("Test3Controller");
        expectedControllers.add("TestChildController");
        expectedControllers.add("interface services"); // lowercase comes after
        assertEquals(expectedControllers, controllerNames);
    }

    private static void checkObjects(Livedoc livedoc) {
        List<Group<ApiTypeDoc>> typesGroups = livedoc.getTypes();
        assertEquals(2, typesGroups.size());

        Group<ApiTypeDoc> anonymousGroup = typesGroups.get(0);
        assertNotNull(anonymousGroup);
        assertEquals("", anonymousGroup.getGroupName());

        List<ApiTypeDoc> anonymousGroupTypes = anonymousGroup.getElements();
        assertNotNull(anonymousGroupTypes);
        assertEquals(3, anonymousGroupTypes.size());

        List<String> objectNames = anonymousGroupTypes.stream().map(ApiTypeDoc::getName).collect(Collectors.toList());
        List<String> expectedObjectNames = new ArrayList<>(3);
        expectedObjectNames.add("child");
        expectedObjectNames.add("gender");
        expectedObjectNames.add("parent");
        assertEquals(expectedObjectNames, objectNames);

        Group<ApiTypeDoc> restaurantGroup = typesGroups.get(1);
        assertNotNull(restaurantGroup);
        assertEquals("Restaurant", restaurantGroup.getGroupName());

        List<ApiTypeDoc> restaurantTypes = restaurantGroup.getElements();
        assertNotNull(restaurantTypes);
        assertEquals(1, restaurantTypes.size());

        List<String> restaurantObjectNames = restaurantTypes.stream()
                                                            .map(ApiTypeDoc::getName)
                                                            .collect(Collectors.toList());
        List<String> expectedRestaurantObjectNames = new ArrayList<>();
        expectedRestaurantObjectNames.add("customPizzaObject");
        assertEquals(expectedRestaurantObjectNames, restaurantObjectNames);
    }

    private static void checkFlows(Livedoc livedoc) {
        List<Group<ApiFlowDoc>> flowGroups = livedoc.getFlows();
        assertEquals(1, flowGroups.size());

        Group<ApiFlowDoc> flowGroup = flowGroups.get(0);
        assertNotNull(flowGroup);
        assertEquals("", flowGroup.getGroupName());
        assertNotNull(flowGroup.getElements());
        assertEquals(2, flowGroup.getElements().size());
    }

    private static void checkAllVerbsUsed(Livedoc livedoc) {
        Set<ApiVerb> verbs = livedoc.getApis()
                                    .stream()
                                    .map(Group::getElements)
                                    .flatMap(Collection::stream)
                                    .distinct()
                                    .map(ApiDoc::getOperations)
                                    .flatMap(Collection::stream)
                                    .map(ApiOperationDoc::getVerbs)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toSet());
        Set<ApiVerb> expectedVerbs = new HashSet<>(Arrays.asList(ApiVerb.values()));
        assertEquals(expectedVerbs, verbs);
    }

}
