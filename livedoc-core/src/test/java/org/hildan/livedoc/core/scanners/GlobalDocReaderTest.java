package org.hildan.livedoc.core.scanners;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.annotations.global.ApiChangelog;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiGlobalSection;
import org.hildan.livedoc.core.annotations.global.ApiMigration;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.AnnotatedTypesFinder;
import org.hildan.livedoc.core.GlobalDocReader;
import org.hildan.livedoc.core.LivedocAnnotationGlobalDocReader;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.flow.ApiFlowDoc;
import org.hildan.livedoc.core.pojo.global.ApiGlobalDoc;
import org.hildan.livedoc.core.pojo.global.ApiGlobalSectionDoc;
import org.junit.Assert;
import org.junit.Test;

import static java.util.stream.Collectors.toSet;

public class GlobalDocReaderTest {

    private ApiGlobalDoc buildGlobalDocFor(Class<?> global, Class<?> changelog, Class<?> migration) {
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

    @ApiGlobal(sections = {
            @ApiGlobalSection(title = "title",
                    paragraphs = {"Paragraph 1", "Paragraph 2", "/jsondocfile./src/main/resources/text.txt"})
    })
    private class Global {}

    @Test
    public void testApiGlobalDoc_basic() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(Global.class, null, null);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getSections().size());
        ApiGlobalSectionDoc sectionDoc = apiGlobalDoc.getSections().iterator().next();
        Assert.assertEquals("title", sectionDoc.getTitle());
        Assert.assertEquals(3, sectionDoc.getParagraphs().size());
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

    @ApiChangelogSet(changlogs = {@ApiChangelog(changes = {"Change #1"}, version = "1.0")})
    private class Changelog {}

    @Test
    public void testApiGlobalDoc_changelog() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, Changelog.class, null);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getChangelogset().getChangelogs().size());
    }

    @ApiMigrationSet(migrations = {@ApiMigration(fromversion = "1.0", steps = {"Step #1"}, toversion = "1.1")})
    private class Migration {}

    @Test
    public void testApiGlobalDoc_migration() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, null, Migration.class);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getMigrationset().getMigrations().size());
    }

    @ApiGlobal(sections = {
            @ApiGlobalSection(title = "title",
                    paragraphs = {"Paragraph 1", "Paragraph 2", "/jsondocfile./src/main/resources/text.txt"})
    })
    @ApiChangelogSet(changlogs = {@ApiChangelog(changes = {"Change #1"}, version = "1.0")})
    @ApiMigrationSet(migrations = {@ApiMigration(fromversion = "1.0", steps = {"Step #1"}, toversion = "1.1")})
    private class AllTogether {}

    @Test
    public void testApiGlobalDoc_allTogether() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(AllTogether.class, AllTogether.class, AllTogether.class);
        Assert.assertNotNull(apiGlobalDoc);
        Assert.assertEquals(1, apiGlobalDoc.getSections().size());
        Assert.assertEquals(1, apiGlobalDoc.getMigrationset().getMigrations().size());
        Assert.assertEquals(1, apiGlobalDoc.getChangelogset().getChangelogs().size());
    }

    @ApiFlowSet
    private class TestFlow {

        @ApiFlow(name = "flow", description = "A test flow", group = "Flows A", steps = {
                @ApiFlowStep(apimethodid = "F1"), @ApiFlowStep(apimethodid = "F2"), @ApiFlowStep(apimethodid = "F3")
        })
        public void flow() {

        }

        @ApiFlow(name = "flow2", description = "A test flow 2", group = "Flows B", steps = {
                @ApiFlowStep(apimethodid = "F4"), @ApiFlowStep(apimethodid = "F5"), @ApiFlowStep(apimethodid = "F6")
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

        Map<String, ApiMethodDoc> apiMethodDocsById = new HashMap<>(1);
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setId("F1");
        apiMethodDocsById.put("F1", apiMethodDoc);

        GlobalDocReader scanner = new LivedocAnnotationGlobalDocReader(finder);
        Set<ApiFlowDoc> apiFlowDocs = scanner.getApiFlowDocs(apiMethodDocsById);
        for (ApiFlowDoc apiFlowDoc : apiFlowDocs) {
            if (apiFlowDoc.getName().equals("flow")) {
                Assert.assertEquals("A test flow", apiFlowDoc.getDescription());
                Assert.assertEquals(3, apiFlowDoc.getSteps().size());
                Assert.assertEquals("F1", apiFlowDoc.getSteps().get(0).getApimethodid());
                Assert.assertEquals("F2", apiFlowDoc.getSteps().get(1).getApimethodid());
                Assert.assertEquals("Flows A", apiFlowDoc.getGroup());
                Assert.assertNotNull(apiFlowDoc.getSteps().get(0).getApimethoddoc());
                Assert.assertEquals("F1", apiFlowDoc.getSteps().get(0).getApimethoddoc().getId());
            }

            if (apiFlowDoc.getName().equals("flow2")) {
                Assert.assertEquals("A test flow 2", apiFlowDoc.getDescription());
                Assert.assertEquals(3, apiFlowDoc.getSteps().size());
                Assert.assertEquals("F4", apiFlowDoc.getSteps().get(0).getApimethodid());
                Assert.assertEquals("F5", apiFlowDoc.getSteps().get(1).getApimethodid());
                Assert.assertEquals("Flows B", apiFlowDoc.getGroup());
            }
        }
    }

}
