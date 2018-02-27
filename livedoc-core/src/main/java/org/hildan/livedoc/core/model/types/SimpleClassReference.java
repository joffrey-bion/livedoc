package org.hildan.livedoc.core.model.types;

import java.util.Collections;
import java.util.List;

public class SimpleClassReference implements LivedocType {

    private final String name;

    private final Class<?> clazz;

    private String livedocId;

    public SimpleClassReference(String name, Class<?> clazz, String livedocId) {
        this.name = name;
        this.clazz = clazz;
        this.livedocId = livedocId;
    }

    @Override
    public List<TypeReferenceElement> getTypeElements() {
        return Collections.singletonList(TypeReferenceElement.link(name, livedocId));
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return Collections.singletonList(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleClassReference that = (SimpleClassReference) o;

        if (!name.equals(that.name)) {
            return false;
        }
        if (!clazz.equals(that.clazz)) {
            return false;
        }
        return livedocId != null ? livedocId.equals(that.livedocId) : that.livedocId == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + clazz.hashCode();
        result = 31 * result + (livedocId != null ? livedocId.hashCode() : 0);
        return result;
    }
}
