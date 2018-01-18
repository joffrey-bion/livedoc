package org.hildan.livedoc.core.pojo;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApiVisibility {
    UNDEFINED(null),
    PRIVATE("PRIVATE"),
    PUBLIC("PUBLIC");

    private String label;

    ApiVisibility(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
