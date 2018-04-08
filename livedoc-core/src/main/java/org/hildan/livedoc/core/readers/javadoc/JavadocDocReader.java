package org.hildan.livedoc.core.readers.javadoc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

public class JavadocDocReader implements DocReader {

    @NotNull
    @Override
    public Collection<Class<?>> findControllerTypes() {
        // although this reader can read docs from any class, it cannot identify a class as a controller
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public Optional<ApiDoc> buildApiDocBase(@NotNull Class<?> controllerType) {
        ApiDoc doc = new ApiDoc();
        doc.setName(controllerType.getSimpleName());
        doc.setDescription(JavadocHelper.getJavadocDescription(controllerType).orElse(null));
        return Optional.of(doc);
    }

    @Override
    public boolean isApiOperation(@NotNull Method method, @NotNull Class<?> controller) {
        // although this reader can read docs from any method, it cannot identify a method as an API operation
        return false;
    }

    @NotNull
    @Override
    public Optional<ApiOperationDoc> buildApiOperationDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        ApiOperationDoc doc = new ApiOperationDoc();
        doc.setName(method.getName());
        doc.setDescription(JavadocHelper.getJavadocDescription(method).orElse(null));
        return Optional.of(doc);
    }
}
