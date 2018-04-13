package org.hildan.livedoc.core.templating;

import java.util.List;

import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;

public class GlobalTemplateData {

    private final ApiMetaData apiInfo;

    private final LivedocMetaData livedocInfo;

    private final List<String> scannedPackages;

    public GlobalTemplateData(ApiMetaData apiInfo, LivedocMetaData livedocInfo, List<String> scannedPackages) {
        this.apiInfo = apiInfo;
        this.livedocInfo = livedocInfo;
        this.scannedPackages = scannedPackages;
    }

    public ApiMetaData getApiInfo() {
        return apiInfo;
    }

    public LivedocMetaData getLivedocInfo() {
        return livedocInfo;
    }

    public List<String> getScannedPackages() {
        return scannedPackages;
    }
}
