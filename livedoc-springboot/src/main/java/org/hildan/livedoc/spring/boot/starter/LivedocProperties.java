package org.hildan.livedoc.spring.boot.starter;

import java.util.List;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "livedoc")
public class LivedocProperties {

    /**
     * The name of your API.
     */
    private String name;

    /**
     * The version of your API.
     */
    private String version;

    /**
     * The base path of your API, for example http://your.host.com/api.
     */
    private String baseUrl;

    /**
     * The list of packages that to scan to look for annotated classes to be documented.
     */
    private List<String> packages;

    /**
     * Whether the playground should be enabled in the UI or not. Defaults to true.
     */
    private boolean playgroundEnabled = true;

    /**
     * Whether to display methods as URIs or with a short description (summary attribute in the {@link ApiOperation}
     * annotation).
     * Allowed values are URI and SUMMARY.
     */
    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
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

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public boolean isPlaygroundEnabled() {
        return playgroundEnabled;
    }

    public void setPlaygroundEnabled(boolean playgroundEnabled) {
        this.playgroundEnabled = playgroundEnabled;
    }

    public MethodDisplay getDisplayMethodAs() {
        return displayMethodAs;
    }

    public void setDisplayMethodAs(MethodDisplay displayMethodAs) {
        this.displayMethodAs = displayMethodAs;
    }

}
