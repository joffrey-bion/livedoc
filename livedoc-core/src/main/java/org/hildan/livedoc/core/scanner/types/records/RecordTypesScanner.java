package org.hildan.livedoc.core.scanner.types.records;

import java.lang.reflect.Type;
import java.util.Set;

public interface RecordTypesScanner {

    Set<Type> getFieldTypes(Class<?> clazz);
}
