package org.hildan.livedoc.springmvc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.readers.messages.MessageHandlerReader;
import org.hildan.livedoc.springmvc.readers.request.RequestHandlerReader;
import org.hildan.livedoc.springmvc.utils.ClasspathUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * An implementation of {@link DocReader} that builds the documentation from Spring annotations. With this
 * implementation, controllers are classes annotated with {@link Controller} and the likes, and methods are the ones
 * annotated with {@link RequestMapping} and the likes.
 */
public class SpringDocReader implements DocReader {

    private final AnnotatedTypesFinder annotatedTypesFinder;

    /**
     * Creates a new {@link SpringDocReader} with the given {@link AnnotatedTypesFinder}.
     *
     * @param annotatedTypesFinder
     *         an abstract way of finding classes annotated with a given annotation, used to find controllers
     */
    public SpringDocReader(AnnotatedTypesFinder annotatedTypesFinder) {
        this.annotatedTypesFinder = annotatedTypesFinder;
    }

    @NotNull
    @Override
    public Collection<Class<?>> findControllerTypes() {
        Collection<Class<?>> controllers = annotatedTypesFinder.apply(Controller.class);
        if (ClasspathUtils.isRestControllerOnClasspath()) {
            controllers.addAll(annotatedTypesFinder.apply(RestController.class));
        }
        if (ClasspathUtils.isRepositoryRestControllerOnClassPath()) {
            controllers.addAll(annotatedTypesFinder.apply(RepositoryRestController.class));
        }
        return controllers;
    }

    /**
     * ApiDoc is initialized with the Controller's simple class name.
     */
    @NotNull
    @Override
    public Optional<ApiDoc> buildApiDocBase(@NotNull Class<?> controllerType) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setName(controllerType.getSimpleName());
        return Optional.of(apiDoc);
    }

    @Override
    public boolean isApiOperation(@NotNull Method method, @NotNull Class<?> controller) {
        if (ClasspathUtils.isRequestMappingOnClasspath() && method.isAnnotationPresent(RequestMapping.class)) {
            return true;
        }
        if (ClasspathUtils.isGetMappingOnClasspath()) {
            if (method.isAnnotationPresent(GetMapping.class) //
                    || method.isAnnotationPresent(PostMapping.class) //
                    || method.isAnnotationPresent(PutMapping.class) //
                    || method.isAnnotationPresent(DeleteMapping.class)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Optional<ApiOperationDoc> buildApiOperationDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        if (!isApiOperation(method, controller)) {
            return Optional.empty();
        }
        return Optional.of(
                RequestHandlerReader.buildApiOperationDoc(method, controller, typeReferenceProvider, templateProvider));
    }

    @Override
    public boolean usesAsyncMessages(@NotNull Method method, @NotNull Class<?> controller) {
        if (ClasspathUtils.isMessageMappingOnClasspath() && method.isAnnotationPresent(MessageMapping.class)) {
            return true;
        }
        if (ClasspathUtils.isSubscribeMappingOnClasspath() && method.isAnnotationPresent(SubscribeMapping.class)) {
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public List<AsyncMessageDoc> buildAsyncMessageDocs(@NotNull Collection<Method> methods,
            @NotNull Class<?> controller, @NotNull ApiDoc parentApiDoc,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        return methods.stream()
                      .map(m -> MessageHandlerReader.read(m, controller, typeReferenceProvider))
                      .flatMap(Collection::stream)
                      .collect(Collectors.toList());
    }
}
