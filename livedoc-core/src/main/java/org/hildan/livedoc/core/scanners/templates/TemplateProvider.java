package org.hildan.livedoc.core.scanners.templates;

import java.util.Map;

/**
 * Provides example objects to display as JSON for each type.
 */
public interface TemplateProvider {

    /**
     * Returns an example object for the given type. The returned value is intended to be serialized, and is not
     * guaranteed to be of the given type. For a complex object, it is often returned as a {@link Map}.
     *
     * @param type
     *         the type to get an example for
     *
     * @return an example object for the given type
     */
    Object getTemplate(Class<?> type);
}
