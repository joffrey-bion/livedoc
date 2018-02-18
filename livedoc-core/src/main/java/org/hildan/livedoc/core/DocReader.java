package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;

/**
 * A component able to build documentation objects for controllers and their methods.
 */
public interface DocReader {

    /**
     * Finds and returns all controllers that this reader could document.
     *
     * @return a collection of controllers identified by this reader
     */
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
    Optional<ApiDoc> buildApiDocBase(Class<?> controllerType);

    /**
     * Builds an {@link ApiMethodDoc} for the given method. Returns an empty optional when not able to build something
     * for the given method.
     *
     * @param method
     *         the method to document
     * @param controller
     *         the controller in which the given method should be resolved. Cannot be retrieved from the {@link Method}
     *         because the method could be inherited from a parent controller.
     * @param parentApiDoc
     *         the {@link ApiDoc} which the given method is part of
     * @param templateProvider
     *         a {@link TemplateProvider} for types mentioned in the method's doc
     *
     * @return a new {@link ApiMethodDoc} for the given method, or an empty optional if this reader is not able to build
     * a doc for the given method.
     */
    Optional<ApiMethodDoc> buildApiMethodDoc(Method method, Class<?> controller, ApiDoc parentApiDoc,
            TemplateProvider templateProvider);
}
