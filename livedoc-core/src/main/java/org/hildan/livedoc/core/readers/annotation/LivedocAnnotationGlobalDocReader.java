package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.GlobalDocReader;
import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.readers.annotation.ApiGlobalDocReader;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;

/**
 * An implementation of {@link GlobalDocReader} that reads Livedoc annotations to build the documentation.
 */
public class LivedocAnnotationGlobalDocReader implements GlobalDocReader {

    private final AnnotatedTypesFinder annotatedTypesFinder;

    public LivedocAnnotationGlobalDocReader(AnnotatedTypesFinder annotatedTypesFinder) {
        this.annotatedTypesFinder = annotatedTypesFinder;
    }

    /**
     * Gets the API flow documentation for the set of classes passed as argument
     */
    @Override
    public Set<ApiFlowDoc> getApiFlowDocs(Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        Set<ApiFlowDoc> apiFlowDocs = new TreeSet<>();
        for (Class<?> clazz : getClassesWithFlows()) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                ApiFlow flowAnnotation = method.getAnnotation(ApiFlow.class);
                if (flowAnnotation != null) {
                    apiFlowDocs.add(getApiFlowDoc(apiOperationDocsById, flowAnnotation));
                }
            }
        }
        return apiFlowDocs;
    }

    private Iterable<Class<?>> getClassesWithFlows() {
        return annotatedTypesFinder.apply(ApiFlowSet.class);
    }

    private ApiFlowDoc getApiFlowDoc(Map<String, ? extends ApiOperationDoc> apiOperationDocsById,
            ApiFlow flowAnnotation) {
        return ApiFlowDoc.buildFromAnnotation(flowAnnotation, apiOperationDocsById);
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
