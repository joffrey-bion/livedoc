package org.hildan.livedoc.core.builders.types;

import java.util.List;

public class ArrayLivedocType implements LivedocType {

    private final LivedocType componentType;

    public ArrayLivedocType(LivedocType componentType) {
        this.componentType = componentType;
    }

    public LivedocType getComponentType() {
        return componentType;
    }

    @Override
    public String getOneLineText() {
        return componentType.getOneLineText() + "[]";
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return componentType.getComposingTypes();
    }
}
