package org.hildan.livedoc.springmvc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

import org.hildan.livedoc.core.AnnotatedTypesFinder;
import org.hildan.livedoc.core.DocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.springmvc.scanner.builder.SpringHeaderBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringMediaTypeBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathVariableBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringQueryParamBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringRequestBodyBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseStatusBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringVerbBuilder;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.springframework.data.rest.webmvc.RepositoryRestController;
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
    @Override
    public Optional<ApiDoc> buildApiDocBase(Class<?> controllerType) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setName(controllerType.getSimpleName());
        return Optional.of(apiDoc);
    }

    @Override
    public Optional<ApiMethodDoc> buildApiMethodDoc(Method method, Class<?> controller, ApiDoc parentApiDoc,
            TemplateProvider templateProvider) {
        if (!canReadInfoFrom(method)) {
            return Optional.empty();
        }
        return Optional.of(buildApiMethodDoc(method, controller, templateProvider));
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

    private ApiMethodDoc buildApiMethodDoc(Method method, Class<?> controller, TemplateProvider templateProvider) {
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setPaths(SpringPathBuilder.buildPath(method, controller));
        apiMethodDoc.setName(method.getName());
        apiMethodDoc.setVerbs(SpringVerbBuilder.buildVerb(method, controller));
        apiMethodDoc.setProduces(SpringMediaTypeBuilder.buildProduces(method, controller));
        apiMethodDoc.setConsumes(SpringMediaTypeBuilder.buildConsumes(method, controller));
        apiMethodDoc.setHeaders(SpringHeaderBuilder.buildHeaders(method, controller));
        apiMethodDoc.setPathParameters(SpringPathVariableBuilder.buildPathVariable(method));
        apiMethodDoc.setQueryParameters(SpringQueryParamBuilder.buildQueryParams(method, controller));
        apiMethodDoc.setRequestBody(SpringRequestBodyBuilder.buildRequestBody(method, templateProvider));
        apiMethodDoc.setResponseBodyType(SpringResponseBuilder.buildResponse(method));
        apiMethodDoc.setResponseStatusCode(SpringResponseStatusBuilder.buildResponseStatusCode(method));
        return apiMethodDoc;
    }
}
