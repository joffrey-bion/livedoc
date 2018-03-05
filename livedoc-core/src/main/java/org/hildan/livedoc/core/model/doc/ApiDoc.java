package org.hildan.livedoc.core.model.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.model.doc.auth.ApiAuthDoc;
import org.hildan.livedoc.core.model.doc.auth.Secured;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;

public class ApiDoc implements Comparable<ApiDoc>, Groupable, Secured, Staged, Versioned {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private String group;

    private List<ApiOperationDoc> operations;

    private Stage stage;

    private ApiVersionDoc supportedVersions;

    private ApiAuthDoc auth;

    public ApiDoc() {
        this.name = "";
        this.description = "";
        this.stage = null;
        this.group = "";
        this.operations = new ArrayList<>();
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

    public List<ApiOperationDoc> getOperations() {
        return operations;
    }

    public void setOperations(List<ApiOperationDoc> operations) {
        this.operations = operations;
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
