package org.hildan.livedoc.core.model.doc;

public class ApiErrorDoc {

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
