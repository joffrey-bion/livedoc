package org.hildan.livedoc.core.model.types;

import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.util.ListJoiner;

public class ParameterizedTypeReference implements LivedocType {

    private final LivedocType rawType;

    private final List<LivedocType> typeParams;

    public ParameterizedTypeReference(LivedocType rawType, List<LivedocType> typeParams) {
        this.rawType = rawType;
        this.typeParams = typeParams;
    }

    @Override
    public List<TypeReferenceElement> getTypeElements() {
        if (typeParams.isEmpty()) {
            return rawType.getTypeElements();
        }
        List<TypeReferenceElement> paramElems = typeParams.stream()
                                                          .map(LivedocType::getTypeElements)
                                                          .collect(new ListJoiner<>(TypeReferenceElement.COMMA));
        List<TypeReferenceElement> elems = new ArrayList<>(rawType.getTypeElements());
        elems.add(TypeReferenceElement.OPEN_ANGLE_BRACKET);
        elems.addAll(paramElems);
        elems.add(TypeReferenceElement.CLOSE_ANGLE_BRACKET);
        return elems;
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        List<Class<?>> types = new ArrayList<>(rawType.getComposingTypes());
        for (LivedocType typeParam : typeParams) {
            types.addAll(typeParam.getComposingTypes());
        }
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParameterizedTypeReference that = (ParameterizedTypeReference) o;

        if (!rawType.equals(that.rawType)) {
            return false;
        }
        return typeParams.equals(that.typeParams);
    }

    @Override
    public int hashCode() {
        int result = rawType.hashCode();
        result = 31 * result + typeParams.hashCode();
        return result;
    }
}
