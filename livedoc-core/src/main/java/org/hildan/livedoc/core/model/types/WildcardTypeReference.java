package org.hildan.livedoc.core.model.types;

import java.util.List;

public class WildcardTypeReference extends BoundedTypeReference implements LivedocType {

    public WildcardTypeReference(BoundType boundType, List<LivedocType> bounds) {
        super(boundType, bounds);
    }

    @Override
    TypeReferenceElement getBaseTypeElement() {
        return TypeReferenceElement.QUESTION_MARK;
    }

}
