package org.hildan.livedoc.core.model.doc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.model.doc.auth.ApiAuthDoc;
import org.hildan.livedoc.core.model.doc.auth.Secured;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;

import com.google.common.collect.Sets;

public class ApiMethodDoc extends AbstractDoc implements Comparable<ApiMethodDoc>, Scoped, Secured, Staged, Versioned {

    public final String livedocId = UUID.randomUUID().toString();

    private String id;

    private String name;

    private String summary;

    private String description;

    private Set<String> paths;

    private Set<ApiVerb> verbs;

    private Set<ApiParamDoc> pathParameters;

    private Set<ApiParamDoc> queryParameters;

    private Set<ApiHeaderDoc> headers;

    private ApiRequestBodyDoc requestBody;

    private LivedocType responseBodyType;

    private Set<String> consumes;

    private Set<String> produces;

    @SpecialDefaultStringValue(ApiMethod.DEFAULT_RESPONSE_STATUS)
    private String responseStatusCode;

    private MethodDisplay displayMethodAs;

    private List<ApiErrorDoc> apiErrors;

    private Visibility visibility;

    private ApiAuthDoc auth;

    private ApiVersionDoc supportedVersions;

    private Stage stage;

    public ApiMethodDoc() {
        super();
        this.id = null;
        this.description = "";
        this.summary = "";
        this.paths = new LinkedHashSet<>();
        this.verbs = new LinkedHashSet<>();
        this.produces = new LinkedHashSet<>();
        this.consumes = new LinkedHashSet<>();
        this.headers = new LinkedHashSet<>();
        this.pathParameters = new LinkedHashSet<>();
        this.queryParameters = new LinkedHashSet<>();
        this.requestBody = null;
        this.responseBodyType = null;
        this.responseStatusCode = ApiMethod.DEFAULT_RESPONSE_STATUS;
        this.visibility = null;
        this.stage = null;
        this.apiErrors = new ArrayList<>();
        this.supportedVersions = null;
        this.auth = null;
        this.displayMethodAs = MethodDisplay.URI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<String> getPaths() {
        return paths;
    }

    public void setPaths(Set<String> paths) {
        this.paths = paths;
    }

    public Set<ApiVerb> getVerbs() {
        return verbs;
    }

    public void setVerbs(Set<ApiVerb> verbs) {
        this.verbs = verbs;
    }

    public Set<ApiHeaderDoc> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<ApiHeaderDoc> headers) {
        this.headers = headers;
    }

    public Set<String> getProduces() {
        return produces;
    }

    public void setProduces(Set<String> produces) {
        this.produces = produces;
    }

    public Set<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(Set<String> consumes) {
        this.consumes = consumes;
    }

    public Set<ApiParamDoc> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(Set<ApiParamDoc> pathParameters) {
        this.pathParameters = pathParameters;
    }

    public Set<ApiParamDoc> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Set<ApiParamDoc> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public LivedocType getResponseBodyType() {
        return responseBodyType;
    }

    public void setResponseBodyType(LivedocType responseBodyType) {
        this.responseBodyType = responseBodyType;
    }

    public ApiRequestBodyDoc getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(ApiRequestBodyDoc requestBody) {
        this.requestBody = requestBody;
    }

    public List<ApiErrorDoc> getApiErrors() {
        return apiErrors;
    }

    public void setApiErrors(List<ApiErrorDoc> apiErrors) {
        this.apiErrors = apiErrors;
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

    public String getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(String responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public MethodDisplay getDisplayMethodAs() {
        return displayMethodAs;
    }

    public void setDisplayMethodAs(MethodDisplay displayMethodAs) {
        this.displayMethodAs = displayMethodAs;
    }

    public Set<String> getDisplayedMethodString() {
        switch (displayMethodAs) {
        case METHOD:
            return Sets.newHashSet(name);
        case SUMMARY:
            return Sets.newHashSet(summary);
        case URI:
        default:
            return paths;
        }
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
    public int compareTo(ApiMethodDoc o) {
        int i;

        if (this.paths.containsAll(o.getPaths()) && this.paths.size() == o.getPaths().size()) {
            i = 0;
        } else {
            i = 1;
        }

        if (i != 0) {
            return i;
        }

        if (this.verbs.containsAll(o.getVerbs()) && this.verbs.size() == o.getVerbs().size()) {
            i = 0;
        } else {
            i = 1;
        }

        if (i != 0) {
            return i;
        }

        if (this.queryParameters.size() == o.getQueryParameters().size()) {
            Set<ApiParamDoc> bothQueryParameters = new HashSet<>();
            bothQueryParameters.addAll(this.queryParameters);
            bothQueryParameters.addAll(o.getQueryParameters());
            if (bothQueryParameters.size() > this.queryParameters.size()) {
                i = 1;
            }
        } else {
            i = 1;
        }

        return i;
    }

}
