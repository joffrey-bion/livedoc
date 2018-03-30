package org.hildan.livedoc.core;

import java.lang.reflect.Method;
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

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.annotation.ApiTypeDocReader;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.hildan.livedoc.core.validators.ApiOperationDocDefaults;
import org.hildan.livedoc.core.validators.ApiOperationDocValidator;
import org.hildan.livedoc.core.validators.ApiTypeDocValidator;

/**
 * A component able to create an API documentation by inspecting classes and reading their annotations.
 */
public class LivedocReader {

    private final GlobalDocReader globalDocReader;

    private final DocReader docReader;

    private final TypeScanner typeScanner;

    private final ApiTypeDocReader apiTypeDocReader;

    private final TemplateProvider templateProvider;

    private final TypeReferenceProvider typeReferenceProvider;

    /**
     * Creates a new {@link LivedocReader} with the given configuration.
     *
     * @param docReader
     *         the {@link DocReader} to use to generate the documentation for API operations
     * @param apiTypeDocReader
     *         the {@link ApiTypeDocReader} to use to generate the documentation for the types used in the API
     * @param globalDocReader
     *         the {@link GlobalDocReader} to use to generate the global documentation of a project (general info,
     *         flows, migrations)
     * @param typeScanner
     *         the {@link TypeScanner} to use to explore the types to document, starting from the types used in the API
     * @param templateProvider
     *         the {@link TemplateProvider} to use to create example objects for the types used in the API
     * @param typeReferenceProvider
     *         the {@link TypeReferenceProvider} to use to build {@link LivedocType}s for documentation elements
     */
    public LivedocReader(DocReader docReader, ApiTypeDocReader apiTypeDocReader, GlobalDocReader globalDocReader,
            TypeScanner typeScanner, TemplateProvider templateProvider, TypeReferenceProvider typeReferenceProvider) {
        this.typeScanner = typeScanner;
        this.apiTypeDocReader = apiTypeDocReader;
        this.globalDocReader = globalDocReader;
        this.docReader = docReader;
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
     * @param version
     *         the current version of the API
     * @param basePath
     *         the base path to use for the playground
     * @param playgroundEnabled
     *         whether the playground is enabled or not
     * @param displayMethodAs
     *         the way methods should be displayed in the UI
     *
     * @return a new {@link Livedoc} object representing the API
     */
    public Livedoc read(String version, String basePath, boolean playgroundEnabled, MethodDisplay displayMethodAs) {

        Collection<Class<?>> controllers = docReader.findControllerTypes();
        Collection<ApiDoc> apiDocs = readApiDocs(controllers);

        Set<Class<?>> types = getClassesToDocument();
        List<ApiTypeDoc> apiTypeDocs = readApiTypeDocs(types);

        Map<String, ApiOperationDoc> apiOperationDocsById = getAllApiOperationDocsById(apiDocs);
        Set<ApiFlowDoc> apiFlowDocs = globalDocReader.getApiFlowDocs(apiOperationDocsById);

        ApiGlobalDoc apiGlobalDoc = globalDocReader.getApiGlobalDoc();

        Livedoc livedoc = new Livedoc(version, basePath);
        livedoc.setPlaygroundEnabled(playgroundEnabled);
        livedoc.setDisplayMethodAs(displayMethodAs);
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

    private Collection<ApiDoc> readApiDocs(Collection<Class<?>> controllers) {
        return buildDocs(controllers, this::readApiDoc);
    }

    public Optional<ApiDoc> readApiDoc(Class<?> controller) {
        Optional<ApiDoc> doc = docReader.buildApiDocBase(controller);
        doc.ifPresent(apiDoc -> apiDoc.setOperations(readApiOperationDocs(controller, apiDoc)));
        return doc;
    }

    private List<ApiOperationDoc> readApiOperationDocs(Class<?> controller, ApiDoc doc) {
        return buildDocs(getApiOperationMethods(controller), m -> readApiOperationDoc(m, controller, doc));
    }

    private List<Method> getApiOperationMethods(Class<?> controller) {
        return LivedocUtils.getAllMethods(controller)
                           .stream()
                           .filter(m -> docReader.isApiOperation(m, controller))
                           .collect(Collectors.toList());
    }

    private Optional<ApiOperationDoc> readApiOperationDoc(Method method, Class<?> controller, ApiDoc parentApiDoc) {
        Optional<ApiOperationDoc> doc = docReader.buildApiOperationDoc(method, controller, parentApiDoc,
                typeReferenceProvider, templateProvider);
        doc.ifPresent(apiOperationDoc -> {
            ApiOperationDocDefaults.complete(apiOperationDoc);
            ApiOperationDocValidator.validate(apiOperationDoc);
        });
        return doc;
    }

    private List<ApiTypeDoc> readApiTypeDocs(Collection<Class<?>> types) {
        return buildDocs(types, this::readApiTypeDoc);
    }

    private Optional<ApiTypeDoc> readApiTypeDoc(Class<?> type) {
        ApiTypeDoc doc = apiTypeDocReader.read(type, typeReferenceProvider, templateProvider);
        ApiTypeDocValidator.validate(doc);
        return Optional.of(doc);
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
