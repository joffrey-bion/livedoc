package org.hildan.livedoc.core.scanner.properties;

import java.util.List;

public interface PropertyScanner {

    List<Property> getProperties(Class<?> clazz);
}
