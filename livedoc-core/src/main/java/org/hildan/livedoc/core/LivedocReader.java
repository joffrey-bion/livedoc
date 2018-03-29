package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.readers.annotation.ApiTypeDocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;
import org.hildan.livedoc.core.scanners.types.references.DefaultTypeReferenceProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.validators.ApiOperationDocDefaults;
import org.hildan.livedoc.core.validators.ApiOperationDocValidator;
import org.hildan.livedoc.core.validators.ApiTypeDocValidator;

/**
 * A component able to create an API documentation by inspecting classes and reading their annotations.
 */
public class LivedocReader {

    private final List<String> packageWhiteList;

    private final GlobalDocReader globalDocReader;

    private final DocReader docReader;

    private final TypeScanner typeScanner;

    private final ApiTypeDocReader apiTypeDocReader;

    private final TemplateProvider templateProvider;

    private final TypeReferenceProvider typeReferenceProvider;

    /**
     * Creates a new {@link LivedocReader} with the given configuration.
     *
     * @param packageWhiteList
     *         the packages that should be scanned to find the classes to document. The given list will be used to find
     *         controllers and to filter the types to document.
     * @param typeScanner
     *         the {@link TypeScanner} to use to explore the types to document, starting from the types used in the API
     * @param globalDocReader
     *         the {@link GlobalDocReader} to use to generate the global documentation of a project (general info,
     *         flows, migrations)
     * @param apiTypeDocReader
     *         the {@link ApiTypeDocReader} to use to generate the documentation for the types used in the API
     * @param docReader
     *         the {@link DocReader} to use to create documentation objects using reflection. They are called in the
     *         given order, and the last reader's output has precedence over previous ones. This is why, by default, the
     *         {@link LivedocAnnotationDocReader} is last, so that Livedoc annotations override default framework
     *         documentations.
     * @param templateProvider
     *         the {@link TemplateProvider} to use to create example objects for the types used in the API
     */
    public LivedocReader(List<String> packageWhiteList, TypeScanner typeScanner, GlobalDocReader globalDocReader,
            ApiTypeDocReader apiTypeDocReader, DocReader docReader, TemplateProvider templateProvider) {
        this.packageWhiteList = packageWhiteList;
        this.typeScanner = typeScanner;
        this.apiTypeDocReader = apiTypeDocReader;
        this.globalDocReader = globalDocReader;
        this.docReader = docReader;
        this.templateProvider = templateProvider;
        this.typeReferenceProvider = new DefaultTypeReferenceProvider(this::isInWhiteListedPackage);
    }

    private boolean isInWhiteListedPackage(Type type) {
        if (!(type instanceof Class)) {
            return true;
        }
        if (TypePredicates.isPrimitiveLike(type)) {
            return false;
        }
        Class<?> clazz = (Class<?>) type;
        String packageName = clazz.getPackage().getName();
        return packageWhiteList.stream().anyMatch(packageName::startsWith);
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
        Livedoc livedoc = new Livedoc(version, basePath);
        livedoc.setPlaygroundEnabled(playgroundEnabled);
        livedoc.setDisplayMethodAs(displayMethodAs);

        Collection<Class<?>> controllers = docReader.findControllerTypes();
        Collection<ApiDoc> apiDocs = readApiDocs(controllers);
        Set<Class<?>> types = getClassesToDocument();

        livedoc.setApis(group(apiDocs));
        livedoc.setTypes(group(getApiTypeDocs(types)));

        Map<String, ApiOperationDoc> apiOperationDocsById = getAllApiOperationDocsById(apiDocs);
        livedoc.setFlows(group(globalDocReader.getApiFlowDocs(apiOperationDocsById)));
        livedoc.setGlobal(globalDocReader.getApiGlobalDoc());

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
        Set<Class<?>> exploredTypes = typeScanner.findTypesToDocument(rootTypes);
        return exploredTypes.stream().filter(this::isInWhiteListedPackage).collect(Collectors.toSet());
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
        return getAllMethods(controller).stream()
                                        .filter(m -> docReader.isApiOperation(m, controller))
                                        .collect(Collectors.toList());
    }

    private static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Method[] declaredMethods = currentClass.getDeclaredMethods();
            Collections.addAll(methods, declaredMethods);
            currentClass = currentClass.getSuperclass();
        }
        return methods;
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

    private List<ApiTypeDoc> getApiTypeDocs(Collection<Class<?>> types) {
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
