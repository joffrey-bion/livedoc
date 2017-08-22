package org.hildan.livedoc.core.scanner.properties;

import java.util.Set;

public interface PropertyScanner {

    Set<Property> getProperties(Class<?> clazz);
}
