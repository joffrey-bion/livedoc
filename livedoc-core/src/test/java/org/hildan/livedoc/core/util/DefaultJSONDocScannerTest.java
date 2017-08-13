package org.hildan.livedoc.core.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.JSONDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import static org.junit.Assert.assertEquals;

public class DefaultJSONDocScannerTest {
    private String version = "1.0";

    private String basePath = "http://localhost:8080/api";

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger log = LoggerFactory.getLogger(DefaultJSONDocScannerTest.class);

    @Test
    public void getJSONDoc() throws IOException {
        JSONDocScanner jsondocScanner = new DefaultJSONDocScanner();
        JSONDoc jsondoc = jsondocScanner.getJSONDoc(version, basePath,
                Lists.newArrayList("org.hildan.livedoc.core.util"), true, MethodDisplay.URI);
        assertEquals(1, jsondoc.getApis().size());

        int countApis = 0;
        for (String string : jsondoc.getApis().keySet()) {
            countApis += jsondoc.getApis().get(string).size();
        }
        assertEquals(4, countApis);

        assertEquals(3, jsondoc.getObjects().size());

        int countFlows = 0;
        for (String string : jsondoc.getFlows().keySet()) {
            countFlows += jsondoc.getFlows().get(string).size();
        }
        assertEquals(2, countFlows);

        int countObjects = 0;
        for (String string : jsondoc.getObjects().keySet()) {
            countObjects += jsondoc.getObjects().get(string).size();
        }
        assertEquals(10, countObjects);

        Set<ApiVerb> apiVerbs = getAllTestedApiVerbs(jsondoc);
        assertEquals(ApiVerb.values().length, apiVerbs.size());

        log.debug(objectMapper.writeValueAsString(jsondoc));
    }

    private Set<ApiVerb> getAllTestedApiVerbs(JSONDoc jsondoc) {
        Set<ApiVerb> apiVerbs = new HashSet<ApiVerb>();

        for (String string : jsondoc.getObjects().keySet()) {
            Set<ApiDoc> apiDocs = jsondoc.getApis().get(string);
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
