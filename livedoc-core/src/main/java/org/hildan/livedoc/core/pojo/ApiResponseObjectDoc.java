package org.hildan.livedoc.core.pojo;

import java.util.UUID;

import org.hildan.livedoc.core.builders.types.LivedocType;

public class ApiResponseObjectDoc {

    public final String livedocId = UUID.randomUUID().toString();

    private LivedocType type;

    public ApiResponseObjectDoc(LivedocType type) {
        this.type = type;
    }

    public LivedocType getType() {
        return type;
    }

}
