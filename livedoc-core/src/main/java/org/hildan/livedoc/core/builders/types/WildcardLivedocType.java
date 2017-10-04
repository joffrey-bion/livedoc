package org.hildan.livedoc.core.builders.types;

import java.util.Collections;
import java.util.List;

public class WildcardLivedocType implements LivedocType {

    public static final WildcardLivedocType INSTANCE = new WildcardLivedocType();

    private WildcardLivedocType() {
    }

    @Override
    public String getOneLineText() {
        return "?";
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return Collections.emptyList();
    }
}
