package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.builders.doc.ApiObjectDocReader;
import org.hildan.livedoc.core.builders.merger.DocMerger;
import org.hildan.livedoc.core.builders.templates.ObjectTemplate;
import org.hildan.livedoc.core.builders.templates.ObjectTemplateBuilder;
import org.hildan.livedoc.core.builders.validators.ApiMethodDocValidator;
import org.hildan.livedoc.core.builders.validators.ApiObjectDocValidator;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Groupable;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.types.TypeScanner;

public class LivedocReader {

    private final List<String> packageWhiteList;

    private final GlobalDocReader globalDocReader;

    private final List<DocReader> docReaders;

    private final TypeScanner typeScanner;

    private final ApiObjectDocReader apiObjectDocReader;

    private final DocMerger docMerger;

    public LivedocReader(List<String> packageWhiteList, TypeScanner typeScanner, GlobalDocReader globalDocReader,
            ApiObjectDocReader apiObjectDocReader, List<DocReader> readers) {
        this.packageWhiteList = packageWhiteList;
        this.typeScanner = typeScanner;
        this.apiObjectDocReader = apiObjectDocReader;
        this.globalDocReader = globalDocReader;
        this.docReaders = readers;
        this.docMerger = new DocMerger(new FieldPropertyScanner());
    }

    public static LivedocReader basicAnnotationBuilder(List<String> packages) {
        return new LivedocReaderBuilder().scanningPackages(packages).build();
    }

    /**
     * Returns the main <code>ApiDoc</code>, containing <code>ApiMethodDoc</code> and <code>ApiObjectDoc</code> objects
     *
     * @return An <code>ApiDoc</code> object
     */
    public Livedoc read(String version, String basePath, boolean playgroundEnabled, MethodDisplay displayMethodAs) {
        Livedoc livedoc = new Livedoc(version, basePath);
        livedoc.setPlaygroundEnabled(playgroundEnabled);
        livedoc.setDisplayMethodAs(displayMethodAs);

        Set<Class<?>> controllers = findControllers();
        Set<Class<?>> types = getClassesToDocument(controllers);

        Map<Class<?>, ObjectTemplate> templates = new HashMap<>();
        for (Class<?> clazz : types) {
            templates.put(clazz, ObjectTemplateBuilder.build(clazz, types));
        }

        Collection<ApiDoc> apiDocs = readApiDocs(controllers, displayMethodAs, templates);
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

    private Set<Class<?>> getClassesToDocument(Set<Class<?>> controllers) {
        Set<Type> rootTypes = getRootTypesToDocument(controllers);
        Set<Class<?>> exploredTypes = typeScanner.findTypes(rootTypes);
        return exploredTypes.stream().filter(this::isInWhiteListedPackage).collect(Collectors.toSet());
    }

    private Set<Type> getRootTypesToDocument(Set<Class<?>> controllers) {
        Collection<Method> methods = getAllMethods(controllers);
        Set<Type> rootTypes = getReferencedTypesToDocument(methods);
        rootTypes.addAll(getOtherTypesToDocument());
        return rootTypes;
    }

    private Collection<Method> getAllMethods(Set<Class<?>> controllers) {
        return controllers.stream().flatMap(c -> Arrays.stream(c.getMethods())).collect(Collectors.toSet());
    }

    // TODO get these types from already built docs, which makes more sense
    // (this refac requires to deal with templates in a second phase)
    private Set<Type> getReferencedTypesToDocument(Collection<Method> methods) {
        Set<Type> types = new HashSet<>();
        for (Method method : methods) {
            for (DocReader reader : docReaders) {
                types.addAll(reader.extractTypesToDocument(method));
            }
        }
        return types;
    }

    private Set<Type> getOtherTypesToDocument() {
        Set<Type> types = new HashSet<>();
        for (DocReader reader : docReaders) {
            types.addAll(reader.getAdditionalTypesToDocument());
        }
        return types;
    }

    private boolean isInWhiteListedPackage(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        return packageWhiteList.stream().anyMatch(packageName::startsWith);
    }

    private Collection<ApiDoc> readApiDocs(Collection<Class<?>> controllers, MethodDisplay displayMethodAs,
            Map<Class<?>, ObjectTemplate> templates) {
        return buildDocs(controllers, c -> readApiDoc(c, displayMethodAs, templates));
    }

    public Optional<ApiDoc> readApiDoc(Class<?> controller, MethodDisplay displayMethodAs,
            Map<Class<?>, ObjectTemplate> templates) {
        Optional<ApiDoc> doc = readFromAllReadersAndMerge(r -> r.buildApiDocBase(controller));
        doc.ifPresent(apiDoc -> apiDoc.setMethods(readApiMethodDocs(controller, apiDoc, displayMethodAs, templates)));
        return doc;
    }

    private List<ApiMethodDoc> readApiMethodDocs(Class<?> controller, ApiDoc doc, MethodDisplay displayMethodAs,
            Map<Class<?>, ObjectTemplate> templates) {
        return buildDocs(getAllMethods(controller), m -> readApiMethodDoc(m, doc, displayMethodAs, templates));
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

    private Optional<ApiMethodDoc> readApiMethodDoc(Method method, ApiDoc parentApiDoc, MethodDisplay displayMethodAs,
            Map<Class<?>, ObjectTemplate> templates) {
        Optional<ApiMethodDoc> doc = readFromAllReadersAndMerge(r -> r.buildApiMethodDoc(method, parentApiDoc, templates));
        doc.ifPresent(apiMethodDoc -> {
            apiMethodDoc.setDisplayMethodAs(displayMethodAs);
            ApiMethodDocValidator.validate(apiMethodDoc);
        });
        return doc;
    }

    private List<ApiObjectDoc> getApiObjectDocs(Collection<Class<?>> types) {
        return buildDocs(types, this::readApiObjectDoc);
    }

    private Optional<ApiObjectDoc> readApiObjectDoc(Class<?> type) {
        ApiObjectDoc doc = apiObjectDocReader.read(type);
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
