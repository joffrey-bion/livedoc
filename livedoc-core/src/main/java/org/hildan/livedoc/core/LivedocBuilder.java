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

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.AnnotatedTypesFinder;
import org.hildan.livedoc.core.scanner.DocReader;
import org.hildan.livedoc.core.scanner.GlobalDocReader;
import org.hildan.livedoc.core.scanner.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.scanner.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;
import org.hildan.livedoc.core.scanner.readers.ApiObjectDocReader;
import org.hildan.livedoc.core.scanner.templates.ObjectTemplate;
import org.hildan.livedoc.core.scanner.templates.ObjectTemplateBuilder;
import org.hildan.livedoc.core.scanner.types.RecursivePropertyTypesScanner;
import org.hildan.livedoc.core.scanner.types.TypesScanner;
import org.hildan.livedoc.core.scanner.types.mappers.ConcreteTypesMapper;
import org.hildan.livedoc.core.scanner.validators.ApiMethodDocValidator;
import org.hildan.livedoc.core.scanner.validators.ApiObjectDocValidator;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.reflections.Reflections;

public class LivedocBuilder {

    private final GlobalDocReader globalDocReader;

    private final List<DocReader> docReaders;

    private TypesScanner typesScanner;

    private ApiObjectDocReader apiObjectDocReader;

    public LivedocBuilder(PropertyScanner propertyScanner, GlobalDocReader globalDocReader, DocReader... readers) {
        this.typesScanner = new RecursivePropertyTypesScanner(propertyScanner);
        this.apiObjectDocReader = new ApiObjectDocReader(propertyScanner);
        this.globalDocReader = globalDocReader;
        this.docReaders = Arrays.asList(readers);
    }

    public static LivedocBuilder basicAnnotationBuilder(List<String> packages) {
        Reflections reflections = LivedocUtils.newReflections(packages);
        AnnotatedTypesFinder annotatedTypesFinder = LivedocUtils.createAnnotatedTypesFinder(reflections);

        PropertyScanner propertyScanner = new FieldPropertyScanner();
        LivedocAnnotationDocReader docReader = new LivedocAnnotationDocReader(annotatedTypesFinder);
        RecursivePropertyTypesScanner typesScanner = new RecursivePropertyTypesScanner(propertyScanner);
        typesScanner.setMapper(new ConcreteTypesMapper(reflections));

        LivedocBuilder builder = new LivedocBuilder(propertyScanner, docReader, docReader);
        builder.setTypesScanner(typesScanner);
        return builder;
    }

    public TypesScanner getTypesScanner() {
        return typesScanner;
    }

    public void setTypesScanner(TypesScanner typesScanner) {
        this.typesScanner = typesScanner;
    }

    public ApiObjectDocReader getApiObjectDocReader() {
        return apiObjectDocReader;
    }

    public void setApiObjectDocReader(ApiObjectDocReader apiObjectDocReader) {
        this.apiObjectDocReader = apiObjectDocReader;
    }

    /**
     * Returns the main <code>ApiDoc</code>, containing <code>ApiMethodDoc</code> and <code>ApiObjectDoc</code> objects
     *
     * @return An <code>ApiDoc</code> object
     */
    public Livedoc build(String version, String basePath, List<String> packages, boolean playgroundEnabled,
            MethodDisplay displayMethodAs) {
        Livedoc livedoc = new Livedoc(version, basePath);
        livedoc.setPlaygroundEnabled(playgroundEnabled);
        livedoc.setDisplayMethodAs(displayMethodAs);

        Set<Class<?>> controllers = findControllers();
        Set<Class<?>> types = getClassesToDocument(controllers, packages);

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

    private Set<Class<?>> getClassesToDocument(Set<Class<?>> controllers, List<String> packages) {
        Set<Type> rootTypes = getRootTypesToDocument(controllers);
        Set<Class<?>> exploredTypes = typesScanner.findTypes(rootTypes);
        return exploredTypes.stream().filter(c -> isInWhiteListedPackage(c, packages)).collect(Collectors.toSet());
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

    private boolean isInWhiteListedPackage(Class<?> clazz, List<String> packages) {
        String packageName = clazz.getPackage().getName();
        return packages.stream().anyMatch(packageName::startsWith);
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
