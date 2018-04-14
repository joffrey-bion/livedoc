package org.hildan.livedoc.core.readers.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link GlobalDocReader} that reads Livedoc annotations to build the documentation.
 */
public class LivedocAnnotationGlobalDocReader implements GlobalDocReader {

    private static final Logger logger = LoggerFactory.getLogger(LivedocAnnotationGlobalDocReader.class);

    private final AnnotatedTypesFinder annotatedTypesFinder;

    public LivedocAnnotationGlobalDocReader(AnnotatedTypesFinder annotatedTypesFinder) {
        this.annotatedTypesFinder = annotatedTypesFinder;
    }

    @NotNull
    @Override
    public ApiGlobalDoc getApiGlobalDoc(LivedocConfiguration configuration, GlobalTemplateData globalTemplateData) {
        Class<?> globalDocClass = findOneClass(ApiGlobal.class);
        if (globalDocClass == null) {
            return ApiGlobalDocReader.readDefault(globalTemplateData);
        }
        return ApiGlobalDocReader.read(configuration, globalTemplateData, globalDocClass);
    }

    @Nullable
    private <A extends Annotation> Class<?> findOneClass(Class<A> annotationClass) {
        Collection<Class<?>> classes = annotatedTypesFinder.apply(annotationClass);
        if (classes.isEmpty()) {
            return null;
        }
        if (classes.size() > 1) {
            logger.warn("Multiple classes annotated {} were found, only one such class is supported",
                    annotationClass.getSimpleName());
        }
        return classes.iterator().next();
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
