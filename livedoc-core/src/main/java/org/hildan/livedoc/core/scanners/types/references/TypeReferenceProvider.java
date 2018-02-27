package org.hildan.livedoc.core.scanners.types.references;

import java.lang.reflect.Type;
import java.util.Set;

import org.hildan.livedoc.core.model.types.LivedocType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypeReferenceProvider {

    @Nullable
    LivedocType getReference(@NotNull Type type);

    @NotNull
    Set<Type> getProvidedReferences();
}
