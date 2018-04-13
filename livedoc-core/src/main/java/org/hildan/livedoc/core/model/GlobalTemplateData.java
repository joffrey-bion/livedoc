package org.hildan.livedoc.core.model;

import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;

public class GlobalTemplateData {

    private final ApiMetaData apiInfo;

    private final LivedocMetaData livedocInfo;

    private final LivedocConfiguration livedocConfig;

    public GlobalTemplateData(ApiMetaData apiInfo, LivedocMetaData livedocInfo, LivedocConfiguration livedocConfig) {
        this.apiInfo = apiInfo;
        this.livedocInfo = livedocInfo;
        this.livedocConfig = livedocConfig;
    }

    public ApiMetaData getApiInfo() {
        return apiInfo;
    }

    public LivedocMetaData getLivedocInfo() {
        return livedocInfo;
    }

    public LivedocConfiguration getLivedocConfig() {
        return livedocConfig;
    }
}
