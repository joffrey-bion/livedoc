package org.hildan.livedoc.core.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiBodyObject;
import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.flow.ApiFlow;
import org.hildan.livedoc.core.annotation.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotation.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotation.global.ApiGlobal;
import org.hildan.livedoc.core.annotation.global.ApiMigrationSet;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.flow.ApiFlowDoc;
import org.hildan.livedoc.core.pojo.global.ApiGlobalDoc;
import org.hildan.livedoc.core.scanner.readers.ApiDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiGlobalDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiMethodDocReader;
import org.hildan.livedoc.core.scanner.templates.ObjectTemplate;
import org.hildan.livedoc.core.util.LivedocUtils;

public class LivedocAnnotationDocReader implements DocReader, GlobalDocReader {

    private final AnnotatedTypesFinder annotatedTypesFinder;

    public LivedocAnnotationDocReader(AnnotatedTypesFinder annotatedTypesFinder) {
        this.annotatedTypesFinder = annotatedTypesFinder;
    }

    @Override
    public Collection<Class<?>> findControllerTypes() {
        return annotatedTypesFinder.apply(Api.class);
    }

    @Override
    public ApiDoc buildApiDocBase(Class<?> controllerType) {
        return ApiDocReader.read(controllerType);
    }

    @Override
    public ApiMethodDoc buildApiMethodDoc(Method method, ApiDoc parentApiDoc, Map<Class<?>, ObjectTemplate> templates) {
        ApiMethodDoc apiMethodDoc = ApiMethodDocReader.read(method, parentApiDoc);

        if (method.isAnnotationPresent(ApiBodyObject.class)) {
            apiMethodDoc.getBodyobject()
                        .setTemplate(templates.get(method.getAnnotation(ApiBodyObject.class).clazz()));
        }
        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiBodyObject.class);
        if (index != -1) {
            apiMethodDoc.getBodyobject().setTemplate(templates.get(method.getParameterTypes()[index]));
        }

        return apiMethodDoc;
    }

    @Override
    public Collection<? extends Type> extractTypesToDocument(Method method) {
        // this default reader is not able to extract anything from methods
        // this will only be useful in specialized readers like the spring one
        return Collections.emptySet();
    }

    @Override
    public Collection<? extends Type> getAdditionalTypesToDocument() {
        return annotatedTypesFinder.apply(ApiObject.class);
    }

    /**
     * Gets the API flow documentation for the set of classes passed as argument
     */
    @Override
    public Set<ApiFlowDoc> getApiFlowDocs(Map<String, ? extends ApiMethodDoc> apiMethodDocsById) {
        Set<ApiFlowDoc> apiFlowDocs = new TreeSet<>();
        for (Class<?> clazz : getClassesWithFlows()) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(ApiFlow.class)) {
                    ApiFlowDoc apiFlowDoc = getApiFlowDoc(method, apiMethodDocsById);
                    apiFlowDocs.add(apiFlowDoc);
                }
            }
        }
        return apiFlowDocs;
    }

    private Iterable<Class<?>> getClassesWithFlows() {
        return annotatedTypesFinder.apply(ApiFlowSet.class);
    }

    private ApiFlowDoc getApiFlowDoc(Method method, Map<String, ? extends ApiMethodDoc> apiMethodDocsById) {
        return ApiFlowDoc.buildFromAnnotation(method.getAnnotation(ApiFlow.class), apiMethodDocsById);
    }

    @Override
    public ApiGlobalDoc getApiGlobalDoc() {
        Collection<Class<?>> global = getClassesWithGlobalDoc();
        Collection<Class<?>> changelogs = getClassesWithChangelogs();
        Collection<Class<?>> migrations = getClassesWithMigrations();
        return ApiGlobalDocReader.read(global, changelogs, migrations);
    }

    private Collection<Class<?>> getClassesWithGlobalDoc() {
        return annotatedTypesFinder.apply(ApiGlobal.class);
    }

    private Collection<Class<?>> getClassesWithChangelogs() {
        return annotatedTypesFinder.apply(ApiChangelogSet.class);
    }

    private Collection<Class<?>> getClassesWithMigrations() {
        return annotatedTypesFinder.apply(ApiMigrationSet.class);
    }
}
