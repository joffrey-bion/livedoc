package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;

public interface DocReader {

    Collection<Class<?>> findControllerTypes();

    /**
     * Also called on controllers that were found by other ApiScanners. Should return an empty optional when not able to
     * build something from the given controller. Should only fill in the top level items, not the methods docs.
     */
    // TODO create a class without method information and make it the return type of this method to avoid confusion
    Optional<ApiDoc> buildApiDocBase(Class<?> controllerType);

    /**
     * Should return an empty optional when not able to build something from the given method.
     */
    Optional<ApiMethodDoc> buildApiMethodDoc(Method method, ApiDoc parentApiDoc, TemplateProvider templateProvider);
}
