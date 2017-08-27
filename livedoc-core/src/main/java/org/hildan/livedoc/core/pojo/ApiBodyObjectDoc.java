package org.hildan.livedoc.core.pojo;

import java.util.UUID;

import org.hildan.livedoc.core.util.LivedocType;

public class ApiBodyObjectDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private LivedocType livedocType;

    private LivedocTemplate jsondocTemplate;

    public ApiBodyObjectDoc(LivedocType livedocType) {
        this.livedocType = livedocType;
    }

    public LivedocType getLivedocType() {
        return livedocType;
    }

    public void setLivedocType(LivedocType livedocType) {
        this.livedocType = livedocType;
    }

    public LivedocTemplate getJsondocTemplate() {
        return jsondocTemplate;
    }

    public void setJsondocTemplate(LivedocTemplate jsondocTemplate) {
        this.jsondocTemplate = jsondocTemplate;
    }

}
