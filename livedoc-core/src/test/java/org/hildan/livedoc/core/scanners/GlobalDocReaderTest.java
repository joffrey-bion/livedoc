package org.hildan.livedoc.core.scanners;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.annotations.global.ApiChangelog;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiMigration;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationGlobalDocReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        return reader.getApiGlobalDoc(new ApiMetaData(), new LivedocMetaData(), new LivedocConfiguration());
    }

    @Test
    public void getApiGlobalDoc_defaultGlobal() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, null, null);
        assertNotNull(apiGlobalDoc);
        assertNotNull(apiGlobalDoc.getGeneral());
        assertNotEquals("", apiGlobalDoc.getGeneral()); // default doc should be loaded
        assertNull(apiGlobalDoc.getChangelogSet());
        assertNull(apiGlobalDoc.getMigrationSet());
    }

    @Test
    public void getApiGlobalDoc_basic() {
        @ApiGlobal("<h1>Title</h1><p>Description</p>")
        class Global {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(Global.class, null, null);
        assertNotNull(apiGlobalDoc);
        assertEquals("<h1>Title</h1><p>Description</p>", apiGlobalDoc.getGeneral());
        assertNull(apiGlobalDoc.getChangelogSet());
        assertNull(apiGlobalDoc.getMigrationSet());
    }

    @Test
    public void getApiGlobalDoc_fileReference() {
        @ApiGlobal("/livedocfile:/test/path/text.txt")
        class GlobalWithFile {}

        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(GlobalWithFile.class, null, null);
        assertNotNull(apiGlobalDoc);
        assertEquals("Paragraph from file", apiGlobalDoc.getGeneral());
        assertNull(apiGlobalDoc.getChangelogSet());
        assertNull(apiGlobalDoc.getMigrationSet());
    }

    @ApiChangelogSet(changelogs = {@ApiChangelog(changes = {"Change #1"}, version = "1.0")})
    private class Changelog {}

    @Test
    public void getApiGlobalDoc_changelog() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, Changelog.class, null);
        assertNotNull(apiGlobalDoc);
        assertEquals(1, apiGlobalDoc.getChangelogSet().getChangelogs().size());
    }

    @ApiMigrationSet(migrations = {@ApiMigration(fromVersion = "1.0", steps = {"Step #1"}, toVersion = "1.1")})
    private class Migration {}

    @Test
    public void getApiGlobalDoc_migration() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(null, null, Migration.class);
        assertNotNull(apiGlobalDoc);
        assertEquals(1, apiGlobalDoc.getMigrationSet().getMigrations().size());
    }

    @ApiGlobal("General doc")
    @ApiChangelogSet(changelogs = {@ApiChangelog(changes = {"Change #1"}, version = "1.0")})
    @ApiMigrationSet(migrations = {@ApiMigration(fromVersion = "1.0", steps = {"Step #1"}, toVersion = "1.1")})
    private class AllTogether {}

    @Test
    public void getApiGlobalDoc_allTogether() {
        ApiGlobalDoc apiGlobalDoc = buildGlobalDocFor(AllTogether.class, AllTogether.class, AllTogether.class);
        assertNotNull(apiGlobalDoc);
        assertEquals("General doc", apiGlobalDoc.getGeneral());
        assertEquals(1, apiGlobalDoc.getMigrationSet().getMigrations().size());
        assertEquals(1, apiGlobalDoc.getChangelogSet().getChangelogs().size());
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
