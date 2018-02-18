package org.hildan.livedoc.core.builders.doc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

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

public class ApiGlobalDocReader {

    public static ApiGlobalDoc read(Collection<Class<?>> globalClasses, Collection<Class<?>> changelogClasses,
            Collection<Class<?>> migrationClasses) {
        ApiGlobalDoc apiGlobalDoc = new ApiGlobalDoc();
        apiGlobalDoc = buildGlobalDoc(apiGlobalDoc, globalClasses);
        apiGlobalDoc = buildChangelogDoc(apiGlobalDoc, changelogClasses);
        apiGlobalDoc = buildMigrationDoc(apiGlobalDoc, migrationClasses);
        if (apiGlobalDoc.getSections().isEmpty() && apiGlobalDoc.getChangelogSet().getChangelogs().isEmpty()
                && apiGlobalDoc.getMigrationSet().getMigrations().isEmpty()) {
            return null;
        } else {
            return apiGlobalDoc;
        }
    }

    private static ApiGlobalDoc buildGlobalDoc(ApiGlobalDoc apiGlobalDoc, Collection<Class<?>> globalClasses) {
        if (!globalClasses.isEmpty()) {
            Class<?> clazz = globalClasses.iterator().next();
            ApiGlobal apiGlobal = clazz.getAnnotation(ApiGlobal.class);

            for (ApiGlobalSection section : apiGlobal.sections()) {
                ApiGlobalSectionDoc sectionDoc = new ApiGlobalSectionDoc();
                sectionDoc.setTitle(section.title());
                for (String paragraph : section.paragraphs()) {
                    if (paragraph.startsWith("/jsondocfile:")) {
                        String path = paragraph.replace("/jsondocfile:", "");
                        try {

                            InputStream resourceAsStream = ApiGlobalDocReader.class.getResourceAsStream(path);
                            if (resourceAsStream != null) {
                                String content = getStringFromInputStream(resourceAsStream);
                                sectionDoc.addParagraph(content);

                            } else {
                                sectionDoc.addParagraph("Unable to find file in path: " + path);
                            }

                        } catch (IOException e) {
                            sectionDoc.addParagraph("Unable to find file in path: " + path);
                        }

                    } else {
                        sectionDoc.addParagraph(paragraph);
                    }
                }
                apiGlobalDoc.addApiGlobalSectionDoc(sectionDoc);
            }
        }
        return apiGlobalDoc;
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

    private static String getStringFromInputStream(InputStream is) throws IOException {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return sb.toString();
    }

}
