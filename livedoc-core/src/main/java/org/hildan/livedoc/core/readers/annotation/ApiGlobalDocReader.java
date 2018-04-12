package org.hildan.livedoc.core.readers.annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.global.ApiChangelog;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiMigration;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.global.ApiChangelogDoc;
import org.hildan.livedoc.core.model.doc.global.ApiChangelogsDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.global.ApiMigrationDoc;
import org.hildan.livedoc.core.model.doc.global.ApiMigrationsDoc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ApiGlobalDocReader {

    private static final Logger logger = LoggerFactory.getLogger(ApiGlobalDocReader.class);

    private final Configuration templateConfig;

    private final Map<String, Object> model;

    public ApiGlobalDocReader(ApiMetaData apiInfo, LivedocMetaData livedocInfo, LivedocConfiguration config) {
        templateConfig = new Configuration();
        templateConfig.setClassForTemplateLoading(ApiGlobalDocReader.class, "");
        model = new HashMap<>();
        model.put("apiInfo", apiInfo);
        model.put("livedocInfo", livedocInfo);
        model.put("config", config);
    }

    @NotNull
    public ApiGlobalDoc read(Collection<Class<?>> globalClasses, Collection<Class<?>> changelogClasses,
            Collection<Class<?>> migrationClasses) {
        ApiGlobalDoc apiGlobalDoc = new ApiGlobalDoc();
        apiGlobalDoc.setGeneral(buildHomePage(globalClasses));
        apiGlobalDoc.setChangelogSet(buildChangelogsDoc(changelogClasses));
        apiGlobalDoc.setMigrationSet(buildMigrationsDoc(migrationClasses));
        return apiGlobalDoc;
    }

    @NotNull
    private String buildHomePage(Collection<Class<?>> globalClasses) {
        ApiGlobal apiGlobal = extractSingleAnnotation(ApiGlobal.class, globalClasses);
        if (apiGlobal == null) {
            return loadDefaultGlobalDoc();
        }
        return readContent(apiGlobal.value());
    }

    private String loadDefaultGlobalDoc() {
        try {
            Template template = templateConfig.getTemplate("default_global.ftl");
            StringWriter out = new StringWriter();
            template.process(model, out);
            return out.toString();
        } catch (IOException e) {
            return "Error: default global doc template missing.\n"
                    + "Please open an issue at https://github.com/joffrey-bion/livedoc/issues";
        } catch (TemplateException e) {
            return "Error: malformed default global doc template.\n"
                    + "Please open an issue at https://github.com/joffrey-bion/livedoc/issues";
        }
    }

    @NotNull
    private static String readContent(@NotNull String content) {
        if (content.startsWith(ApiGlobal.FILE_PREFIX)) {
            String path = content.substring(ApiGlobal.FILE_PREFIX.length());
            return readContentFromResource(path);
        }
        return content;
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
    private static ApiChangelogsDoc buildChangelogsDoc(Collection<Class<?>> changelogClasses) {
        ApiChangelogSet apiChangelogSet = extractSingleAnnotation(ApiChangelogSet.class, changelogClasses);
        if (apiChangelogSet == null) {
            return null;
        }
        ApiChangelogsDoc apiChangelogsDoc = new ApiChangelogsDoc();
        for (ApiChangelog apiChangelog : apiChangelogSet.changelogs()) {
            apiChangelogsDoc.addChangelog(buildChangelogDoc(apiChangelog));
        }
        return apiChangelogsDoc;
    }

    @NotNull
    private static ApiChangelogDoc buildChangelogDoc(ApiChangelog apiChangelog) {
        ApiChangelogDoc apiChangelogDoc = new ApiChangelogDoc();
        apiChangelogDoc.setVersion(apiChangelog.version());
        apiChangelogDoc.setChanges(apiChangelog.changes());
        return apiChangelogDoc;
    }

    @Nullable
    private static ApiMigrationsDoc buildMigrationsDoc(Collection<Class<?>> migrationClasses) {
        ApiMigrationSet apiMigrationSet = extractSingleAnnotation(ApiMigrationSet.class, migrationClasses);
        if (apiMigrationSet == null) {
            return null;
        }
        ApiMigrationsDoc apiMigrationsDoc = new ApiMigrationsDoc();
        for (ApiMigration apiMigration : apiMigrationSet.migrations()) {
            apiMigrationsDoc.addMigration(buildMigrationDoc(apiMigration));
        }
        return apiMigrationsDoc;
    }

    @NotNull
    private static ApiMigrationDoc buildMigrationDoc(ApiMigration apiMigration) {
        ApiMigrationDoc apiMigrationDoc = new ApiMigrationDoc();
        apiMigrationDoc.setFromVersion(apiMigration.fromVersion());
        apiMigrationDoc.setToVersion(apiMigration.toVersion());
        apiMigrationDoc.setSteps(apiMigration.steps());
        return apiMigrationDoc;
    }

    @Nullable
    private static <A extends Annotation> A extractSingleAnnotation(Class<A> annotationClass,
            Collection<Class<?>> annotatedClasses) {
        if (annotatedClasses.isEmpty()) {
            return null;
        }
        if (annotatedClasses.size() > 1) {
            logger.warn("Multiple classes annotated {} were found, only one such class is supported",
                    annotationClass.getSimpleName());
        }
        Class<?> clazz = annotatedClasses.iterator().next();
        return clazz.getAnnotation(annotationClass);
    }
}
