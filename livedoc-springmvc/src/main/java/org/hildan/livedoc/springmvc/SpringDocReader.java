package org.hildan.livedoc.springmvc;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import org.hildan.livedoc.core.AnnotatedTypesFinder;
import org.hildan.livedoc.core.DocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.scanner.builder.SpringHeaderBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringMediaTypeBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathVariableBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringQueryParamBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringRequestBodyBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseStatusBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringVerbBuilder;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
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

    @NotNull
    @Override
    public Optional<ApiMethodDoc> buildApiMethodDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        if (!canReadInfoFrom(method)) {
            return Optional.empty();
        }
        return Optional.of(buildApiMethodDoc(method, controller, typeReferenceProvider, templateProvider));
    }

    private boolean canReadInfoFrom(Method method) {
        if (ClasspathUtils.isRequestMappingOnClasspath() && method.isAnnotationPresent(RequestMapping.class)) {
            return true;
        }
        if (ClasspathUtils.isMessageMappingOnClasspath() && method.isAnnotationPresent(MessageMapping.class)) {
            return true;
        }
        if (ClasspathUtils.isSubscribeMappingOnClasspath() && method.isAnnotationPresent(SubscribeMapping.class)) {
            return true;
        }
        return false;
    }

    private ApiMethodDoc buildApiMethodDoc(Method method, Class<?> controller,
            TypeReferenceProvider typeReferenceProvider, TemplateProvider templateProvider) {
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setPaths(SpringPathBuilder.buildPath(method, controller));
        apiMethodDoc.setName(method.getName());
        apiMethodDoc.setVerbs(SpringVerbBuilder.buildVerb(method, controller));
        apiMethodDoc.setProduces(SpringMediaTypeBuilder.buildProduces(method, controller));
        apiMethodDoc.setConsumes(SpringMediaTypeBuilder.buildConsumes(method, controller));
        apiMethodDoc.setHeaders(SpringHeaderBuilder.buildHeaders(method, controller));
        apiMethodDoc.setPathParameters(SpringPathVariableBuilder.buildPathVariable(method, typeReferenceProvider));
        apiMethodDoc.setQueryParameters(
                SpringQueryParamBuilder.buildQueryParams(method, controller, typeReferenceProvider));
        apiMethodDoc.setRequestBody(
                SpringRequestBodyBuilder.buildRequestBody(method, typeReferenceProvider, templateProvider));
        apiMethodDoc.setResponseBodyType(buildResponse(method, typeReferenceProvider));
        apiMethodDoc.setResponseStatusCode(SpringResponseStatusBuilder.buildResponseStatusCode(method));
        return apiMethodDoc;
    }

    /**
     * Builds the response body type from the method's return type.
     * <p>
     * This method handles Spring's {@link ResponseEntity} wrappers by unwrapping them, because they don't matter to the
     * user of the doc.
     *
     * @param method
     *         the method to create the response object for
     * @param typeReferenceProvider
     *         a provider for {@link LivedocType} objects
     *
     * @return the created {@link LivedocType}
     */
    private static LivedocType buildResponse(Method method, TypeReferenceProvider typeReferenceProvider) {
        Type returnType = getActualReturnType(method);
        return typeReferenceProvider.getReference(returnType);
    }

    private static Type getActualReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (ResponseEntity.class.equals(method.getReturnType())) {
            returnType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
        }
        return returnType;
    }
}
