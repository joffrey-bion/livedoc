package org.hildan.livedoc.core.builders.types;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LivedocTypeVariable implements LivedocType {

    private final String name;

    private final List<LivedocType> bounds;

    public LivedocTypeVariable(String name, List<LivedocType> bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public List<LivedocType> getBounds() {
        return bounds;
    }

    @Override
    public LivedocTypeKind getKind() {
        return LivedocTypeKind.TYPE_VARIABLE;
    }

    @Override
    public String getOneLineText() {
        if (bounds.isEmpty()) {
            return name;
        }
        String boundsStr = bounds.stream().map(LivedocType::getOneLineText).collect(Collectors.joining(", "));
        return name + " extends " + boundsStr;
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return bounds.stream()
                     .map(LivedocType::getComposingTypes)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }
}
