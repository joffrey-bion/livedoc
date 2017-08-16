package org.hildan.livedoc.core.scanner;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.flow.ApiFlow;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.Groupable;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.pojo.LivedocTemplate;
import org.hildan.livedoc.core.pojo.flow.ApiFlowDoc;
import org.hildan.livedoc.core.pojo.global.ApiGlobalDoc;
import org.hildan.livedoc.core.scanner.readers.ApiAuthDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiErrorDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiGlobalDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiVersionDocReader;
import org.hildan.livedoc.core.scanner.validators.ApiMethodDocValidator;
import org.hildan.livedoc.core.scanner.validators.ApiObjectDocValidator;
import org.hildan.livedoc.core.util.LivedocTemplateBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDocAnnotationScanner implements DocAnnotationScanner {

    protected Reflections reflections = null;

    private static Logger log = LoggerFactory.getLogger(DocAnnotationScanner.class);

    public abstract Set<Class<?>> jsondocControllers();

    public abstract Set<Method> jsondocMethods(Class<?> controller);

    public abstract Set<Class<?>> jsondocObjects(List<String> packages);

    public abstract Set<Class<?>> jsondocFlows();

    public abstract Set<Class<?>> jsondocGlobal();

    public abstract Set<Class<?>> jsondocChangelogs();

    public abstract Set<Class<?>> jsondocMigrations();

    public abstract ApiDoc initApiDoc(Class<?> controller);

    public abstract ApiDoc mergeApiDoc(Class<?> controller, ApiDoc apiDoc);

    public abstract ApiMethodDoc initApiMethodDoc(Method method, Map<Class<?>, LivedocTemplate> jsondocTemplates);

    public abstract ApiMethodDoc mergeApiMethodDoc(Method method, ApiMethodDoc apiMethodDoc);

    public abstract ApiObjectDoc initApiObjectDoc(Class<?> clazz);

    public abstract ApiObjectDoc mergeApiObjectDoc(Class<?> clazz, ApiObjectDoc apiObjectDoc);

    private List<ApiMethodDoc> allApiMethodDocs = new ArrayList<>();

    private Map<Class<?>, LivedocTemplate> jsondocTemplates = new HashMap<>();

    /**
     * Returns the main <code>ApiDoc</code>, containing <code>ApiMethodDoc</code> and <code>ApiObjectDoc</code> objects
     *
     * @return An <code>ApiDoc</code> object
     */
    @Override
    public Livedoc getLivedoc(String version, String basePath, List<String> packages, boolean playgroundEnabled,
            MethodDisplay displayMethodAs) {
        reflections = newReflections(packages);

        Livedoc livedoc = new Livedoc(version, basePath);
        livedoc.setPlaygroundEnabled(playgroundEnabled);
        livedoc.setDisplayMethodAs(displayMethodAs);

        Set<Class<?>> jsondocControllers = jsondocControllers();
        Set<Class<?>> jsondocObjects = jsondocObjects(packages);
        Set<Class<?>> jsondocFlows = jsondocFlows();
        Set<Class<?>> jsondocGlobal = jsondocGlobal();
        Set<Class<?>> jsondocChangelogs = jsondocChangelogs();
        Set<Class<?>> jsondocMigrations = jsondocMigrations();

        for (Class<?> clazz : jsondocObjects) {
            jsondocTemplates.put(clazz, LivedocTemplateBuilder.build(clazz, jsondocObjects));
        }

        livedoc.setApis(group(getApiDocs(jsondocControllers, displayMethodAs)));
        livedoc.setObjects(group(getApiObjectDocs(jsondocObjects)));
        livedoc.setFlows(group(getApiFlowDocs(jsondocFlows, allApiMethodDocs)));
        livedoc.setGlobal(getApiGlobalDoc(jsondocGlobal, jsondocChangelogs, jsondocMigrations));

        return livedoc;
    }

    private static Reflections newReflections(List<String> packages) {
        Set<URL> urls = new HashSet<>();
        FilterBuilder filter = new FilterBuilder();

        log.debug("Found " + packages.size() + " package(s) to scan...");
        for (String pkg : packages) {
            log.debug("Adding package to Livedoc recursive scan: " + pkg);
            urls.addAll(ClasspathHelper.forPackage(pkg));
            filter.includePackage(pkg);
        }

        return new Reflections(new ConfigurationBuilder().filterInputsBy(filter)
                                                         .setUrls(urls)
                                                         .addScanners(new MethodAnnotationsScanner()));
    }

    @Override
    public ApiGlobalDoc getApiGlobalDoc(Set<Class<?>> global, Set<Class<?>> changelogs, Set<Class<?>> migrations) {
        return ApiGlobalDocReader.read(global, changelogs, migrations);
    }

    /**
     * Gets the API documentation for the set of classes passed as argument
     */
    @Override
    public Set<ApiDoc> getApiDocs(Set<Class<?>> classes, MethodDisplay displayMethodAs) {
        Set<ApiDoc> apiDocs = new TreeSet<>();
        for (Class<?> controller : classes) {
            ApiDoc apiDoc = getApiDoc(controller, displayMethodAs);
            apiDocs.add(apiDoc);
        }
        return apiDocs;
    }

    /**
     * Gets the API documentation for a single class annotated with @Api and for its methods, annotated with @ApiMethod
     *
     * @param controller
     *
     * @return
     */
    private ApiDoc getApiDoc(Class<?> controller, MethodDisplay displayMethodAs) {
        log.debug("Getting doc for class: " + controller.getName());
        ApiDoc apiDoc = initApiDoc(controller);

        apiDoc.setSupportedversions(ApiVersionDocReader.read(controller));
        apiDoc.setAuth(ApiAuthDocReader.read(controller));
        apiDoc.setMethods(getApiMethodDocs(controller, displayMethodAs));

        if (controller.isAnnotationPresent(Api.class)) {
            apiDoc = mergeApiDoc(controller, apiDoc);
        }

        return apiDoc;
    }

    private Set<ApiMethodDoc> getApiMethodDocs(Class<?> controller, MethodDisplay displayMethodAs) {
        Set<ApiMethodDoc> apiMethodDocs = new TreeSet<>();
        Set<Method> methods = jsondocMethods(controller);
        for (Method method : methods) {
            ApiMethodDoc apiMethodDoc = getApiMethodDoc(method, displayMethodAs);
            apiMethodDocs.add(apiMethodDoc);
        }
        allApiMethodDocs.addAll(apiMethodDocs);
        return apiMethodDocs;
    }

    private ApiMethodDoc getApiMethodDoc(Method method, MethodDisplay displayMethodAs) {
        ApiMethodDoc apiMethodDoc = initApiMethodDoc(method, jsondocTemplates);

        apiMethodDoc.setDisplayMethodAs(displayMethodAs);
        apiMethodDoc.setApierrors(ApiErrorDocReader.build(method));
        apiMethodDoc.setSupportedversions(ApiVersionDocReader.read(method));
        apiMethodDoc.setAuth(ApiAuthDocReader.read(method));

        apiMethodDoc = mergeApiMethodDoc(method, apiMethodDoc);
        apiMethodDoc = ApiMethodDocValidator.validate(apiMethodDoc, displayMethodAs);

        return apiMethodDoc;
    }

    /**
     * Gets the API flow documentation for the set of classes passed as argument
     */
    @Override
    public Set<ApiFlowDoc> getApiFlowDocs(Set<Class<?>> classes, List<ApiMethodDoc> apiMethodDocs) {
        Set<ApiFlowDoc> apiFlowDocs = new TreeSet<>();
        for (Class<?> clazz : classes) {
            log.debug("Getting flow doc for class: " + clazz.getName());
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(ApiFlow.class)) {
                    ApiFlowDoc apiFlowDoc = getApiFlowDoc(method, apiMethodDocs);
                    apiFlowDocs.add(apiFlowDoc);
                }
            }
        }
        return apiFlowDocs;
    }

    private ApiFlowDoc getApiFlowDoc(Method method, List<ApiMethodDoc> apiMethodDocs) {
        return ApiFlowDoc.buildFromAnnotation(method.getAnnotation(ApiFlow.class), apiMethodDocs);
    }

    @Override
    public Set<ApiObjectDoc> getApiObjectDocs(Set<Class<?>> classes) {
        Set<ApiObjectDoc> apiObjectDocs = new TreeSet<>();

        for (Class<?> clazz : classes) {
            log.debug("Getting object doc for class: " + clazz.getName());
            ApiObjectDoc apiObjectDoc = initApiObjectDoc(clazz);

            apiObjectDoc.setSupportedversions(ApiVersionDocReader.read(clazz));

            apiObjectDoc = mergeApiObjectDoc(clazz, apiObjectDoc);
            if (apiObjectDoc.isShow()) {
                apiObjectDoc = ApiObjectDocValidator.validate(apiObjectDoc);
                apiObjectDocs.add(apiObjectDoc);
            }

            apiObjectDoc.setJsondocTemplate(jsondocTemplates.get(clazz));
        }

        return apiObjectDocs;
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

    public static String[] enumConstantsToStringArray(Object[] enumConstants) {
        String[] sarr = new String[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            sarr[i] = String.valueOf(enumConstants[i]);
        }
        return sarr;
    }

}
