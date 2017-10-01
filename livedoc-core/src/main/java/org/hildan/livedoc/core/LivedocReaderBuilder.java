package org.hildan.livedoc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hildan.livedoc.core.builders.doc.ApiObjectDocReader;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.LivedocPropertyScannerWrapper;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.templates.RecursiveTemplateProvider;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.RecursivePropertyTypeScanner;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.scanners.types.mappers.ConcreteSubtypesMapper;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.reflections.Reflections;

public class LivedocReaderBuilder {

    private List<String> packages;

    private PropertyScanner propertyScanner;

    private GlobalDocReader globalDocReader;

    private List<DocReader> docReaders = new ArrayList<>();

    private ApiObjectDocReader apiObjectDocReader;

    private TemplateProvider templateProvider;

    private Map<Class<?>, Object> defaultTemplates = new HashMap<>();

    private TypeScanner typeScanner;

    private Reflections reflections;

    private AnnotatedTypesFinder annotatedTypesFinder;

    public LivedocReaderBuilder scanningPackages(String... packages) {
        return scanningPackages(Arrays.asList(packages));
    }

    public LivedocReaderBuilder scanningPackages(List<String> packages) {
        if (packages == null) {
            throw new IllegalArgumentException("The given package list may not be null");
        }
        this.packages = packages;
        return this;
    }

    public LivedocReaderBuilder withPropertyScanner(PropertyScanner propertyScanner) {
        this.propertyScanner = propertyScanner;
        return this;
    }

    public LivedocReaderBuilder withGlobalReader(GlobalDocReader reader) {
        this.globalDocReader = reader;
        return this;
    }

    public LivedocReaderBuilder addDocReader(DocReader reader) {
        this.docReaders.add(reader);
        return this;
    }

    public LivedocReaderBuilder withObjectDocReader(ApiObjectDocReader apiObjectDocReader) {
        this.apiObjectDocReader = apiObjectDocReader;
        return this;
    }

    public LivedocReaderBuilder withTypeScanner(TypeScanner typeScanner) {
        this.typeScanner = typeScanner;
        return this;
    }

    public LivedocReaderBuilder withTemplateProvider(TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
        return this;
    }

    public LivedocReaderBuilder addDefaultTemplate(Class<?> type, Object example) {
        this.defaultTemplates.put(type, example);
        return this;
    }

    public LivedocReaderBuilder addDefaultTemplates(Map<Class<?>, Object> defaultTemplates) {
        this.defaultTemplates.putAll(defaultTemplates);
        return this;
    }

    public LivedocReader build() {
        if (packages == null) {
            throw new IllegalStateException("No packages white list provided");
        }
        if (propertyScanner == null) {
            propertyScanner = getDefaultPropertyScanner();
        }
        if (typeScanner == null) {
            typeScanner = getDefaultTypeScanner(propertyScanner);
        }
        if (apiObjectDocReader == null) {
            apiObjectDocReader = getDefaultApiObjectDocReader(propertyScanner);
        }
        if (templateProvider == null) {
            templateProvider = getDefaultTemplateProvider(propertyScanner);
        }
        if (globalDocReader == null) {
            globalDocReader = getDefaultGlobalReader();
        }
        if (docReaders.isEmpty()) {
            docReaders.add(new LivedocAnnotationDocReader(getAnnotatedTypesFinder()));
        }
        return new LivedocReader(packages, typeScanner, globalDocReader, apiObjectDocReader, docReaders,
                templateProvider);
    }

    private static LivedocPropertyScannerWrapper getDefaultPropertyScanner() {
        return new LivedocPropertyScannerWrapper(new FieldPropertyScanner());
    }

    private TypeScanner getDefaultTypeScanner(PropertyScanner propertyScanner) {
        RecursivePropertyTypeScanner scanner = new RecursivePropertyTypeScanner(propertyScanner);
        // excludes collections/maps from doc (would just be noise)
        scanner.setTypeFilter(TypePredicates.IS_CONTAINER.negate());
        // do not explore the fields of simple types like primitive wrappers, strings and enums
        scanner.setTypeExplorationFilter(TypePredicates.IS_BASIC_TYPE.negate());
        scanner.setTypeMapper(new ConcreteSubtypesMapper(getReflections()));
        return scanner;
    }

    private static ApiObjectDocReader getDefaultApiObjectDocReader(PropertyScanner propertyScanner) {
        return new ApiObjectDocReader(propertyScanner);
    }

    private TemplateProvider getDefaultTemplateProvider(PropertyScanner propertyScanner) {
        return new RecursiveTemplateProvider(propertyScanner, TypePredicates.IS_BASIC_TYPE.negate(), defaultTemplates);
    }

    private GlobalDocReader getDefaultGlobalReader() {
        return new LivedocAnnotationGlobalDocReader(getAnnotatedTypesFinder());
    }

    private AnnotatedTypesFinder getAnnotatedTypesFinder() {
        if (annotatedTypesFinder == null) {
            annotatedTypesFinder = LivedocUtils.createAnnotatedTypesFinder(getReflections());
        }
        return annotatedTypesFinder;
    }

    private Reflections getReflections() {
        if (reflections == null) {
            reflections = LivedocUtils.newReflections(packages);
        }
        return reflections;
    }
}
