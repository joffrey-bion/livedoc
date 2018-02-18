package org.hildan.livedoc.core.model.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hildan.livedoc.core.model.doc.auth.ApiAuthDoc;
import org.hildan.livedoc.core.model.doc.auth.Secured;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;

public class ApiDoc implements Comparable<ApiDoc>, Groupable, Scoped, Secured, Staged, Versioned {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private String group;

    private List<ApiMethodDoc> methods;

    private Visibility visibility;

    private Stage stage;

    private ApiVersionDoc supportedVersions;

    private ApiAuthDoc auth;

    public ApiDoc() {
        this.name = "";
        this.description = "";
        this.visibility = null;
        this.stage = null;
        this.group = "";
        this.methods = new ArrayList<>();
        this.supportedVersions = null;
        this.auth = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ApiMethodDoc> getMethods() {
        return methods;
    }

    public void setMethods(List<ApiMethodDoc> methods) {
        this.methods = methods;
    }

    @Override
    public ApiVersionDoc getSupportedVersions() {
        return supportedVersions;
    }

    @Override
    public void setSupportedVersions(ApiVersionDoc supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

    @Override
    public ApiAuthDoc getAuth() {
        return auth;
    }

    @Override
    public void setAuth(ApiAuthDoc auth) {
        this.auth = auth;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public int compareTo(ApiDoc o) {
        return name.compareTo(o.getName());
    }

}
