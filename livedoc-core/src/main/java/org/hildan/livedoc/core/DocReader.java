package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * A component able to build documentation objects for controllers and their methods.
 */
public interface DocReader {

    /**
     * Finds and returns all controllers that this reader could document.
     *
     * @return a collection of controllers identified by this reader
     */
    @NotNull
    Collection<Class<?>> findControllerTypes();

    /**
     * Builds a base documentation for the given controller, without method docs. Note that this method is also called
     * on controllers that were found by other {@link DocReader}s. It should return an empty optional when not able to
     * build something for the given controller.
     *
     * @param controllerType
     *         the class of the controller to build a doc for
     *
     * @return a new {@link ApiDoc} for the given controller type, or an empty optional if this reader is not able to
     * build a doc for the given controller.
     */
    // TODO create a class without method information and make it the return type of this method to avoid confusion
    @NotNull
    Optional<ApiDoc> buildApiDocBase(@NotNull Class<?> controllerType);

    /**
     * Builds an {@link ApiOperationDoc} for the given method. Returns an empty optional when not able to build
     * something for the given method.
     *
     * @param method
     *         the method to document
     * @param controller
     *         the controller in which the given method should be resolved. Cannot be retrieved from the {@link Method}
     *         because the method could be inherited from a parent controller.
     * @param parentApiDoc
     *         the {@link ApiDoc} which the given method is part of
     * @param typeReferenceProvider
     *         a {@link TypeReferenceProvider} to get {@link LivedocType}s for the request/body types of the method
     * @param templateProvider
     *         a {@link TemplateProvider} for types mentioned in the method's doc
     *
     * @return a new {@link ApiOperationDoc} for the given method, or an empty optional if this reader is not able to
     * build a doc for the given method.
     */
    @NotNull
    Optional<ApiOperationDoc> buildApiOperationDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider);
}
