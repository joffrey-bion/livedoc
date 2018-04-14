package org.hildan.livedoc.core.scanners;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;
import org.hildan.livedoc.core.annotations.global.PageContentType;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.global.GlobalDocPage;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationGlobalDocReader;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GlobalDocReaderTest {

    private static ApiGlobalDoc buildGlobalDocFor(Class<?> global) {
        GlobalDocReader reader = new LivedocAnnotationGlobalDocReader(ann -> {
            if (ApiGlobal.class.equals(ann)) {
                return global == null ? Collections.emptyList() : Collections.singletonList(global);
            }
            return Collections.emptyList();
        });
        List<String> packages = Collections.emptyList();
        LivedocConfiguration configuration = new LivedocConfiguration(packages);
        GlobalTemplateData templateData = new GlobalTemplateData(new ApiMetaData(), new LivedocMetaData(), packages);
        return reader.getApiGlobalDoc(configuration, templateData);
    }

    @Test
    public void getApiGlobalDoc_defaultGlobal() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null);
        assertNotNull(apiGlobalDoc);
        assertFalse(apiGlobalDoc.getHomePageId().isEmpty());

        List<GlobalDocPage> pages = apiGlobalDoc.getPages();
        assertNotNull(pages);
        assertFalse(pages.isEmpty());

        GlobalDocPage page = pages.get(0);
        assertFalse(page.getLivedocId().isEmpty());
        assertFalse(page.getTitle().isEmpty());
        assertFalse(page.getContent().isEmpty());
    }

    @Test
    public void getApiGlobalDoc_basic() {
        @ApiGlobal({
                @ApiGlobalPage(title = "Home", content = "<h1>Title</h1><p>Description</p>"),
                @ApiGlobalPage(title = "Secondary", content = "<h1>Title 2</h1><p>Description 2</p>")
        })
        class Global {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(Global.class);
        assertNotNull(apiGlobalDoc);
        assertEquals("home", apiGlobalDoc.getHomePageId());

        List<GlobalDocPage> pages = apiGlobalDoc.getPages();
        assertNotNull(pages);
        assertFalse(pages.isEmpty());

        GlobalDocPage page0 = pages.get(0);
        assertEquals("home", page0.getLivedocId());
        assertEquals("Home", page0.getTitle());
        assertEquals("<h1>Title</h1><p>Description</p>", page0.getContent());

        GlobalDocPage page1 = pages.get(1);
        assertEquals("secondary", page1.getLivedocId());
        assertEquals("Secondary", page1.getTitle());
        assertEquals("<h1>Title 2</h1><p>Description 2</p>", page1.getContent());
    }

    @Test
    public void getApiGlobalDoc_fileReference() {
        @ApiGlobal({
                @ApiGlobalPage(title = "From File", content = "/test/path/text.txt", type = PageContentType.TEXT_FILE)
        })
        class GlobalWithFile {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(GlobalWithFile.class);
        assertNotNull(apiGlobalDoc);
        assertEquals("from+file", apiGlobalDoc.getHomePageId());

        List<GlobalDocPage> pages = apiGlobalDoc.getPages();
        assertNotNull(pages);
        assertFalse(pages.isEmpty());

        GlobalDocPage page = pages.get(0);
        assertEquals("from+file", page.getLivedocId());
        assertEquals("From File", page.getTitle());
        assertEquals("Paragraph from file", page.getContent());
    }

    @SuppressWarnings("unused")
    @ApiFlowSet
    private class TestFlow {

        @ApiFlow(name = "flow", description = "A test flow", group = "Flows A", steps = {
                @ApiFlowStep(apiOperationId = "F1"),
                @ApiFlowStep(apiOperationId = "F2"),
                @ApiFlowStep(apiOperationId = "F3")
        })
        public void flow() {
        }

        @ApiFlow(name = "flow2", description = "A test flow 2", group = "Flows B", steps = {
                @ApiFlowStep(apiOperationId = "F4"),
                @ApiFlowStep(apiOperationId = "F5"),
                @ApiFlowStep(apiOperationId = "F6")
        })
        public void flow2() {
        }
    }

    @Test
    public void testApiFlowDoc() {
        AnnotatedTypesFinder finder = ann -> {
            if (ApiFlowSet.class.equals(ann)) {
                return Collections.singletonList(TestFlow.class);
            }
            return Collections.emptySet();
        };

        Map<String, ApiOperationDoc> apiOperationDocsById = new HashMap<>(1);
        ApiOperationDoc apiOperationDoc = new ApiOperationDoc();
        apiOperationDoc.setId("F1");
        apiOperationDocsById.put("F1", apiOperationDoc);

        GlobalDocReader scanner = new LivedocAnnotationGlobalDocReader(finder);
        Set<ApiFlowDoc> apiFlowDocs = scanner.getApiFlowDocs(apiOperationDocsById);
        for (ApiFlowDoc apiFlowDoc : apiFlowDocs) {
            if (apiFlowDoc.getName().equals("flow")) {
                assertEquals("A test flow", apiFlowDoc.getDescription());
                assertEquals(3, apiFlowDoc.getSteps().size());
                assertEquals("F1", apiFlowDoc.getSteps().get(0).getApiOperationId());
                assertEquals("F2", apiFlowDoc.getSteps().get(1).getApiOperationId());
                assertEquals("Flows A", apiFlowDoc.getGroup());
                assertNotNull(apiFlowDoc.getSteps().get(0).getApiOperationDoc());
                assertEquals("F1", apiFlowDoc.getSteps().get(0).getApiOperationDoc().getId());
            }

            if (apiFlowDoc.getName().equals("flow2")) {
                assertEquals("A test flow 2", apiFlowDoc.getDescription());
                assertEquals(3, apiFlowDoc.getSteps().size());
                assertEquals("F4", apiFlowDoc.getSteps().get(0).getApiOperationId());
                assertEquals("F5", apiFlowDoc.getSteps().get(1).getApiOperationId());
                assertEquals("Flows B", apiFlowDoc.getGroup());
            }
        }
    }

}
