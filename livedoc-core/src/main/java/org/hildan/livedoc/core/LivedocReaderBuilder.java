package org.hildan.livedoc.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationGlobalDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationTypeDocReader;
import org.hildan.livedoc.core.readers.combined.CombinedDocReader;
import org.hildan.livedoc.core.readers.combined.CombinedTypeDocReader;
import org.hildan.livedoc.core.readers.javadoc.JavadocDocReader;
import org.hildan.livedoc.core.readers.javadoc.JavadocTypeDocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.LivedocPropertyScannerWrapper;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.templates.RecursiveTemplateProvider;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.RecursivePropertyTypeScanner;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.scanners.types.mappers.ConcreteSubtypesMapper;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;
import org.hildan.livedoc.core.scanners.types.references.DefaultTypeReferenceProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

/**
 * A builder to ease the configuration of a {@link LivedocReader}. It provides multiple configuration methods to
 * customize the behaviour of the future {@link LivedocReader}, which is created by calling {@link #build()}. <p>Only
 * the relevant configuration methods need to be called, because this builder already provides sensible defaults, with
 * the exception of {@link #scanningPackages(List)}, which must be called before building the reader.
 */
public class LivedocReaderBuilder {

    private List<String> packages;

    private PropertyScanner propertyScanner;

    private GlobalDocReader globalDocReader;

    private List<DocReader> docReaders = new ArrayList<>();

    private List<TypeDocReader> typeDocReaders = new ArrayList<>();

    private TemplateProvider templateProvider;

    private Map<Type, Object> customExampleValuesByType = new HashMap<>();

    private TypeScanner typeScanner;

    private Predicate<Type> documentedTypesFilter = TypePredicates.IS_CONTAINER.negate();

    private Predicate<Type> typeInspectionFilter = TypePredicates.IS_BASIC_TYPE.negate();

    private Reflections reflections;

    private AnnotatedTypesFinder annotatedTypesFinder;

    /**
     * Defines the packages that should be scanned to find the classes to document. The given list will be used to find
     * controllers and to filter the types to document.
     *
     * @param packages
     *         the array of packages to scan. These are considered package prefixes, subpackages are implicitly
     *         included. If {@code com.example} is in the list, {@code com.example.controllers.MyController} will be
     *         found.
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder scanningPackages(String... packages) {
        return scanningPackages(Arrays.asList(packages));
    }

    /**
     * Defines the packages that should be scanned to find the classes to document. The given list will be used to find
     * controllers and to filter the types to document.
     *
     * @param packages
     *         the list of packages to scan. These are considered package prefixes, subpackages are implicitly included.
     *         If {@code com.example} is in the list, {@code com.example.controllers.MyController} will be found.
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder scanningPackages(List<String> packages) {
        if (packages == null) {
            throw new IllegalArgumentException("The given package list may not be null");
        }
        this.packages = packages;
        return this;
    }

    /**
     * Defines how object properties are inspected. This is used to document the properties in types, and also to
     * generate templates/examples of objects.
     *
     * @param propertyScanner
     *         the {@link PropertyScanner} defining properties on a given type
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder withPropertyScanner(PropertyScanner propertyScanner) {
        this.propertyScanner = new LivedocPropertyScannerWrapper(propertyScanner);
        return this;
    }

    /**
     * Defines the {@link GlobalDocReader} to use to generate the global documentation of a project (general info,
     * flows, migrations).
     *
     * @param reader
     *         the {@link GlobalDocReader} to use
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder withGlobalReader(GlobalDocReader reader) {
        this.globalDocReader = reader;
        return this;
    }

    /**
     * Adds a {@link DocReader} to the configuration. {@link DocReader}s are used to create documentation objects using
     * reflection. They are called in the given order, and the last reader's output has precedence over previous ones.
     * This is why, by default, the {@link LivedocAnnotationDocReader} is last, so that Livedoc annotations override
     * default framework documentations.
     *
     * @param reader
     *         the {@link DocReader} to add to the list
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder addDocReader(DocReader reader) {
        docReaders.add(reader);
        return this;
    }

    /**
     * Adds a {@link TypeDocReader} to the configuration. {@link TypeDocReader}s are used to generate the documentation
     * for the types used in the API. <p> The default uses the configured {@link PropertyScanner}, so it should not
     * usually need to be replaced.
     *
     * @param apiTypeDocReader
     *         the {@link TypeDocReader} to add to the list
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder addTypeDocReader(TypeDocReader apiTypeDocReader) {
        this.typeDocReaders.add(apiTypeDocReader);
        return this;
    }

    /**
     * Defines the {@link TypeScanner} to use to explore the types to document, starting from the types used in the
     * API.
     * <p>The default uses the configured {@link PropertyScanner}, so it should not usually need to be replaced.
     *
     * @param typeScanner
     *         the {@link TypeScanner} to use
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder withTypeScanner(TypeScanner typeScanner) {
        this.typeScanner = typeScanner;
        return this;
    }

    /**
     * Defines a filter for documented types. Any type that doesn't match the given predicate will not be documented.
     *
     * @param shouldBeDocumented
     *         the predicate that types need to verify to be documented
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder withTypeFilter(Predicate<Type> shouldBeDocumented) {
        this.documentedTypesFilter = shouldBeDocumented;
        return this;
    }

    /**
     * Defines a filter for recursion in type exploration. Any type that doesn't match the given predicate will not have
     * its properties inspected. This is useful for classes that express a simple concept and are usually serialized in
     * a single element, but internally encapsulates a lot of complexity (fields/getters). For instance, we could use
     * this to filter out dates, strings, or enums.
     *
     * @param canBeIntrospected
     *         the predicate that types need to verify to be introspected
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder withTypeInspectionFilter(Predicate<Type> canBeIntrospected) {
        this.typeInspectionFilter = canBeIntrospected;
        return this;
    }

    /**
     * Defines the {@link TemplateProvider} to use to create example objects for the types used in the API. <p> The
     * default uses the configured {@link PropertyScanner}, so it should not usually need to be replaced. To add custom
     * templates, {@link #addDefaultTemplate(Type, Object)} and {@link #addDefaultTemplates(Map)} should be preferred.
     *
     * @param templateProvider
     *         the {@link TemplateProvider} to use
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder withTemplateProvider(TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
        return this;
    }

    /**
     * Defines the example instance to use for the given type in templates.
     *
     * @param type
     *         the type to define the example for
     * @param example
     *         the instance to use as example
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder addDefaultTemplate(Type type, Object example) {
        customExampleValuesByType.put(type, example);
        return this;
    }

    /**
     * Defines the example instances to use for the given types in templates.
     *
     * @param defaultTemplates
     *         a mapping between types and their corresponding example instance
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReaderBuilder addDefaultTemplates(Map<? extends Type, Object> defaultTemplates) {
        this.customExampleValuesByType.putAll(defaultTemplates);
        return this;
    }

    /**
     * Creates a new {@link LivedocReader} using the current configuration. Please note that {@link
     * #scanningPackages(List)} should be called before this method.
     *
     * @return this {@code LivedocReaderBuilder}, to satisfy the builder pattern for easy chaining
     */
    public LivedocReader build() {
        if (packages == null) {
            throw new IllegalStateException("No packages white list provided");
        }
        documentedTypesFilter = documentedTypesFilter.and(TypePredicates.isInPackage(packages));
        if (propertyScanner == null) {
            propertyScanner = getDefaultPropertyScanner();
        }
        if (typeScanner == null) {
            typeScanner = getDefaultTypeScanner(propertyScanner, documentedTypesFilter, typeInspectionFilter, packages);
        }
        if (typeDocReaders.isEmpty()) {
            typeDocReaders.add(new JavadocTypeDocReader());
            typeDocReaders.add(new LivedocAnnotationTypeDocReader());
        }
        if (templateProvider == null) {
            templateProvider = getDefaultTemplateProvider(propertyScanner, typeInspectionFilter);
        }
        if (globalDocReader == null) {
            globalDocReader = getDefaultGlobalReader(packages);
        }
        if (docReaders.isEmpty()) {
            docReaders.add(new JavadocDocReader());
            docReaders.add(new LivedocAnnotationDocReader(getAnnotatedTypesFinder(packages)));
        }
        DocReader combinedDocReader = new CombinedDocReader(docReaders);
        TypeDocReader combinedTypeDocReader = new CombinedTypeDocReader(typeDocReaders);
        TypeReferenceProvider typeRefProvider = getDefaultTypeReferenceProvider(documentedTypesFilter);
        return new LivedocReader(combinedDocReader, combinedTypeDocReader, globalDocReader, typeScanner,
                propertyScanner, templateProvider, typeRefProvider);
    }

    private static LivedocPropertyScannerWrapper getDefaultPropertyScanner() {
        return new LivedocPropertyScannerWrapper(new FieldPropertyScanner());
    }

    private TypeScanner getDefaultTypeScanner(PropertyScanner propertyScanner, Predicate<? super Class<?>> typeFilter,
            Predicate<Type> typeInspectionFilter, List<String> packages) {
        RecursivePropertyTypeScanner scanner = new RecursivePropertyTypeScanner(propertyScanner);
        // excludes collections/maps from doc (would just be noise)
        scanner.setTypeFilter(typeFilter);
        // do not explore the fields of simple types like primitive wrappers, strings and enums
        scanner.setTypeInspectionFilter(typeInspectionFilter);
        scanner.setTypeMapper(new ConcreteSubtypesMapper(getReflections(packages)));
        return scanner;
    }

    @NotNull
    private static DefaultTypeReferenceProvider getDefaultTypeReferenceProvider(Predicate<? super Type> typeFilter) {
        return new DefaultTypeReferenceProvider(typeFilter);
    }

    private static TypeDocReader getDefaultApiObjectDocReader(PropertyScanner propertyScanner) {
        return new LivedocAnnotationTypeDocReader();
    }

    private TemplateProvider getDefaultTemplateProvider(PropertyScanner propertyScanner,
            Predicate<Type> typeInspectionFilter) {
        return new RecursiveTemplateProvider(propertyScanner, typeInspectionFilter, customExampleValuesByType);
    }

    private GlobalDocReader getDefaultGlobalReader(List<String> packages) {
        return new LivedocAnnotationGlobalDocReader(getAnnotatedTypesFinder(packages));
    }

    private AnnotatedTypesFinder getAnnotatedTypesFinder(List<String> packages) {
        if (annotatedTypesFinder == null) {
            annotatedTypesFinder = LivedocUtils.createAnnotatedTypesFinder(getReflections(packages));
        }
        return annotatedTypesFinder;
    }

    private Reflections getReflections(List<String> packages) {
        if (reflections == null) {
            reflections = LivedocUtils.newReflections(packages);
        }
        return reflections;
    }
}
