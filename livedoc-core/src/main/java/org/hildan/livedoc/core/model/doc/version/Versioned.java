package org.hildan.livedoc.core.model.doc.version;

public interface Versioned {

    ApiVersionDoc getSupportedVersions();

    void setSupportedVersions(ApiVersionDoc versions);
}
