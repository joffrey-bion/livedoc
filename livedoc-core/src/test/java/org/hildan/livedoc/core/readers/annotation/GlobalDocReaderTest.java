package org.hildan.livedoc.core.readers.annotation;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPages;
import org.hildan.livedoc.core.annotations.global.PageGenerator;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.global.GlobalDoc;
import org.hildan.livedoc.core.model.doc.global.GlobalDocPage;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GlobalDocReaderTest {

    private static final String TEST_API_NAME = "Test API Name";

    private static GlobalDoc buildGlobalDocFor(Class<?> global) {
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
        GlobalDoc globalDoc = buildGlobalDocFor(null);
        assertNotNull(globalDoc);
        assertFalse(globalDoc.getHomePageId().isEmpty());

        List<GlobalDocPage> pages = globalDoc.getPages();
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

        GlobalDoc globalDoc = buildGlobalDocFor(Global.class);
        assertNotNull(globalDoc);
        assertEquals("home", globalDoc.getHomePageId());

        List<GlobalDocPage> pages = globalDoc.getPages();
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
        GlobalDoc globalDoc = buildGlobalDocFor(clazz);
        assertNotNull(globalDoc);
        assertEquals("from+file", globalDoc.getHomePageId());

        List<GlobalDocPage> pages = globalDoc.getPages();
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

        GlobalDoc globalDoc = buildGlobalDocFor(GlobalWithTemplate.class);
        assertNotNull(globalDoc);
        assertEquals("from+template", globalDoc.getHomePageId());

        List<GlobalDocPage> pages = globalDoc.getPages();
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

        GlobalDoc globalDoc = buildGlobalDocFor(GlobalWithGenerator.class);
        assertNotNull(globalDoc);
        assertEquals("from+generator", globalDoc.getHomePageId());

        List<GlobalDocPage> pages = globalDoc.getPages();
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
}
