package org.hildan.livedoc.core.readers.annotation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPages;
import org.hildan.livedoc.core.annotations.global.PageGenerator;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.global.GlobalDocPage;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GlobalDocReaderTest {

    private static final String TEST_API_NAME = "Test API Name";

    private static ApiGlobalDoc buildGlobalDocFor(Class<?> global) {
        GlobalDocReader reader = new LivedocAnnotationGlobalDocReader(ann -> {
            if (ApiGlobalPages.class.equals(ann)) {
                return global == null ? Collections.emptyList() : Collections.singletonList(global);
            }
            return Collections.emptyList();
        });
        List<String> packages = Collections.emptyList();
        LivedocConfiguration configuration = new LivedocConfiguration(packages);
        ApiMetaData apiInfo = new ApiMetaData();
        apiInfo.setName(TEST_API_NAME);
        GlobalTemplateData templateData = new GlobalTemplateData(apiInfo, new LivedocMetaData(), packages);
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
        @ApiGlobalPage(title = "Home", content = "<h1>Title</h1><p>Description</p>")
        @ApiGlobalPage(title = "Secondary", content = "<h1>Title 2</h1><p>Description 2</p>")
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
    public void getApiGlobalDoc_absoluteFileReference() {
        @ApiGlobalPage(title = "From File", resource = "/org/hildan/livedoc/core/readers/annotation/text.txt")
        class GlobalWithFile {}

        assertElementsFromResourceFile(GlobalWithFile.class);
    }

    @Test
    public void getApiGlobalDoc_relativeFileReference() {
        @ApiGlobalPage(title = "From File", resource = "text.txt")
        class GlobalWithFile {}

        assertElementsFromResourceFile(GlobalWithFile.class);
    }

    private static void assertElementsFromResourceFile(Class<?> clazz) {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(clazz);
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

    @Test
    public void getApiGlobalDoc_templateReference() {
        @ApiGlobalPage(title = "From Template", template = "freemarker.ftl")
        class GlobalWithTemplate {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(GlobalWithTemplate.class);
        assertNotNull(apiGlobalDoc);
        assertEquals("from+template", apiGlobalDoc.getHomePageId());

        List<GlobalDocPage> pages = apiGlobalDoc.getPages();
        assertNotNull(pages);
        assertFalse(pages.isEmpty());

        GlobalDocPage page = pages.get(0);
        assertEquals("from+template", page.getLivedocId());
        assertEquals("From Template", page.getTitle());
        assertEquals("Paragraph from template with API name: " + TEST_API_NAME, page.getContent().trim());
    }

    public static class MyPageGenerator implements PageGenerator {
        @Override
        public String generate(GlobalTemplateData templateData) {
            return "Generated test content";
        }
    }

    @Test
    public void getApiGlobalDoc_generator() {
        @ApiGlobalPage(title = "From Generator", generator = MyPageGenerator.class)
        class GlobalWithGenerator {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(GlobalWithGenerator.class);
        assertNotNull(apiGlobalDoc);
        assertEquals("from+generator", apiGlobalDoc.getHomePageId());

        List<GlobalDocPage> pages = apiGlobalDoc.getPages();
        assertNotNull(pages);
        assertFalse(pages.isEmpty());

        GlobalDocPage page = pages.get(0);
        assertEquals("from+generator", page.getLivedocId());
        assertEquals("From Generator", page.getTitle());
        assertEquals("Generated test content", page.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getApiGlobalDoc_generator_failsOnNonPublic() {
        class NonPublicPageGenerator implements PageGenerator {
            @Override
            public String generate(GlobalTemplateData templateData) {
                return "Generated test content";
            }
        }

        @ApiGlobalPage(title = "From Generator", generator = NonPublicPageGenerator.class)
        class GlobalWithGenerator {}

        buildGlobalDocFor(GlobalWithGenerator.class);
    }

    public static class UninstantiablePageGenerator implements PageGenerator {
        public UninstantiablePageGenerator() {
            throw new IllegalStateException("here to test failure at instantiation");
        }

        @Override
        public String generate(GlobalTemplateData templateData) {
            return "Generated test content";
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getApiGlobalDoc_generator_failsOnGeneratorCreation() {

        @ApiGlobalPage(title = "From Generator", generator = UninstantiablePageGenerator.class)
        class GlobalWithGenerator {}

        buildGlobalDocFor(GlobalWithGenerator.class);
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
