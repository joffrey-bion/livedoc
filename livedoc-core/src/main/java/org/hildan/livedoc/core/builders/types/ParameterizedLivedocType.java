package org.hildan.livedoc.core.builders.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParameterizedLivedocType implements LivedocType {

    private final LivedocType rawType;

    private final List<LivedocType> typeParams;

    public ParameterizedLivedocType(LivedocType rawType, List<LivedocType> typeParams) {
        this.rawType = rawType;
        this.typeParams = typeParams;
    }

    public LivedocType getRawType() {
        return rawType;
    }

    public List<LivedocType> getTypeParams() {
        return typeParams;
    }

    @Override
    public LivedocTypeKind getKind() {
        return LivedocTypeKind.PARAMETERIZED;
    }

    @Override
    public String getOneLineText() {
        if (typeParams.isEmpty()) {
            return rawType.getOneLineText();
        }
        String typeParamsStr = typeParams.stream().map(LivedocType::getOneLineText).collect(Collectors.joining(", "));
        return rawType.getOneLineText() + '<' + typeParamsStr + '>';
    }

    @Override
    public List<Class<?>> getComposingTypes() {
        List<Class<?>> types = new ArrayList<>();
        types.addAll(rawType.getComposingTypes());
        for (LivedocType typeParam : typeParams) {
            types.addAll(typeParam.getComposingTypes());
        }
        return types;
    }
}
