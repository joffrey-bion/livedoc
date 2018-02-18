package org.hildan.livedoc.core.model.types;

import java.util.Collections;
import java.util.List;

public class SimpleLivedocType implements LivedocType {

    private final String name;

    private final Class<?> clazz;

    SimpleLivedocType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public List<TypeElement> getTypeElements() {
        return Collections.singletonList(TypeElement.ref(name, clazz));
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return Collections.singletonList(clazz);
    }
}
