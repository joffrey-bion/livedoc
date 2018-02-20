package org.hildan.livedoc.core.scanners.properties;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A component that inspects the list of properties of a given type. This is used to generate the documentation of the
 * properties, and also to explore types recursively. The definition of a property is decided by the implementations of
 * this interface.
 */
public interface PropertyScanner {

    /**
     * Gets the properties of the given class, as defined by the implementation.
     *
     * @param type
     *         the type to get the properties of
     *
     * @return the properties of the given class
     */
    List<Property> getProperties(Type type);
}
