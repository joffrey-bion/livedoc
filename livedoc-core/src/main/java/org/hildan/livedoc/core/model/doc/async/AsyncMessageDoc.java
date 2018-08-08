package org.hildan.livedoc.core.model.doc.async;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hildan.livedoc.core.annotations.messages.StompCommand;
import org.hildan.livedoc.core.model.doc.AbstractDoc;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.doc.Stage;
import org.hildan.livedoc.core.model.doc.Staged;
import org.hildan.livedoc.core.model.doc.auth.AuthDoc;
import org.hildan.livedoc.core.model.doc.auth.Secured;
import org.hildan.livedoc.core.model.doc.headers.HeaderDoc;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.jetbrains.annotations.NotNull;

public class AsyncMessageDoc extends AbstractDoc implements Comparable<AsyncMessageDoc>, Mergeable<AsyncMessageDoc>,
        Secured, Staged, Versioned {

    private static final Comparator<AsyncMessageDoc> COMMAND_COMPARATOR = Comparator.comparing(
            AsyncMessageDoc::getCommand);

    private static final Comparator<AsyncMessageDoc> DESTS_COMPARATOR = LivedocUtils.comparingFirstItem(
            AsyncMessageDoc::getDestinations);

    private String livedocId;

    private String name;

    private String summary;

    private String description;

    private StompCommand command;

    private List<String> destinations;

    private List<ParamDoc> destinationVariables;

    private List<HeaderDoc> headers;

    private LivedocType payloadType;

    private List<AsyncMessageDoc> triggeredMessages;

    private AuthDoc auth;

    private VersionDoc supportedVersions;

    private Stage stage;

    public AsyncMessageDoc() {
        super();
        this.destinations = new ArrayList<>();
        this.summary = null;
        this.description = null;
        this.headers = new ArrayList<>();
        this.destinationVariables = new ArrayList<>();
        this.auth = null;
        this.supportedVersions = null;
        this.stage = null;
        this.triggeredMessages = new ArrayList<>();
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StompCommand getCommand() {
        return command;
    }

    public void setCommand(StompCommand command) {
        this.command = command;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public List<HeaderDoc> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderDoc> headers) {
        this.headers = headers;
    }

    public List<ParamDoc> getDestinationVariables() {
        return destinationVariables;
    }

    public void setDestinationVariables(List<ParamDoc> destinationVariables) {
        this.destinationVariables = destinationVariables;
    }

    public LivedocType getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(LivedocType payloadType) {
        this.payloadType = payloadType;
    }

    public List<AsyncMessageDoc> getTriggeredMessages() {
        return triggeredMessages;
    }

    public void setTriggeredMessages(List<AsyncMessageDoc> triggeredMessages) {
        this.triggeredMessages = triggeredMessages;
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
    public VersionDoc getSupportedVersions() {
        return supportedVersions;
    }

    @Override
    public void setSupportedVersions(VersionDoc supportedVersions) {
        this.supportedVersions = supportedVersions;
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
    public AsyncMessageDoc merge(AsyncMessageDoc override, DocMerger merger) {
        AsyncMessageDoc doc = merger.mergeProperties(this, override, new AsyncMessageDoc());
        // For destinations, we want the last non empty list to win, no merge
        doc.setDestinationVariables(
                merger.mergeAndSort(this.getDestinationVariables(), override.getDestinationVariables(),
                        ParamDoc::getName));
        doc.setHeaders(merger.mergeLists(this.getHeaders(), override.getHeaders(), HeaderDoc::getName));
        return doc;
    }

    @Override
    public int compareTo(@NotNull AsyncMessageDoc o) {
        return COMMAND_COMPARATOR.thenComparing(DESTS_COMPARATOR).compare(this, o);
    }

    @Override
    public String toString() {
        return command + " " + destinations;
    }
}
