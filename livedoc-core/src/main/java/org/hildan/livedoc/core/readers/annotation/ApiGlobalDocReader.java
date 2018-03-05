package org.hildan.livedoc.core.readers.annotation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.global.ApiChangelog;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiGlobalSection;
import org.hildan.livedoc.core.annotations.global.ApiMigration;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.model.doc.global.ApiChangelogDoc;
import org.hildan.livedoc.core.model.doc.global.ApiChangelogsDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalSectionDoc;
import org.hildan.livedoc.core.model.doc.global.ApiMigrationDoc;
import org.hildan.livedoc.core.model.doc.global.ApiMigrationsDoc;
import org.jetbrains.annotations.NotNull;

public class ApiGlobalDocReader {

    public static ApiGlobalDoc read(Collection<Class<?>> globalClasses, Collection<Class<?>> changelogClasses,
            Collection<Class<?>> migrationClasses) {
        ApiGlobalDoc apiGlobalDoc = new ApiGlobalDoc();
        apiGlobalDoc = buildGeneralSections(apiGlobalDoc, globalClasses);
        apiGlobalDoc = buildChangelogDoc(apiGlobalDoc, changelogClasses);
        apiGlobalDoc = buildMigrationDoc(apiGlobalDoc, migrationClasses);

        boolean noGeneralSections = apiGlobalDoc.getSections().isEmpty();
        boolean noChangeLogs = apiGlobalDoc.getChangelogSet().getChangelogs().isEmpty();
        boolean noMigrations = apiGlobalDoc.getMigrationSet().getMigrations().isEmpty();

        if (noGeneralSections && noChangeLogs && noMigrations) {
            return null;
        }
        return apiGlobalDoc;
    }

    private static ApiGlobalDoc buildGeneralSections(ApiGlobalDoc apiGlobalDoc, Collection<Class<?>> globalClasses) {
        if (!globalClasses.isEmpty()) {
            Class<?> clazz = globalClasses.iterator().next();
            ApiGlobal apiGlobal = clazz.getAnnotation(ApiGlobal.class);
            apiGlobalDoc.setSections(readSections(apiGlobal));
        }
        return apiGlobalDoc;
    }

    private static List<ApiGlobalSectionDoc> readSections(ApiGlobal apiGlobal) {
        return Arrays.stream(apiGlobal.sections()).map(ApiGlobalDocReader::readSection).collect(Collectors.toList());
    }

    @NotNull
    private static ApiGlobalSectionDoc readSection(@NotNull ApiGlobalSection section) {
        ApiGlobalSectionDoc sectionDoc = new ApiGlobalSectionDoc();
        sectionDoc.setTitle(section.title());
        sectionDoc.setParagraphs(readParagraphs(section));
        return sectionDoc;
    }

    @NotNull
    private static List<String> readParagraphs(@NotNull ApiGlobalSection section) {
        return Arrays.stream(section.paragraphs()).map(ApiGlobalDocReader::readParagraph).collect(Collectors.toList());
    }

    @NotNull
    private static String readParagraph(@NotNull String paragraph) {
        if (paragraph.startsWith(ApiGlobalSection.FILE_PREFIX)) {
            String path = paragraph.substring(ApiGlobalSection.FILE_PREFIX.length());
            return readContentFromResource(path);
        }
        return paragraph;
    }

    @NotNull
    private static String readContentFromResource(@NotNull String path) {
        try {
            InputStream resourceAsStream = ApiGlobalDocReader.class.getResourceAsStream(path);
            if (resourceAsStream == null) {
                throw new IllegalArgumentException("Unable to find file at path: " + path);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            return reader.lines().collect(Collectors.joining());
        } catch (UncheckedIOException e) {
            throw new IllegalArgumentException("Unable to read file at path: " + path, e);
        }
    }

    private static ApiGlobalDoc buildChangelogDoc(ApiGlobalDoc apiGlobalDoc, Collection<Class<?>> changelogClasses) {
        if (!changelogClasses.isEmpty()) {
            Class<?> clazz = changelogClasses.iterator().next();
            ApiChangelogSet apiChangelogSet = clazz.getAnnotation(ApiChangelogSet.class);
            ApiChangelogsDoc apiChangelogsDoc = new ApiChangelogsDoc();
            for (ApiChangelog apiChangelog : apiChangelogSet.changelogs()) {
                ApiChangelogDoc apiChangelogDoc = new ApiChangelogDoc();
                apiChangelogDoc.setVersion(apiChangelog.version());
                apiChangelogDoc.setChanges(apiChangelog.changes());
                apiChangelogsDoc.addChangelog(apiChangelogDoc);
            }
            apiGlobalDoc.setChangelogSet(apiChangelogsDoc);
        }
        return apiGlobalDoc;
    }

    private static ApiGlobalDoc buildMigrationDoc(ApiGlobalDoc apiGlobalDoc, Collection<Class<?>> migrationClasses) {
        if (!migrationClasses.isEmpty()) {
            Class<?> clazz = migrationClasses.iterator().next();
            ApiMigrationSet apiMigrationSet = clazz.getAnnotation(ApiMigrationSet.class);
            ApiMigrationsDoc apiMigrationsDoc = new ApiMigrationsDoc();
            for (ApiMigration apiMigration : apiMigrationSet.migrations()) {
                ApiMigrationDoc apiMigrationDoc = new ApiMigrationDoc();
                apiMigrationDoc.setFromVersion(apiMigration.fromVersion());
                apiMigrationDoc.setToVersion(apiMigration.toVersion());
                apiMigrationDoc.setSteps(apiMigration.steps());
                apiMigrationsDoc.addMigration(apiMigrationDoc);
            }
            apiGlobalDoc.setMigrationSet(apiMigrationsDoc);
        }
        return apiGlobalDoc;
    }
}
