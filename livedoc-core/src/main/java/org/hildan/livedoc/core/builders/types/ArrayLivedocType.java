package org.hildan.livedoc.core.builders.types;

import java.util.ArrayList;
import java.util.List;

public class ArrayLivedocType implements LivedocType {

    private final LivedocType componentType;

    ArrayLivedocType(LivedocType componentType) {
        this.componentType = componentType;
    }

    @Override
    public List<TypeElement> getTypeElements() {
        List<TypeElement> elements = new ArrayList<>(componentType.getTypeElements());
        elements.add(TypeElement.SQUARE_BRACKETS);
        return elements;
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return componentType.getComposingTypes();
    }
}
