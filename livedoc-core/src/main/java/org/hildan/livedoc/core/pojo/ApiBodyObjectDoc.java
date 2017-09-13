package org.hildan.livedoc.core.pojo;

import java.util.UUID;

import org.hildan.livedoc.core.builders.types.LivedocType;

public class ApiBodyObjectDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private final LivedocType type;

    private final Object template;

    public ApiBodyObjectDoc(LivedocType type, Object template) {
        this.type = type;
        this.template = template;
    }

    public LivedocType getType() {
        return type;
    }

    public Object getTemplate() {
        return template;
    }
}
