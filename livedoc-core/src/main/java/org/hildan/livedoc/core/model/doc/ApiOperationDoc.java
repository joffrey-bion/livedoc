package org.hildan.livedoc.core.model.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.model.doc.auth.ApiAuthDoc;
import org.hildan.livedoc.core.model.doc.auth.Secured;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;
import org.hildan.livedoc.core.readers.combined.SpecialDefaultStringValue;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.jetbrains.annotations.NotNull;

public class ApiOperationDoc extends AbstractDoc implements Comparable<ApiOperationDoc>, Secured, Staged, Versioned,
        Mergeable<ApiOperationDoc> {

    private static final Comparator<ApiOperationDoc> PATHS_COMPARATOR = LivedocUtils.comparingFirstItem(
            ApiOperationDoc::getPaths);

    private static final Comparator<ApiOperationDoc> VERBS_COMPARATOR = LivedocUtils.comparingFirstItem(
            ApiOperationDoc::getVerbs);

    private static final Comparator<ApiOperationDoc> PARAMS_COMPARATOR = LivedocUtils.comparingFirstItem(
            ApiOperationDoc::getQueryParameters);

    private static final Comparator<ApiOperationDoc> COMPARATOR = PATHS_COMPARATOR.thenComparing(VERBS_COMPARATOR)
                                                                                  .thenComparing(PARAMS_COMPARATOR);

    private String id;

    private String name;

    private String summary;

    private String description;

    private List<String> paths;

    private List<ApiVerb> verbs;

    private List<ApiParamDoc> pathParameters;

    private List<ApiParamDoc> queryParameters;

    private List<ApiHeaderDoc> headers;

    private ApiRequestBodyDoc requestBody;

    private LivedocType responseBodyType;

    private List<String> consumes;

    private List<String> produces;

    @SpecialDefaultStringValue(ApiOperation.DEFAULT_RESPONSE_STATUS)
    private String responseStatusCode;

    private List<ApiErrorDoc> apiErrors;

    private ApiAuthDoc auth;

    private ApiVersionDoc supportedVersions;

    private Stage stage;

    public ApiOperationDoc() {
        super();
        this.id = null;
        this.description = null;
        this.summary = null;
        this.paths = new ArrayList<>();
        this.verbs = new ArrayList<>();
        this.produces = new ArrayList<>();
        this.consumes = new ArrayList<>();
        this.headers = new ArrayList<>();
        this.pathParameters = new ArrayList<>();
        this.queryParameters = new ArrayList<>();
        this.requestBody = null;
        this.responseBodyType = null;
        this.responseStatusCode = ApiOperation.DEFAULT_RESPONSE_STATUS;
        this.stage = null;
        this.apiErrors = new ArrayList<>();
        this.supportedVersions = null;
        this.auth = null;
    }

    public String getLivedocId() {
        // a given verb with a given path is by definition unique, even among controllers
        String firstVerb = verbs.isEmpty() ? "" : verbs.get(0).toString();
        String firstPath = paths.isEmpty() ? "" : paths.get(0);
        firstPath = firstPath.replaceAll("[{}]", "");
        firstPath = firstPath.replaceAll("/", "-");
        if (!firstPath.startsWith("-")) {
            firstPath = "-" + firstPath;
        }
        return LivedocUtils.asLivedocId(firstVerb + firstPath);
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

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public List<ApiVerb> getVerbs() {
        return verbs;
    }

    public void setVerbs(List<ApiVerb> verbs) {
        this.verbs = new ArrayList<>(verbs);
        Collections.sort(this.verbs);
    }

    public List<ApiHeaderDoc> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ApiHeaderDoc> headers) {
        this.headers = headers;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<ApiParamDoc> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(List<ApiParamDoc> pathParameters) {
        this.pathParameters = pathParameters;
    }

    public List<ApiParamDoc> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(List<ApiParamDoc> queryParameters) {
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

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public ApiOperationDoc merge(ApiOperationDoc override, DocMerger merger) {
        ApiOperationDoc doc = merger.mergeProperties(this, override, new ApiOperationDoc());
        // For paths, consumes, produces, and errors, we want the last non empty list to win, no merge
        doc.pathParameters = merger.mergeAndSort(this.pathParameters, override.pathParameters, ApiParamDoc::getName);
        doc.queryParameters = merger.mergeAndSort(this.queryParameters, override.queryParameters, ApiParamDoc::getName);
        doc.headers = merger.mergeList(this.headers, override.headers, ApiHeaderDoc::getName);
        return doc;
    }

    @Override
    public int compareTo(@NotNull ApiOperationDoc o) {
        return COMPARATOR.compare(this, o);
    }
}
