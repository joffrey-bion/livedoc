package org.hildan.livedoc.core.scanners.types.references;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.generics.GenericDeclarationExplorer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultTypeReferenceProvider implements TypeReferenceProvider {

    private final Predicate<? super Type> typeFilter;

    private final Map<Type, LivedocType> cache;

    public DefaultTypeReferenceProvider(Predicate<? super Type> typeFilter) {
        this.typeFilter = typeFilter;
        this.cache = new HashMap<>();
    }

    @Nullable
    @Override
    public LivedocType getReference(@NotNull Type type) {
        LivedocType livedocType = cache.get(type);
        if (livedocType != null) {
            return livedocType;
        }
        livedocType = GenericDeclarationExplorer.explore(type, new TypeReferenceBuilder(typeFilter));
        cache.put(type, livedocType);
        return livedocType;
    }

    @NotNull
    @Override
    public Set<Type> getProvidedReferences() {
        return cache.keySet();
    }
}
