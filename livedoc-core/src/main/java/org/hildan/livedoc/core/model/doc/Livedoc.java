package org.hildan.livedoc.core.model.doc;

import java.util.List;

import org.hildan.livedoc.core.model.doc.global.GlobalDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.groups.Group;

public class Livedoc {

    private final LivedocMetaData livedocInfo;

    private final ApiMetaData apiInfo;

    private List<Group<ApiDoc>> apis;

    private List<Group<TypeDoc>> types;

    private GlobalDoc global;

    private boolean playgroundEnabled;

    private MethodDisplay displayMethodAs;

    private final String error;

    public enum MethodDisplay {
        URI,
        SUMMARY,
        METHOD
    }

    public Livedoc(LivedocMetaData livedocInfo, String error) {
        this.livedocInfo = livedocInfo;
        this.apiInfo = null;
        this.error = error;
    }

    public Livedoc(LivedocMetaData livedocInfo, ApiMetaData apiInfo) {
        this.livedocInfo = livedocInfo;
        this.apiInfo = apiInfo;
        this.error = null;
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

    public String getError() {
        return error;
    }

    public String toString() {
        return "Livedoc{" + "livedocInfo=" + livedocInfo + ", apiInfo=" + apiInfo + ", apis=" + apis + ", types="
                + types + ", global=" + global + ", playgroundEnabled=" + playgroundEnabled + ", displayMethodAs="
                + displayMethodAs + '}';
    }
}
