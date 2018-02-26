package org.hildan.livedoc.core.model.types;

import java.util.List;

class LivedocTypeVariable extends BoundedLivedocType implements LivedocType {

    private final String name;

    LivedocTypeVariable(String name, List<LivedocType> bounds) {
        super(BoundType.UPPER, bounds);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    TypeElement getBaseTypeElement() {
        return TypeElement.text(name);
    }
}
