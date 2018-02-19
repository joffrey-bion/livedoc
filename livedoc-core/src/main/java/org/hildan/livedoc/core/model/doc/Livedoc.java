package org.hildan.livedoc.core.model.doc;

import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;

public class Livedoc {

    private String version;

    private String basePath;

    // The key is the group these apis belongs to. It can be empty.
    private Map<String, Set<ApiDoc>> apis;

    // The key is the group these objects belongs to. It can be empty.
    private Map<String, Set<ApiTypeDoc>> objects;

    // The key is the group these flows belongs to. It can be empty.
    private Map<String, Set<ApiFlowDoc>> flows;

    private ApiGlobalDoc global;

    private boolean playgroundEnabled;

    private MethodDisplay displayMethodAs;

    public enum MethodDisplay {
        URI,
        SUMMARY,
        METHOD
    }

    public Livedoc(String version, String basePath) {
        super();
        this.version = version;
        this.basePath = basePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Set<ApiTypeDoc>> getObjects() {
        return objects;
    }

    public Map<String, Set<ApiDoc>> getApis() {
        return apis;
    }

    public void setApis(Map<String, Set<ApiDoc>> apis) {
        this.apis = apis;
    }

    public void setObjects(Map<String, Set<ApiTypeDoc>> objects) {
        this.objects = objects;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Map<String, Set<ApiFlowDoc>> getFlows() {
        return flows;
    }

    public void setFlows(Map<String, Set<ApiFlowDoc>> flows) {
        this.flows = flows;
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

    public ApiGlobalDoc getGlobal() {
        return global;
    }

    public void setGlobal(ApiGlobalDoc global) {
        this.global = global;
    }

    @Override
    public String toString() {
        return "Livedoc [version=" + version + ", basePath=" + basePath + ", apis=" + apis + ", objects=" + objects
                + ", flows=" + flows + ", global=" + global + ", playgroundEnabled=" + playgroundEnabled
                + ", displayMethodAs=" + displayMethodAs + "]";
    }

}