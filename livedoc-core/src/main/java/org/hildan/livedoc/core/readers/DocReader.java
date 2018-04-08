package org.hildan.livedoc.core.readers;

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
 * Reads API documentation objects from controller classes and their methods.
 */
public interface DocReader {

    /**
     * Finds and returns all classes that this reader identifies as controllers.
     *
     * @return a collection of controllers identified by this reader
     */
    @NotNull
    Collection<Class<?>> findControllerTypes();

    /**
     * Builds a base documentation for the given controller, without method docs. Note that this method is also called
     * on controllers that were found by other {@link DocReader}s. It should return an empty optional when not able to
     * read any documentation for the given controller.
     *
     * @param controllerType
     *         the class of the controller to build a doc for
     *
     * @return a new {@link ApiDoc} for the given controller type, or an empty optional if this reader is not able to
     * build a doc for the given controller.
     */
    @NotNull
    Optional<ApiDoc> buildApiDocBase(@NotNull Class<?> controllerType);

    /**
     * Returns true if this reader identifies the given method as an API operation. A method will be considered an API
     * operation if at least one reader identifies it as such. Returning false here does not mean the given method is
     * not an API operation, it simply means that this particular reader doesn't see it as such.
     *
     * @param method
     *         the method to check
     * @param controller
     *         the controller the given method belongs to
     *
     * @return true if this reader identifies the given method as an API operation, false otherwise.
     */
    boolean isApiOperation(@NotNull Method method, @NotNull Class<?> controller);

    /**
     * Builds an {@link ApiOperationDoc} for the given method. Any method could be passed on here, even the ones for
     * which this reader's {@link #isApiOperation(Method, Class)} returned false. If this reader can't read any
     * documentation data for the given method, it should return an empty optional.
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
