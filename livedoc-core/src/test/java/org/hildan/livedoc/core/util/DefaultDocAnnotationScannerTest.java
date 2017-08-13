package org.hildan.livedoc.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import static org.junit.Assert.assertEquals;

public class DefaultDocAnnotationScannerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger log = LoggerFactory.getLogger(DefaultDocAnnotationScannerTest.class);

    @Test
    public void getLivedoc() throws IOException {
        DocAnnotationScanner scanner = new DefaultDocAnnotationScanner();
        String version = "1.0";
        String basePath = "http://localhost:8080/api";
        List<String> packages = Collections.singletonList("org.hildan.livedoc.core.util");
        Livedoc livedoc = scanner.getLivedoc(version, basePath, packages, true, MethodDisplay.URI);
        assertEquals(1, livedoc.getApis().size());

        int countApis = 0;
        for (String string : livedoc.getApis().keySet()) {
            countApis += livedoc.getApis().get(string).size();
        }
        assertEquals(4, countApis);

        assertEquals(3, livedoc.getObjects().size());

        int countFlows = 0;
        for (String string : livedoc.getFlows().keySet()) {
            countFlows += livedoc.getFlows().get(string).size();
        }
        assertEquals(2, countFlows);

        int countObjects = 0;
        for (String string : livedoc.getObjects().keySet()) {
            countObjects += livedoc.getObjects().get(string).size();
        }
        assertEquals(10, countObjects);

        Set<ApiVerb> apiVerbs = getAllTestedApiVerbs(livedoc);
        assertEquals(ApiVerb.values().length, apiVerbs.size());

        log.debug(objectMapper.writeValueAsString(livedoc));
    }

    private Set<ApiVerb> getAllTestedApiVerbs(Livedoc livedoc) {
        Set<ApiVerb> apiVerbs = new HashSet<ApiVerb>();

        for (String string : livedoc.getObjects().keySet()) {
            Set<ApiDoc> apiDocs = livedoc.getApis().get(string);
            if (apiDocs != null) {
                for (ApiDoc doc : apiDocs) {
                    for (ApiMethodDoc apiMethodDoc : doc.getMethods()) {
                        apiVerbs.addAll(Sets.newHashSet(apiMethodDoc.getVerb()));
                    }
                }
            }
        }

        return apiVerbs;
    }

}
