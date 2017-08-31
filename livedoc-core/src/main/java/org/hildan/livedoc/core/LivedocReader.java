package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.builders.doc.ApiObjectDocReader;
import org.hildan.livedoc.core.builders.templates.ObjectTemplate;
import org.hildan.livedoc.core.builders.templates.ObjectTemplateBuilder;
import org.hildan.livedoc.core.builders.validators.ApiMethodDocValidator;
import org.hildan.livedoc.core.builders.validators.ApiObjectDocValidator;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.LivedocUtils;

public class LivedocReader {

    private final List<String> packageWhiteList;

    private final GlobalDocReader globalDocReader;

    private final List<DocReader> docReaders;

    private final TypeScanner typeScanner;

    private final ApiObjectDocReader apiObjectDocReader;

    public LivedocReader(List<String> packageWhiteList, TypeScanner typeScanner, GlobalDocReader globalDocReader,
            ApiObjectDocReader apiObjectDocReader, List<DocReader> readers) {
        this.packageWhiteList = packageWhiteList;
        this.typeScanner = typeScanner;
        this.apiObjectDocReader = apiObjectDocReader;
        this.globalDocReader = globalDocReader;
        this.docReaders = readers;
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
        livedoc.setApis(LivedocUtils.group(apiDocs));
        livedoc.setObjects(LivedocUtils.group(getApiObjectDocs(types)));

        Map<String, ApiMethodDoc> apiMethodDocsById = getAllApiMethodDocsById(apiDocs);
        livedoc.setFlows(LivedocUtils.group(globalDocReader.getApiFlowDocs(apiMethodDocsById)));
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

    public ApiDoc readApiDoc(Class<?> controller, MethodDisplay displayMethodAs,
            Map<Class<?>, ObjectTemplate> templates) {
        ApiDoc doc = readFromAllReadersAndMerge(r -> r.buildApiDocBase(controller));
        if (doc != null) {
            doc.setMethods(readApiMethodDocs(controller, doc, displayMethodAs, templates));
        }
        return doc;
    }

    private List<ApiMethodDoc> readApiMethodDocs(Class<?> controller, ApiDoc doc, MethodDisplay displayMethodAs,
            Map<Class<?>, ObjectTemplate> templates) {
        // TODO add support for inherited controller methods
        Method[] methods = controller.getDeclaredMethods();
        return buildDocs(Arrays.asList(methods), m -> {
            ApiMethodDoc apiMethodDoc = readApiMethodDoc(m, doc, templates);
            apiMethodDoc.setDisplayMethodAs(displayMethodAs);
            ApiMethodDocValidator.validate(apiMethodDoc);
            return apiMethodDoc;
        });
    }

    private ApiMethodDoc readApiMethodDoc(Method method, ApiDoc parentApiDoc, Map<Class<?>, ObjectTemplate> templates) {
        return readFromAllReadersAndMerge(r -> r.buildApiMethodDoc(method, parentApiDoc, templates));
    }

    private List<ApiObjectDoc> getApiObjectDocs(Collection<Class<?>> types) {
        return buildDocs(types, this::readApiObjectDoc);
    }

    private ApiObjectDoc readApiObjectDoc(Class<?> type) {
        ApiObjectDoc doc = apiObjectDocReader.read(type);
        ApiObjectDocValidator.validate(doc);
        return doc;
    }

    private static <T, D> List<D> buildDocs(Collection<T> objects, Function<T, D> read) {
        return objects.stream().map(read).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private <D> D readFromAllReadersAndMerge(Function<DocReader, D> buildDoc) {
        D doc = null;
        for (DocReader reader : docReaders) {
            D newDoc = buildDoc.apply(reader);
            if (doc == null) {
                doc = newDoc;
            } else if (newDoc != null) {
                BeanUtils.copyNonNullFields(newDoc, doc);
            }
        }
        return doc;
    }
}
