package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.messages.ApiMessageChannel;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link DocReader} that reads Livedoc annotations to build the documentation. In this
 * implementation, controllers are classes annotated with {@link Api}, and methods are only found and documented if
 * annotated with {@link ApiOperation}.
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

    @Override
    public boolean isApiOperation(@NotNull Method method, @NotNull Class<?> controller) {
        return method.isAnnotationPresent(ApiOperation.class);
    }

    @NotNull
    @Override
    public Optional<ApiOperationDoc> buildApiOperationDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        ApiOperationDoc doc = ApiOperationDocReader.read(method, parentApiDoc, typeReferenceProvider, templateProvider);
        return Optional.ofNullable(doc);
    }

    @Override
    public boolean usesAsyncMessages(@NotNull Method method, @NotNull Class<?> controller) {
        return method.getDeclaredAnnotationsByType(ApiMessageChannel.class).length > 0;
    }

    @NotNull
    @Override
    public List<AsyncMessageDoc> buildAsyncMessageDocs(@NotNull Collection<Method> methods,
            @NotNull Class<?> controller, @NotNull ApiDoc parentApiDoc,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        return methods.stream()
                      .map(m -> MessagesDocReader.readAsyncMessages(m, typeReferenceProvider))
                      .flatMap(Collection::stream)
                      .collect(Collectors.toList());
    }
}
