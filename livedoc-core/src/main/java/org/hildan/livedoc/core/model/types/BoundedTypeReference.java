package org.hildan.livedoc.core.model.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.util.ListJoiner;

public abstract class BoundedTypeReference implements LivedocType {

    private final BoundType boundType;

    private final List<LivedocType> bounds;

    BoundedTypeReference(BoundType boundType, List<LivedocType> bounds) {
        this.boundType = boundType;
        this.bounds = bounds;
    }

    @Override
    public List<TypeReferenceElement> getTypeElements() {
        if (bounds.isEmpty()) {
            return Collections.singletonList(getBaseTypeElement());
        }
        List<TypeReferenceElement> elements = new ArrayList<>();
        elements.add(getBaseTypeElement());
        elements.add(boundType.getKeyword());
        elements.addAll(getBoundElements());
        return elements;
    }

    abstract TypeReferenceElement getBaseTypeElement();

    private List<TypeReferenceElement> getBoundElements() {
        return bounds.stream().map(LivedocType::getTypeElements).collect(new ListJoiner<>(TypeReferenceElement.COMMA));
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return bounds.stream()
                     .map(LivedocType::getComposingTypes)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoundedTypeReference that = (BoundedTypeReference) o;

        if (boundType != that.boundType) {
            return false;
        }
        return bounds.equals(that.bounds);
    }

    @Override
    public int hashCode() {
        int result = boundType.hashCode();
        result = 31 * result + bounds.hashCode();
        return result;
    }
}
