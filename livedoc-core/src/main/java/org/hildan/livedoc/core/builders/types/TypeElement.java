package org.hildan.livedoc.core.builders.types;

import org.hildan.livedoc.core.util.LivedocUtils;

public class TypeElement {

    static final TypeElement COMMA = text(", ");

    static final TypeElement OPEN_ANGLE_BRACKET = text("<");

    static final TypeElement CLOSE_ANGLE_BRACKET = text(">");

    static final TypeElement SQUARE_BRACKETS = text("[]");

    static final TypeElement QUESTION_MARK = text("?");

    static final TypeElement EXTENDS = text(" extends ");

    private final String text;

    private final String livedocId;

    private TypeElement(String text, String livedocId) {
        this.text = text;
        this.livedocId = livedocId;
    }

    public static TypeElement text(String text) {
        return new TypeElement(text, null);
    }

    public static TypeElement ref(String customName, Class<?> clazz) {
        return new TypeElement(customName, LivedocUtils.getLivedocId(clazz));
    }

    public String getText() {
        return text;
    }

    public String getLivedocId() {
        return livedocId;
    }
}
