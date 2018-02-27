package org.hildan.livedoc.core.model.types;

import java.util.ArrayList;
import java.util.List;

public class ArrayTypeReference implements LivedocType {

    private final LivedocType componentType;

    public ArrayTypeReference(LivedocType componentType) {
        this.componentType = componentType;
    }

    @Override
    public List<TypeReferenceElement> getTypeElements() {
        List<TypeReferenceElement> elements = new ArrayList<>(componentType.getTypeElements());
        elements.add(TypeReferenceElement.SQUARE_BRACKETS);
        return elements;
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return componentType.getComposingTypes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArrayTypeReference that = (ArrayTypeReference) o;
        return componentType.equals(that.componentType);
    }

    @Override
    public int hashCode() {
        return componentType.hashCode();
    }
}
