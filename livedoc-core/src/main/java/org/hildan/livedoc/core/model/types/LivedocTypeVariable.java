package org.hildan.livedoc.core.model.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.util.ListJoiner;

public class LivedocTypeVariable implements LivedocType {

    private final String name;

    private final List<LivedocType> bounds;

    LivedocTypeVariable(String name, List<LivedocType> bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<TypeElement> getTypeElements() {
        if (bounds.isEmpty()) {
            return Collections.singletonList(TypeElement.text(name));
        }
        List<TypeElement> boundElements = bounds.stream()
                                                .map(LivedocType::getTypeElements)
                                                .collect(new ListJoiner<>(TypeElement.COMMA));
        List<TypeElement> elements = new ArrayList<>();
        elements.add(TypeElement.text(name));
        elements.add(TypeElement.EXTENDS);
        elements.addAll(boundElements);
        return elements;
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return bounds.stream()
                     .map(LivedocType::getComposingTypes)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }
}
