package org.hildan.livedoc.core.builders.types;

import java.util.List;

public interface LivedocType {

    LivedocTypeKind getKind();

    String getOneLineText();

    List<Class<?>> getComposingTypes();
}
