package org.hildan.livedoc.core.readers.annotation;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPages;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.global.GlobalDoc;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
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
    public GlobalDoc getApiGlobalDoc(LivedocConfiguration configuration, GlobalTemplateData globalTemplateData) {
        Class<?> globalDocClass = findOneClass(ApiGlobalPage.class);
        if (globalDocClass == null) {
            globalDocClass = findOneClass(ApiGlobalPages.class);
            if (globalDocClass == null) {
                return ApiGlobalDocReader.readDefault(globalTemplateData);
            }
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
}
