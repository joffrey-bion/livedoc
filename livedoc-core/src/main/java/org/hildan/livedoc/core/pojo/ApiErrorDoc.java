package org.hildan.livedoc.core.pojo;

import java.util.UUID;

public class ApiErrorDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private String code;

    private String description;

    public ApiErrorDoc(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
