package org.hildan.livedoc.core.model.doc;

import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.model.doc.auth.AuthDoc;
import org.hildan.livedoc.core.model.doc.auth.Secured;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;
import org.hildan.livedoc.core.readers.combined.SpecialDefaultStringValue;
import org.jetbrains.annotations.NotNull;

public class ApiDoc implements Comparable<ApiDoc>, Groupable, Secured, Staged, Versioned, Mergeable<ApiDoc> {

    private String livedocId;

    private String name;

    private String description;

    @SpecialDefaultStringValue("")
    private String group;

    private List<ApiOperationDoc> operations;

    private List<AsyncMessageDoc> messages;

    private Stage stage;

    private VersionDoc supportedVersions;

    private AuthDoc auth;

    public ApiDoc() {
        this.name = null;
        this.description = null;
        this.stage = null;
        this.group = "";
        this.operations = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.supportedVersions = null;
        this.auth = null;
    }

    public String getLivedocId() {
        return livedocId;
    }

    public void setLivedocId(String livedocId) {
        this.livedocId = livedocId;
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

    public List<AsyncMessageDoc> getMessages() {
        return messages;
    }

    public void setMessages(List<AsyncMessageDoc> messages) {
        this.messages = messages;
    }

    @Override
    public VersionDoc getSupportedVersions() {
        return supportedVersions;
    }

    @Override
    public void setSupportedVersions(VersionDoc supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

    @Override
    public AuthDoc getAuth() {
        return auth;
    }

    @Override
    public void setAuth(AuthDoc auth) {
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
    public int compareTo(@NotNull ApiDoc o) {
        return name.compareTo(o.getName());
    }

    @Override
    public ApiDoc merge(ApiDoc override, DocMerger merger) {
        ApiDoc merged = merger.mergeProperties(this, override, new ApiDoc());
        merged.operations = merger.mergeAndSort(this.operations, override.operations, ApiOperationDoc::getLivedocId);
        merged.messages = merger.mergeAndSort(this.messages, override.messages, AsyncMessageDoc::getLivedocId);
        return merged;
    }
}
