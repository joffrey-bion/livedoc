package org.hildan.livedoc.core.model.doc;

import java.util.List;

import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;

public class Livedoc {

    private String version;

    private String basePath;

    private List<Group<ApiDoc>> apis;

    private List<Group<ApiTypeDoc>> types;

    private List<Group<ApiFlowDoc>> flows;

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

    public List<Group<ApiDoc>> getApis() {
        return apis;
    }

    public void setApis(List<Group<ApiDoc>> apis) {
        this.apis = apis;
    }

    public List<Group<ApiTypeDoc>> getTypes() {
        return types;
    }

    public void setTypes(List<Group<ApiTypeDoc>> types) {
        this.types = types;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Group<ApiFlowDoc>> getFlows() {
        return flows;
    }

    public void setFlows(List<Group<ApiFlowDoc>> flows) {
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
        return "Livedoc [version=" + version + ", basePath=" + basePath + ", apis=" + apis + ", types=" + types
                + ", flows=" + flows + ", global=" + global + ", playgroundEnabled=" + playgroundEnabled
                + ", displayMethodAs=" + displayMethodAs + "]";
    }

}
