package org.hildan.livedoc.core;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.meta.LivedocMetaDataReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

/**
 * Builds a {@link Livedoc} object representing an API documentation by reading java classes and their annotations.
 */
public class LivedocReader {

    private final GlobalDocReader globalDocReader;

    private final MasterApiDocReader masterApiDocReader;

    private final TypeScanner typeScanner;

    private final MasterTypeDocReader masterTypeDocReader;

    private final TemplateProvider templateProvider;

    private final TypeReferenceProvider typeReferenceProvider;

    /**
     * Creates a new {@link LivedocReader} with the given configuration.
     *
     * @param docReader
     *         the {@link DocReader} to use to generate the documentation for API operations
     * @param typeDocReader
     *         the {@link TypeDocReader} to use to generate the documentation for the types used in the API
     * @param globalDocReader
     *         the {@link GlobalDocReader} to use to generate the global documentation of a project (general info,
     *         flows, migrations)
     * @param typeScanner
     *         the {@link TypeScanner} to use to retrieve all the types that should be documented, starting from the
     *         types that are directly referenced in the API (as request or response body, mainly)
     * @param propertyScanner
     *         defines the properties of a type
     * @param templateProvider
     *         the {@link TemplateProvider} to use to create example objects for the types used in the API
     * @param typeReferenceProvider
     *         the {@link TypeReferenceProvider} to use to build {@link LivedocType}s for documentation elements,
     */
    public LivedocReader(DocReader docReader, TypeDocReader typeDocReader, GlobalDocReader globalDocReader,
            TypeScanner typeScanner, PropertyScanner propertyScanner, TemplateProvider templateProvider,
            TypeReferenceProvider typeReferenceProvider) {
        this.masterApiDocReader = new MasterApiDocReader(docReader);
        this.masterTypeDocReader = new MasterTypeDocReader(typeDocReader, propertyScanner);
        this.globalDocReader = globalDocReader;
        this.typeScanner = typeScanner;
        this.templateProvider = templateProvider;
        this.typeReferenceProvider = typeReferenceProvider;
    }

    /**
     * Creates a new {@link LivedocReader} with a default configuration.
     *
     * @param packages
     *         the packages that should be scanned to find the classes to document. The given list will be used to find
     *         controllers and to filter the types to document.
     *
     * @return the newly created {@link LivedocReader}
     */
    public static LivedocReader basicAnnotationReader(List<String> packages) {
        return new LivedocReaderBuilder().scanningPackages(packages).build();
    }

    /**
     * Reads a {@link Livedoc} object by inspecting classes as configured.
     *
     * @param apiInfo
     *         the current version of the API
     * @param configuration
     *         the configuration to use to generate the doc
     *
     * @return a new {@link Livedoc} object representing the API
     */
    public Livedoc read(ApiMetaData apiInfo, LivedocConfiguration configuration) {

        Collection<ApiDoc> apiDocs = masterApiDocReader.readApiDocs(typeReferenceProvider, templateProvider);

        Set<Class<?>> types = getClassesToDocument();
        List<ApiTypeDoc> apiTypeDocs = readApiTypeDocs(types);

        Map<String, ApiOperationDoc> apiOperationDocsById = getAllApiOperationDocsById(apiDocs);
        Set<ApiFlowDoc> apiFlowDocs = globalDocReader.getApiFlowDocs(apiOperationDocsById);

        ApiGlobalDoc apiGlobalDoc = globalDocReader.getApiGlobalDoc();

        Livedoc livedoc = new Livedoc(LivedocMetaDataReader.read(), apiInfo);
        livedoc.setPlaygroundEnabled(configuration.isPlaygroundEnabled());
        livedoc.setDisplayMethodAs(configuration.getDisplayMethodAs());
        livedoc.setApis(group(apiDocs));
        livedoc.setTypes(group(apiTypeDocs));
        livedoc.setFlows(group(apiFlowDocs));
        livedoc.setGlobal(apiGlobalDoc);
        return livedoc;
    }

    private Map<String, ApiOperationDoc> getAllApiOperationDocsById(Collection<ApiDoc> apiDocs) {
        return apiDocs.stream()
                      .flatMap(api -> api.getOperations().stream())
                      .filter(m -> m.getId() != null && !m.getId().isEmpty())
                      .collect(Collectors.toMap(ApiOperationDoc::getId, x -> x));
    }

    private Set<Class<?>> getClassesToDocument() {
        Set<Type> rootTypes = typeReferenceProvider.getProvidedReferences();
        return typeScanner.findTypesToDocument(rootTypes);
    }

    // for testing purposes only
    public Optional<ApiDoc> readApiDoc(Class<?> controller) {
        return masterApiDocReader.readApiDoc(controller, typeReferenceProvider, templateProvider);
    }

    private List<ApiTypeDoc> readApiTypeDocs(Collection<Class<?>> types) {
        return buildDocs(types, this::readApiTypeDoc);
    }

    private Optional<ApiTypeDoc> readApiTypeDoc(Class<?> type) {
        return masterTypeDocReader.buildTypeDoc(type, typeReferenceProvider, templateProvider);
    }

    private static <T, D extends Comparable<D>> List<D> buildDocs(Collection<T> objects,
            Function<T, Optional<D>> read) {
        return objects.stream()
                      .map(read)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .sorted()
                      .collect(Collectors.toList());
    }

    private static <T extends Groupable & Comparable<T>> List<Group<T>> group(Collection<T> elements) {
        Map<String, List<T>> groupedElements = elements.stream().collect(Collectors.groupingBy(Groupable::getGroup));
        return groupedElements.entrySet()
                              .stream()
                              .sorted(Comparator.comparing(Entry::getKey))
                              .map(e -> Group.sorted(e.getKey(), e.getValue()))
                              .collect(Collectors.toList());
    }
}
