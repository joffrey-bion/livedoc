package org.hildan.livedoc.core.model.doc;

import java.util.List;

import org.hildan.livedoc.core.model.doc.flow.FlowDoc;
import org.hildan.livedoc.core.model.doc.global.GlobalDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.groups.Group;

public class Livedoc {

    private final LivedocMetaData livedocInfo;

    private final ApiMetaData apiInfo;

    private List<Group<ApiDoc>> apis;

    private List<Group<TypeDoc>> types;

    private List<Group<FlowDoc>> flows;

    private GlobalDoc global;

    private boolean playgroundEnabled;

    private MethodDisplay displayMethodAs;

    public enum MethodDisplay {
        URI,
        SUMMARY,
        METHOD
    }

    public Livedoc(LivedocMetaData livedocInfo, ApiMetaData apiInfo) {
        this.livedocInfo = livedocInfo;
        this.apiInfo = apiInfo;
    }

    public LivedocMetaData getLivedocInfo() {
        return livedocInfo;
    }

    public ApiMetaData getApiInfo() {
        return apiInfo;
    }

    public List<Group<ApiDoc>> getApis() {
        return apis;
    }

    public void setApis(List<Group<ApiDoc>> apis) {
        this.apis = apis;
    }

    public List<Group<TypeDoc>> getTypes() {
        return types;
    }

    public void setTypes(List<Group<TypeDoc>> types) {
        this.types = types;
    }

    public List<Group<FlowDoc>> getFlows() {
        return flows;
    }

    public void setFlows(List<Group<FlowDoc>> flows) {
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

    public GlobalDoc getGlobal() {
        return global;
    }

    public void setGlobal(GlobalDoc global) {
        this.global = global;
    }

    @Override
    public String toString() {
        return "Livedoc [apiInfo=" + apiInfo + ", apis=" + apis + ", types=" + types + ", flows=" + flows + ", global="
                + global + ", playgroundEnabled=" + playgroundEnabled + ", displayMethodAs=" + displayMethodAs + "]";
    }

}
