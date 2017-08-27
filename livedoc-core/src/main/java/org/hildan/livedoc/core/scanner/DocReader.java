package org.hildan.livedoc.core.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.scanner.templates.ObjectTemplate;

public interface DocReader {

    Collection<Class<?>> findControllerTypes();

    /**
     * Also called on controllers that were found by other ApiScanners. Should return null when not able to build
     * something from the given controller. Should only fill in the top level items, not the methods docs.
     */
    // TODO create a class without method information and make it the return type of this method to avoid confusion
    ApiDoc buildApiDocBase(Class<?> controllerType);

    /**
     * Should return null when not able to build something from the given method.
     */
    ApiMethodDoc buildApiMethodDoc(Method method, ApiDoc parentApiDoc, Map<Class<?>, ObjectTemplate> templates);

    /**
     * Extracts types (mentioned in the given method's signature) that are relevant for the API. Only root type
     * declarations should be returned here, recursive exploration of types is done with these starting points.
     */
    Collection<? extends Type> extractTypesToDocument(Method method);

    /**
     * Extracts types that are relevant for the API, but are not be referenced from the rest of the documentation. Only
     * root type declarations should be returned here, recursive exploration of types is done with these starting
     * points.
     */
    Collection<? extends Type> getAdditionalTypesToDocument();
}
