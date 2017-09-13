package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.builders.doc.ApiDocReader;
import org.hildan.livedoc.core.builders.doc.ApiMethodDocReader;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;

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
    public Optional<ApiMethodDoc> buildApiMethodDoc(Method method, ApiDoc parentApiDoc,
            TemplateProvider templateProvider) {
        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        if (methodAnnotation == null) {
            return Optional.empty(); // this basic builder only supports annotated methods
        }
        ApiMethodDoc apiMethodDoc = ApiMethodDocReader.read(method, parentApiDoc, templateProvider);
        return Optional.of(apiMethodDoc);
    }

    @Override
    public Collection<? extends Type> getAdditionalTypesToDocument() {
        return annotatedTypesFinder.apply(ApiObject.class);
    }
}
