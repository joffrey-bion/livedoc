package org.hildan.livedoc.core.builders.types;

import java.util.Collections;
import java.util.List;

public class SimpleLivedocType implements LivedocType {

    private final String id;

    private final String name;

    private final Class<?> clazz;

    public SimpleLivedocType(String id, String name, Class<?> clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public LivedocTypeKind getKind() {
        return LivedocTypeKind.SIMPLE;
    }

    @Override
    public String getOneLineText() {
        return name;
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return Collections.singletonList(clazz);
    }
}
