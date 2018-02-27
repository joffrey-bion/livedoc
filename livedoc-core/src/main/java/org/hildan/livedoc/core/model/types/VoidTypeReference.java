package org.hildan.livedoc.core.model.types;

import java.util.Collections;
import java.util.List;

public class VoidTypeReference implements LivedocType {

    public static final VoidTypeReference INSTANCE = new VoidTypeReference();

    private VoidTypeReference() {
    }

    @Override
    public List<TypeReferenceElement> getTypeElements() {
        return Collections.singletonList(TypeReferenceElement.VOID);
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        return Collections.emptyList();
    }
}
