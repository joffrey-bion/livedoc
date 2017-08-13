package org.hildan.livedoc.core.pojo;

import java.util.UUID;

import org.hildan.livedoc.core.util.LivedocType;

public class ApiResponseObjectDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private LivedocType livedocType;

    public ApiResponseObjectDoc(LivedocType livedocType) {
        this.livedocType = livedocType;
    }

    public LivedocType getJsondocType() {
        return livedocType;
    }

}
