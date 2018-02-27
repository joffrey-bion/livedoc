package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.builders.doc.ApiDocReader;
import org.hildan.livedoc.core.builders.doc.ApiMethodDocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link DocReader} that reads Livedoc annotations to build the documentation. In this
 * implementation, controllers are classes annotated with {@link Api}, and methods are only found and documented if
 * annotated with {@link ApiMethod}.
 */
public class LivedocAnnotationDocReader implements DocReader {

    private final AnnotatedTypesFinder annotatedTypesFinder;

    public LivedocAnnotationDocReader(AnnotatedTypesFinder annotatedTypesFinder) {
        this.annotatedTypesFinder = annotatedTypesFinder;
    }

    @NotNull
    @Override
    public Collection<Class<?>> findControllerTypes() {
        return annotatedTypesFinder.apply(Api.class);
    }

    @NotNull
    @Override
    public Optional<ApiDoc> buildApiDocBase(@NotNull Class<?> controllerType) {
        return Optional.of(ApiDocReader.read(controllerType));
    }

    @NotNull
    @Override
    public Optional<ApiMethodDoc> buildApiMethodDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        if (methodAnnotation == null) {
            return Optional.empty(); // this basic builder only supports annotated methods
        }
        ApiMethodDoc doc = ApiMethodDocReader.read(method, parentApiDoc, typeReferenceProvider, templateProvider);
        return Optional.of(doc);
    }
}
