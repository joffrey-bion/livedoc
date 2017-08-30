package org.hildan.livedoc.core.scanners.properties;

import java.util.List;

public interface PropertyScanner {

    List<Property> getProperties(Class<?> clazz);
}
