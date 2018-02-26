package org.hildan.livedoc.core.model.types;

public enum BoundType {
    NONE(null),
    UPPER(TypeElement.EXTENDS),
    LOWER(TypeElement.SUPER);

    private final TypeElement keyword;

    BoundType(TypeElement keyword) {
        this.keyword = keyword;
    }

    public TypeElement getKeyword() {
        return keyword;
    }
}
