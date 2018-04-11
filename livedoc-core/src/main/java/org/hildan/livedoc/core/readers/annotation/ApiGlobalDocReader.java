package org.hildan.livedoc.core.readers.annotation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.jetbrains.annotations.Nullable;

public class ApiGlobalDocReader {

    @NotNull
    public static ApiGlobalDoc read(Collection<Class<?>> globalClasses, Collection<Class<?>> changelogClasses,
            Collection<Class<?>> migrationClasses) {
        ApiGlobalDoc apiGlobalDoc = new ApiGlobalDoc();
        apiGlobalDoc.setSections(buildGeneralSections(globalClasses));
        apiGlobalDoc.setChangelogSet(buildChangelogDoc(changelogClasses));
        apiGlobalDoc.setMigrationSet(buildMigrationDoc(migrationClasses));
        return apiGlobalDoc;
    }

    @NotNull
    private static List<ApiGlobalSectionDoc> buildGeneralSections(Collection<Class<?>> globalClasses) {
        if (globalClasses.isEmpty()) {
            return Collections.emptyList();
        }
        Class<?> clazz = globalClasses.iterator().next();
        ApiGlobal apiGlobal = clazz.getAnnotation(ApiGlobal.class);
        return readSections(apiGlobal);
    }

    @NotNull
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

    @Nullable
    private static ApiChangelogsDoc buildChangelogDoc(Collection<Class<?>> changelogClasses) {
        if (changelogClasses.isEmpty()) {
            return null;
        }
        Class<?> clazz = changelogClasses.iterator().next();
        ApiChangelogSet apiChangelogSet = clazz.getAnnotation(ApiChangelogSet.class);
        ApiChangelogsDoc apiChangelogsDoc = new ApiChangelogsDoc();
        for (ApiChangelog apiChangelog : apiChangelogSet.changelogs()) {
            ApiChangelogDoc apiChangelogDoc = new ApiChangelogDoc();
            apiChangelogDoc.setVersion(apiChangelog.version());
            apiChangelogDoc.setChanges(apiChangelog.changes());
            apiChangelogsDoc.addChangelog(apiChangelogDoc);
        }
        return apiChangelogsDoc;
    }

    @Nullable
    private static ApiMigrationsDoc buildMigrationDoc(Collection<Class<?>> migrationClasses) {
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
            return apiMigrationsDoc;
        }
        return null;
    }
}
