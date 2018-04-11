package org.hildan.livedoc.core.model.doc;

public class ApiMetaData {

    /**
     * The current version of the API
     */
    private String version;

    /**
     * The base URL of the API, containing from protocol to context root. Ex: http://my.host.com/api
     */
    private String baseUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
