package org.hildan.livedoc.core.pojo;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApiStage {
    UNDEFINED(null),
    PRE_ALPHA("PRE-ALPHA"),
    ALPHA("ALPHA"),
    BETA("BETA"),
    RC("RC"),
    GA("GA"),
    DEPRECATED("DEPRECATED");

    private String label;

    ApiStage(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
