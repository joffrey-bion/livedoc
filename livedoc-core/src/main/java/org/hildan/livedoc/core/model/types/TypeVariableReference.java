package org.hildan.livedoc.core.model.types;

import java.util.List;

public class TypeVariableReference extends BoundedTypeReference implements LivedocType {

    private final String name;

    public TypeVariableReference(String name, List<LivedocType> bounds) {
        super(BoundType.UPPER, bounds);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    TypeReferenceElement getBaseTypeElement() {
        return TypeReferenceElement.text(name);
    }
}
