package org.hildan.livedoc.core.validators;

import java.util.Collections;

import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;

public class ApiOperationDocValidator {

    private static final String ERROR_MISSING_OPERATION_PATH = "Missing documentation data: path";

    private static final String ERROR_MISSING_PATH_PARAM_NAME = "Missing documentation data: path parameter name";

    private static final String ERROR_MISSING_QUERY_PARAM_NAME = "Missing documentation data: query parameter name";

    private static final String ERROR_MISSING_HEADER_NAME = "Missing documentation data: header name";

    private static final String WARN_MISSING_OPERATION_PRODUCES = "Missing documentation data: produces";

    private static final String WARN_MISSING_OPERATION_CONSUMES = "Missing documentation data: consumes";

    private static final String HINT_MISSING_PATH_PARAM_DESCRIPTION = "Add description to ApiPathParam";

    private static final String HINT_MISSING_QUERY_PARAM_DESCRIPTION = "Add description to ApiQueryParam";

    private static final String HINT_MISSING_OPERATION_DESCRIPTION = "Add description to ApiOperation";

    private static final String HINT_MISSING_OPERATION_BODY_OBJECT =
            "Add annotation ApiRequestBodyType to document " + "the expected body of the request";

    private static final String HINT_MISSING_OPERATION_RESPONSE_OBJECT =
            "Add annotation ApiResponseBodyType to " + "document the returned object";

    /**
     * This checks that some of the properties are correctly set to produce a meaningful documentation and a working
     * playground.
     * <p>
     * If the provided {@link ApiOperationDoc} is missing some required information, an error string is added to the doc
     * through {@link ApiOperationDoc#addJsondocError(String)}. If some optional information is missing but could really
     * help produce a meaningful documentation, warnings are added instead of errors.
     *
     * @param apiOperationDoc
     *         the doc to validate
     */
    public static void validate(ApiOperationDoc apiOperationDoc) {

        if (apiOperationDoc.getPaths().isEmpty()) {
            apiOperationDoc.setPaths(Collections.singletonList(ERROR_MISSING_OPERATION_PATH));
            apiOperationDoc.addJsondocError(ERROR_MISSING_OPERATION_PATH);
        }

        String description = apiOperationDoc.getDescription();
        if (description == null || description.isEmpty()) {
            apiOperationDoc.addJsondocHint(HINT_MISSING_OPERATION_DESCRIPTION);
        }

        validateHeaders(apiOperationDoc);
        validatePathParams(apiOperationDoc);
        validateQueryParams(apiOperationDoc);
        validateRequestBody(apiOperationDoc);
        validateResponse(apiOperationDoc);
    }

    private static void validateHeaders(ApiOperationDoc apiOperationDoc) {
        for (ApiHeaderDoc apiHeaderDoc : apiOperationDoc.getHeaders()) {
            if (apiHeaderDoc.getName() == null) {
                apiOperationDoc.addJsondocError(ERROR_MISSING_HEADER_NAME);
            }
        }
    }

    private static void validatePathParams(ApiOperationDoc apiOperationDoc) {
        for (ApiParamDoc apiParamDoc : apiOperationDoc.getPathParameters()) {
            if (apiParamDoc.getName() == null) {
                apiOperationDoc.addJsondocError(ERROR_MISSING_PATH_PARAM_NAME);
            }

            if (apiParamDoc.getDescription() == null) {
                apiOperationDoc.addJsondocHint(HINT_MISSING_PATH_PARAM_DESCRIPTION);
            }
        }
    }

    private static void validateQueryParams(ApiOperationDoc apiOperationDoc) {
        for (ApiParamDoc apiParamDoc : apiOperationDoc.getQueryParameters()) {
            if (apiParamDoc.getName() == null) {
                apiOperationDoc.addJsondocError(ERROR_MISSING_QUERY_PARAM_NAME);
            }
            if (apiParamDoc.getDescription() == null) {
                apiOperationDoc.addJsondocHint(HINT_MISSING_QUERY_PARAM_DESCRIPTION);
            }
        }
    }

    private static void validateRequestBody(ApiOperationDoc apiOperationDoc) {
        if (apiOperationDoc.getVerbs().stream().anyMatch(ApiVerb::requiresBody)) {
            if (apiOperationDoc.getConsumes().isEmpty()) {
                apiOperationDoc.addJsondocWarning(WARN_MISSING_OPERATION_CONSUMES);
            }
            if (apiOperationDoc.getRequestBody() == null) {
                apiOperationDoc.addJsondocHint(HINT_MISSING_OPERATION_BODY_OBJECT);
            }
        }
    }

    private static void validateResponse(ApiOperationDoc apiOperationDoc) {
        if (apiOperationDoc.getProduces().isEmpty()) {
            apiOperationDoc.addJsondocWarning(WARN_MISSING_OPERATION_PRODUCES);
        }
        if (apiOperationDoc.getResponseBodyType() == null) {
            apiOperationDoc.addJsondocHint(HINT_MISSING_OPERATION_RESPONSE_OBJECT);
        }
    }

}
