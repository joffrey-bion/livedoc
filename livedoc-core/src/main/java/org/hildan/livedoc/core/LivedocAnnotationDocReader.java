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

    @Override
    public Collection<Class<?>> findControllerTypes() {
        return annotatedTypesFinder.apply(Api.class);
    }

    @Override
    public Optional<ApiDoc> buildApiDocBase(Class<?> controllerType) {
        return Optional.of(ApiDocReader.read(controllerType));
    }

    @Override
    public Optional<ApiMethodDoc> buildApiMethodDoc(Method method, Class<?> controller, ApiDoc parentApiDoc,
            TemplateProvider templateProvider) {
        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        if (methodAnnotation == null) {
            return Optional.empty(); // this basic builder only supports annotated methods
        }
        ApiMethodDoc apiMethodDoc = ApiMethodDocReader.read(method, parentApiDoc, templateProvider);
        return Optional.of(apiMethodDoc);
    }
}
