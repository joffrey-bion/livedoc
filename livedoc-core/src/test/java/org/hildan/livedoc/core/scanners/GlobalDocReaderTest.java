package org.hildan.livedoc.core.scanners;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.GlobalDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationGlobalDocReader;
import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.annotations.global.ApiChangelog;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiGlobalSection;
import org.hildan.livedoc.core.annotations.global.ApiMigration;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalSectionDoc;
import org.junit.Assert;
import org.junit.Test;

import static java.util.stream.Collectors.toSet;

public class GlobalDocReaderTest {

    private static ApiGlobalDoc buildGlobalDocFor(Class<?> global, Class<?> changelog, Class<?> migration) {
        GlobalDocReader reader = new LivedocAnnotationGlobalDocReader(ann -> {
            if (ApiGlobal.class.equals(ann)) {
                return global == null ? Collections.emptyList() : Collections.singletonList(global);
            }
            if (ApiChangelogSet.class.equals(ann)) {
                return changelog == null ? Collections.emptyList() : Collections.singletonList(changelog);
            }
            if (ApiMigrationSet.class.equals(ann)) {
                return migration == null ? Collections.emptyList() : Collections.singletonList(migration);
            }
            return Collections.emptyList();
        });
        return reader.getApiGlobalDoc();
    }

    @Test
    public void testApiGlobalDoc_basic() {
        @ApiGlobal(sections = {
                @ApiGlobalSection(title = "title", paragraphs = {"Paragraph 1", "Paragraph 2"})
        })
        class Global {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(Global.class, null, null);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getSections().size());
        ApiGlobalSectionDoc sectionDoc = apiGlobalDoc.getSections().get(0);
        Assert.assertEquals("title", sectionDoc.getTitle());

        List<String> paragraphs = Arrays.asList("Paragraph 1", "Paragraph 2");
        Assert.assertEquals(paragraphs, sectionDoc.getParagraphs());
    }

    @Test
    public void testApiGlobalDoc_fileReference() {
        @ApiGlobal(sections = {
                @ApiGlobalSection(title = "title",
                        paragraphs = {"Paragraph 1", "Paragraph 2", "/livedocfile:/test/path/text.txt"})
        })
        class GlobalWithFile {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(GlobalWithFile.class, null, null);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getSections().size());
        ApiGlobalSectionDoc sectionDoc = apiGlobalDoc.getSections().get(0);
        Assert.assertEquals("title", sectionDoc.getTitle());

        List<String> paragraphs = Arrays.asList("Paragraph 1", "Paragraph 2", "Paragraph from file");
        Assert.assertEquals(paragraphs, sectionDoc.getParagraphs());
    }

    @ApiGlobal(sections = {
            @ApiGlobalSection(title = "section1", paragraphs = {"Paragraph 1"}),
            @ApiGlobalSection(title = "abc", paragraphs = {"Paragraph 1", "Paragraph2"}),
            @ApiGlobalSection(title = "198xyz", paragraphs = {"Paragraph 1", "Paragraph2", "Paragraph3", "Paragraph4"}),
    })
    private class MultipleGlobalSections {}

    @Test
    public void testApiGlobalDoc_multipleSections() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(MultipleGlobalSections.class, null, null);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(3, apiGlobalDoc.getSections().size());

        Set<String> expectedTitles = new HashSet<>();
        expectedTitles.add("section1");
        expectedTitles.add("abc");
        expectedTitles.add("198xyz");

        Set<String> actualTitles = apiGlobalDoc.getSections()
                                               .stream()
                                               .map(ApiGlobalSectionDoc::getTitle)
                                               .collect(toSet());
        Assert.assertEquals(expectedTitles, actualTitles);
    }

    @ApiChangelogSet(changelogs = {@ApiChangelog(changes = {"Change #1"}, version = "1.0")})
    private class Changelog {}

    @Test
    public void testApiGlobalDoc_changelog() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, Changelog.class, null);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getChangelogSet().getChangelogs().size());
    }

    @ApiMigrationSet(migrations = {@ApiMigration(fromVersion = "1.0", steps = {"Step #1"}, toVersion = "1.1")})
    private class Migration {}

    @Test
    public void testApiGlobalDoc_migration() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, null, Migration.class);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getMigrationSet().getMigrations().size());
    }

    @ApiGlobal(sections = {
            @ApiGlobalSection(title = "title", paragraphs = {"Paragraph 1", "Paragraph 2"})
    })
    @ApiChangelogSet(changelogs = {@ApiChangelog(changes = {"Change #1"}, version = "1.0")})
    @ApiMigrationSet(migrations = {@ApiMigration(fromVersion = "1.0", steps = {"Step #1"}, toVersion = "1.1")})
    private class AllTogether {}

    @Test
    public void testApiGlobalDoc_allTogether() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(AllTogether.class, AllTogether.class, AllTogether.class);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getSections().size());
        Assert.assertEquals(1, apiGlobalDoc.getMigrationSet().getMigrations().size());
        Assert.assertEquals(1, apiGlobalDoc.getChangelogSet().getChangelogs().size());
    }

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
                Assert.assertEquals("A test flow", apiFlowDoc.getDescription());
                Assert.assertEquals(3, apiFlowDoc.getSteps().size());
                Assert.assertEquals("F1", apiFlowDoc.getSteps().get(0).getApiOperationId());
                Assert.assertEquals("F2", apiFlowDoc.getSteps().get(1).getApiOperationId());
                Assert.assertEquals("Flows A", apiFlowDoc.getGroup());
                Assert.assertNotNull(apiFlowDoc.getSteps().get(0).getApiOperationDoc());
                Assert.assertEquals("F1", apiFlowDoc.getSteps().get(0).getApiOperationDoc().getId());
            }

            if (apiFlowDoc.getName().equals("flow2")) {
                Assert.assertEquals("A test flow 2", apiFlowDoc.getDescription());
                Assert.assertEquals(3, apiFlowDoc.getSteps().size());
                Assert.assertEquals("F4", apiFlowDoc.getSteps().get(0).getApiOperationId());
                Assert.assertEquals("F5", apiFlowDoc.getSteps().get(1).getApiOperationId());
                Assert.assertEquals("Flows B", apiFlowDoc.getGroup());
            }
        }
    }

}
