package org.hildan.livedoc.core.model.types;

/**
 * Represents an element in a {@link LivedocType}. An element can either be a textual element for display purposes,
 * such as "[]" for arrays, or a reference to a documented class, in the form of its livedoc ID.
 */
public class TypeReferenceElement {

    static final TypeReferenceElement VOID = text("void");

    static final TypeReferenceElement COMMA = text(", ");

    static final TypeReferenceElement OPEN_ANGLE_BRACKET = text("<");

    static final TypeReferenceElement CLOSE_ANGLE_BRACKET = text(">");

    static final TypeReferenceElement SQUARE_BRACKETS = text("[]");

    static final TypeReferenceElement QUESTION_MARK = text("?");

    static final TypeReferenceElement EXTENDS = text(" extends ");

    static final TypeReferenceElement SUPER = text(" super ");

    private final String text;

    private final String livedocId;

    private TypeReferenceElement(String text, String livedocId) {
        this.text = text;
        this.livedocId = livedocId;
    }

    static TypeReferenceElement text(String text) {
        return new TypeReferenceElement(text, null);
    }

    static TypeReferenceElement link(String customName, String livedocId) {
        return new TypeReferenceElement(customName, livedocId);
    }

    public String getText() {
        return text;
    }

    public String getLivedocId() {
        return livedocId;
    }
}
