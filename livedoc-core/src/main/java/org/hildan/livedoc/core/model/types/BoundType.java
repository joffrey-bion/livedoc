package org.hildan.livedoc.core.model.types;

public enum BoundType {
    NONE(null),
    UPPER(TypeReferenceElement.EXTENDS),
    LOWER(TypeReferenceElement.SUPER);

    private final TypeReferenceElement keyword;

    BoundType(TypeReferenceElement keyword) {
        this.keyword = keyword;
    }

    public TypeReferenceElement getKeyword() {
        return keyword;
    }
}
