package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.builders.defaults.ApiMethodDocDefaults;
import org.hildan.livedoc.core.builders.doc.ApiObjectDocReader;
import org.hildan.livedoc.core.builders.merger.DocMerger;
import org.hildan.livedoc.core.builders.validators.ApiMethodDocValidator;
import org.hildan.livedoc.core.builders.validators.ApiObjectDocValidator;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.Groupable;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.TypeScanner;

/**
 * A component able to create an API documentation by inspecting classes and reading their annotations.
 */
public class LivedocReader {

    private final List<String> packageWhiteList;

    private final GlobalDocReader globalDocReader;

    private final List<DocReader> docReaders;

    private final TypeScanner typeScanner;

    private final ApiObjectDocReader apiObjectDocReader;

    private final TemplateProvider templateProvider;

    private final DocMerger docMerger;

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
     * @param apiObjectDocReader
     *         the {@link ApiObjectDocReader} to use to generate the documentation for the types used in the API
     * @param readers
     *         the {@link DocReader}s to use to create documentation objects using reflection. They are called in the
     *         given order, and the last reader's output has precedence over previous ones. This is why, by default, the
     *         {@link LivedocAnnotationDocReader} is last, so that Livedoc annotations override default framework
     *         documentations.
     * @param templateProvider
     *         the {@link TemplateProvider} to use to create example objects for the types used in the API
     */
    public LivedocReader(List<String> packageWhiteList, TypeScanner typeScanner, GlobalDocReader globalDocReader,
            ApiObjectDocReader apiObjectDocReader, List<DocReader> readers, TemplateProvider templateProvider) {
        this.packageWhiteList = packageWhiteList;
        this.typeScanner = typeScanner;
        this.apiObjectDocReader = apiObjectDocReader;
        this.globalDocReader = globalDocReader;
        this.docReaders = readers;
        this.templateProvider = templateProvider;
        this.docMerger = new DocMerger(new FieldPropertyScanner());
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

        Set<Class<?>> controllers = findControllers();
        Collection<ApiDoc> apiDocs = readApiDocs(controllers, displayMethodAs);
        Set<Class<?>> types = getClassesToDocument(apiDocs);

        livedoc.setApis(group(apiDocs));
        livedoc.setObjects(group(getApiObjectDocs(types)));

        Map<String, ApiMethodDoc> apiMethodDocsById = getAllApiMethodDocsById(apiDocs);
        livedoc.setFlows(group(globalDocReader.getApiFlowDocs(apiMethodDocsById)));
        livedoc.setGlobal(globalDocReader.getApiGlobalDoc());

        return livedoc;
    }

    private Map<String, ApiMethodDoc> getAllApiMethodDocsById(Collection<ApiDoc> apiDocs) {
        return apiDocs.stream()
                      .flatMap(api -> api.getMethods().stream())
                      .filter(m -> m.getId() != null && !m.getId().isEmpty())
                      .collect(Collectors.toMap(ApiMethodDoc::getId, x -> x));
    }

    private Set<Class<?>> findControllers() {
        Set<Class<?>> controllers = new HashSet<>();
        for (DocReader reader : docReaders) {
            controllers.addAll(reader.findControllerTypes());
        }
        return controllers;
    }

    private Set<Class<?>> getClassesToDocument(Collection<ApiDoc> apiDocs) {
        Set<Type> rootTypes = getReferencedTypesToDocument(apiDocs);
        Set<Class<?>> exploredTypes = typeScanner.findTypes(rootTypes);
        return exploredTypes.stream().filter(this::isInWhiteListedPackage).collect(Collectors.toSet());
    }

    private static Set<Type> getReferencedTypesToDocument(Collection<ApiDoc> apiDocs) {
        Set<Type> types = new HashSet<>();
        for (ApiDoc apiDoc : apiDocs) {
            for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
                types.addAll(getReferencedTypes(apiMethodDoc));
            }
        }
        return types;
    }

    private static Set<Type> getReferencedTypes(ApiMethodDoc apiMethodDoc) {
        Set<Type> types = new HashSet<>();
        if (apiMethodDoc.getRequestBody() != null) {
            types.addAll(apiMethodDoc.getRequestBody().getType().getComposingTypes());
        }
        if (apiMethodDoc.getResponseBodyType() != null) {
            types.addAll(apiMethodDoc.getResponseBodyType().getComposingTypes());
        }
        return types;
    }

    private boolean isInWhiteListedPackage(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        return packageWhiteList.stream().anyMatch(packageName::startsWith);
    }

    private Collection<ApiDoc> readApiDocs(Collection<Class<?>> controllers, MethodDisplay displayMethodAs) {
        return buildDocs(controllers, c -> readApiDoc(c, displayMethodAs));
    }

    public Optional<ApiDoc> readApiDoc(Class<?> controller, MethodDisplay displayMethodAs) {
        Optional<ApiDoc> doc = readFromAllReadersAndMerge(r -> r.buildApiDocBase(controller));
        doc.ifPresent(apiDoc -> apiDoc.setMethods(readApiMethodDocs(controller, apiDoc, displayMethodAs)));
        return doc;
    }

    private List<ApiMethodDoc> readApiMethodDocs(Class<?> controller, ApiDoc doc, MethodDisplay displayMethodAs) {
        return buildDocs(getAllMethods(controller), m -> readApiMethodDoc(m, controller, doc, displayMethodAs));
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

    private Optional<ApiMethodDoc> readApiMethodDoc(Method method, Class<?> controller, ApiDoc parentApiDoc,
            MethodDisplay displayMethodAs) {
        Optional<ApiMethodDoc> doc = readFromAllReadersAndMerge(
                r -> r.buildApiMethodDoc(method, controller, parentApiDoc, templateProvider));
        doc.ifPresent(apiMethodDoc -> {
            apiMethodDoc.setDisplayMethodAs(displayMethodAs);
            ApiMethodDocDefaults.complete(apiMethodDoc);
            ApiMethodDocValidator.validate(apiMethodDoc);
        });
        return doc;
    }

    private List<ApiTypeDoc> getApiObjectDocs(Collection<Class<?>> types) {
        return buildDocs(types, this::readApiObjectDoc);
    }

    private Optional<ApiTypeDoc> readApiObjectDoc(Class<?> type) {
        ApiTypeDoc doc = apiObjectDocReader.read(type);
        doc.setTemplate(templateProvider.getTemplate(type));
        ApiObjectDocValidator.validate(doc);
        return Optional.of(doc);
    }

    private static <T, D> List<D> buildDocs(Collection<T> objects, Function<T, Optional<D>> read) {
        return objects.stream().map(read).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    private <D> Optional<D> readFromAllReadersAndMerge(Function<DocReader, Optional<D>> buildDoc) {
        D doc = null;
        for (DocReader reader : docReaders) {
            Optional<D> newDoc = buildDoc.apply(reader);
            if (newDoc.isPresent()) {
                if (doc == null) {
                    doc = newDoc.get();
                } else {
                    docMerger.merge(newDoc.get(), doc);
                }
            }
        }
        return Optional.ofNullable(doc);
    }

    private static <T extends Groupable> Map<String, Set<T>> group(Iterable<T> elements) {
        Map<String, Set<T>> groupedElements = new TreeMap<>();
        for (T e : elements) {
            String groupName = e.getGroup();
            groupedElements.putIfAbsent(groupName, new TreeSet<>());
            Set<T> group = groupedElements.get(groupName);
            group.add(e);
        }
        return groupedElements;
    }
}
