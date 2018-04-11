package org.hildan.livedoc.core.readers.annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link GlobalDocReader} that reads Livedoc annotations to build the documentation.
 */
public class LivedocAnnotationGlobalDocReader implements GlobalDocReader {

    private final AnnotatedTypesFinder annotatedTypesFinder;

    public LivedocAnnotationGlobalDocReader(AnnotatedTypesFinder annotatedTypesFinder) {
        this.annotatedTypesFinder = annotatedTypesFinder;
    }

    @NotNull
    @Override
    public ApiGlobalDoc getApiGlobalDoc(ApiMetaData apiInfo, LivedocMetaData livedocInfo, LivedocConfiguration config) {
        Collection<Class<?>> global = annotatedTypesFinder.apply(ApiGlobal.class);
        Collection<Class<?>> changelogs = annotatedTypesFinder.apply(ApiChangelogSet.class);
        Collection<Class<?>> migrations = annotatedTypesFinder.apply(ApiMigrationSet.class);
        return new ApiGlobalDocReader(apiInfo, livedocInfo, config).read(global, changelogs, migrations);
    }

    @NotNull
    @Override
    public Set<ApiFlowDoc> getApiFlowDocs(Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        Collection<Class<?>> classesWithFlows = annotatedTypesFinder.apply(ApiFlowSet.class);
        return classesWithFlows.stream()
                               .map(Class::getMethods)
                               .flatMap(Arrays::stream)
                               .map(method -> method.getAnnotation(ApiFlow.class))
                               .filter(Objects::nonNull)
                               .map(flowAnnotation -> getApiFlowDoc(apiOperationDocsById, flowAnnotation))
                               .collect(Collectors.toCollection(TreeSet::new));
    }

    private ApiFlowDoc getApiFlowDoc(Map<String, ? extends ApiOperationDoc> apiOperationDocsById,
            ApiFlow flowAnnotation) {
        return ApiFlowDoc.buildFromAnnotation(flowAnnotation, apiOperationDocsById);
    }

}
