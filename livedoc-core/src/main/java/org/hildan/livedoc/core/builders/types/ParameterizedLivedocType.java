package org.hildan.livedoc.core.builders.types;

import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.util.ListJoiner;

public class ParameterizedLivedocType implements LivedocType {

    private final SimpleLivedocType rawType;

    private final List<LivedocType> typeParams;

    ParameterizedLivedocType(SimpleLivedocType rawType, List<LivedocType> typeParams) {
        this.rawType = rawType;
        this.typeParams = typeParams;
    }

    @Override
    public List<TypeElement> getTypeElements() {
        if (typeParams.isEmpty()) {
            return rawType.getTypeElements();
        }
        List<TypeElement> paramElems = typeParams.stream()
                                                 .map(LivedocType::getTypeElements)
                                                 .collect(new ListJoiner<>(TypeElement.COMMA));
        List<TypeElement> elems = new ArrayList<>(rawType.getTypeElements());
        elems.add(TypeElement.OPEN_ANGLE_BRACKET);
        elems.addAll(paramElems);
        elems.add(TypeElement.CLOSE_ANGLE_BRACKET);
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
}
