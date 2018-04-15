package org.hildan.livedoc.core.readers.annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.global.ApiGlobalPages;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;
import org.hildan.livedoc.core.annotations.global.PageContentType;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.global.GlobalDocPage;
import org.hildan.livedoc.core.templating.FreeMarkerUtils;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
import org.jetbrains.annotations.NotNull;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class ApiGlobalDocReader {

    private final Supplier<Configuration> freemarkerConfigSupplier;

    private final GlobalTemplateData templateData;

    public ApiGlobalDocReader(Supplier<Configuration> freemarkerConfigSupplier, GlobalTemplateData templateData) {
        this.freemarkerConfigSupplier = freemarkerConfigSupplier;
        this.templateData = templateData;
    }

    @NotNull
    public static ApiGlobalDoc readDefault(GlobalTemplateData templateData) {
        ApiGlobalDoc apiGlobalDoc = new ApiGlobalDoc();
        apiGlobalDoc.setPages(createDefaultPages(templateData));
        return apiGlobalDoc;
    }

    private static List<GlobalDocPage> createDefaultPages(GlobalTemplateData templateData) {
        GlobalDocPage defaultHome = new GlobalDocPage("Home", loadDefaultHomeContent(templateData));
        return Collections.singletonList(defaultHome);
    }

    private static String loadDefaultHomeContent(GlobalTemplateData templateData) {
        try {
            Configuration config = FreeMarkerUtils.createDefaultFreeMarkerConfig();
            return FreeMarkerUtils.loadTemplateAsString(config, templateData, "default_global.ftl");
        } catch (IOException e) {
            return "Error: default global doc template missing.\n"
                    + "Please open an issue at https://github.com/joffrey-bion/livedoc/issues";
        } catch (TemplateException e) {
            return "Error: malformed default global doc template.\n"
                    + "Please open an issue at https://github.com/joffrey-bion/livedoc/issues";
        }
    }

    @NotNull
    public static ApiGlobalDoc read(LivedocConfiguration configuration, GlobalTemplateData templateData,
            @NotNull Class<?> globalDocClass) {
        Configuration freeMarkerConfig = configuration.getFreemarkerConfig();
        Supplier<Configuration> classBased = () -> createConfiguration(globalDocClass);
        Supplier<Configuration> configSupplier = () -> freeMarkerConfig;
        Supplier<Configuration> freeMarkerConfigSupplier = freeMarkerConfig == null ? classBased : configSupplier;

        ApiGlobalDocReader reader = new ApiGlobalDocReader(freeMarkerConfigSupplier, templateData);
        ApiGlobalDoc apiGlobalDoc = new ApiGlobalDoc();
        apiGlobalDoc.setPages(reader.readPages(globalDocClass));
        return apiGlobalDoc;
    }

    private static Configuration createConfiguration(Class<?> globalDocClass) {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(globalDocClass, "");
        return configuration;
    }

    @NotNull
    private List<GlobalDocPage> readPages(@NotNull Class<?> globalDocClass) {
        ApiGlobalPage[] apiGlobalPages = getPageAnnotations(globalDocClass);
        return Arrays.stream(apiGlobalPages).map(this::readPage).collect(Collectors.toList());
    }

    private ApiGlobalPage[] getPageAnnotations(@NotNull Class<?> globalDocClass) {
        ApiGlobalPages apiGlobalPages = globalDocClass.getAnnotation(ApiGlobalPages.class);
        if (apiGlobalPages == null) {
            return globalDocClass.getAnnotationsByType(ApiGlobalPage.class);
        } else {
            return apiGlobalPages.value();
        }
    }

    private GlobalDocPage readPage(ApiGlobalPage pageAnnotation) {
        String title = pageAnnotation.title();
        String content = readContent(pageAnnotation.content(), pageAnnotation.type());
        return new GlobalDocPage(title, content);
    }

    private String readContent(String content, PageContentType type) {
        switch (type) {
        case TEXT_FILE:
            return readContentFromResource(content);
        case FREEMARKER:
            return readTemplate(content);
        case STRING:
        default:
            return content;
        }
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

    private String readTemplate(String templateName) {
        Configuration config = freemarkerConfigSupplier.get();
        if (config == null) {
            throw new RuntimeException("Missing FreeMarker configuration");
        }
        try {
            return FreeMarkerUtils.loadTemplateAsString(config, templateData, templateName);
        } catch (IOException e) {
            throw new RuntimeException(String.format("FreeMarker template '%s' not found", templateName), e);
        } catch (TemplateException e) {
            throw new RuntimeException(String.format("Invalid FreeMarker template '%s'", templateName), e);
        }
    }
}
