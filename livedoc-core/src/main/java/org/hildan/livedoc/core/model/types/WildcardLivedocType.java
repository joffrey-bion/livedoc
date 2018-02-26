package org.hildan.livedoc.core.model.types;

import java.util.List;

class WildcardLivedocType extends BoundedLivedocType implements LivedocType {

    WildcardLivedocType(BoundType boundType, List<LivedocType> bounds) {
        super(boundType, bounds);
    }

    @Override
    TypeElement getBaseTypeElement() {
        return TypeElement.QUESTION_MARK;
    }

}
