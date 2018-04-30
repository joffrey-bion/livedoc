package org.hildan.livedoc.core.model.doc.version;

public interface Versioned {

    VersionDoc getSupportedVersions();

    void setSupportedVersions(VersionDoc versions);
}
