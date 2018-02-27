package org.hildan.livedoc.core.scanners.types.references;

import java.util.Collection;

public interface TypeReferenceTracker {

    Collection<Class<?>> getReferencedTypes();
}
