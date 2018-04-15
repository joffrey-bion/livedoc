package org.hildan.livedoc.core.model.doc;

public class ApiMetaData {

    /**
     * The name of the API
     */
    private String name;

    /**
     * The current version of the API
     */
    private String version;

    /**
     * The base URL of the API, containing from protocol to context root. Ex: http://my.host.com/api
     */
    private String baseUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
