package org.hildan.livedoc.core.model.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.util.ListJoiner;

public abstract class BoundedLivedocType implements LivedocType {

    private final BoundType boundType;

    private final List<LivedocType> bounds;

    BoundedLivedocType(BoundType boundType, List<LivedocType> bounds) {
        this.boundType = boundType;
        this.bounds = bounds;
    }

    @Override
    public List<TypeElement> getTypeElements() {
        if (bounds.isEmpty()) {
            return Collections.singletonList(getBaseTypeElement());
        }
        List<TypeElement> elements = new ArrayList<>();
        elements.add(getBaseTypeElement());
        elements.add(boundType.getKeyword());
        elements.addAll(getBoundElements());
        return elements;
    }

    abstract TypeElement getBaseTypeElement();

    private List<TypeElement> getBoundElements() {
        return bounds.stream().map(LivedocType::getTypeElements).collect(new ListJoiner<>(TypeElement.COMMA));
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return bounds.stream()
                     .map(LivedocType::getComposingTypes)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }
}
