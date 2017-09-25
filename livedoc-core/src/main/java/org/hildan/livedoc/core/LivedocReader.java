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
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Groupable;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.TypeScanner;

public class LivedocReader {

    private final List<String> packageWhiteList;

    private final GlobalDocReader globalDocReader;

    private final List<DocReader> docReaders;

    private final TypeScanner typeScanner;

    private final ApiObjectDocReader apiObjectDocReader;

    private final TemplateProvider templateProvider;

    private final DocMerger docMerger;

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
        if (apiMethodDoc.getBodyobject() != null) {
            types.addAll(apiMethodDoc.getBodyobject().getType().getComposingTypes());
        }
        if (apiMethodDoc.getResponse() != null) {
            types.addAll(apiMethodDoc.getResponse().getType().getComposingTypes());
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
        return buildDocs(getAllMethods(controller), m -> readApiMethodDoc(m, doc, displayMethodAs));
    }

    private static List<Method> getAllMethods(final Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Method[] declaredMethods = currentClass.getDeclaredMethods();
            Collections.addAll(methods, declaredMethods);
            currentClass = currentClass.getSuperclass();
        }
        return methods;
    }

    private Optional<ApiMethodDoc> readApiMethodDoc(Method method, ApiDoc parentApiDoc, MethodDisplay displayMethodAs) {
        Optional<ApiMethodDoc> doc = readFromAllReadersAndMerge(
                r -> r.buildApiMethodDoc(method, parentApiDoc, templateProvider));
        doc.ifPresent(apiMethodDoc -> {
            apiMethodDoc.setDisplayMethodAs(displayMethodAs);
            ApiMethodDocDefaults.complete(apiMethodDoc);
            ApiMethodDocValidator.validate(apiMethodDoc);
        });
        return doc;
    }

    private List<ApiObjectDoc> getApiObjectDocs(Collection<Class<?>> types) {
        return buildDocs(types, this::readApiObjectDoc);
    }

    private Optional<ApiObjectDoc> readApiObjectDoc(Class<?> type) {
        ApiObjectDoc doc = apiObjectDocReader.read(type);
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
