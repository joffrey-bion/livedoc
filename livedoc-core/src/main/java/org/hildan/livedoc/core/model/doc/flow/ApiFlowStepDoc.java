package org.hildan.livedoc.core.model.doc.flow;

import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;

import com.google.common.collect.Sets;

public class ApiFlowStepDoc {

    private static final String ERROR_MISSING_METHOD_ID = "No method found with id: %s";

    private static final String HINT_MISSING_METHOD_ID = "Add the same id to both ApiOperation and ApiFlowStep";

    public final String livedocId = UUID.randomUUID().toString();

    private String apiOperationId;

    private ApiOperationDoc apiOperationDoc;

    static ApiFlowStepDoc buildFromAnnotation(ApiFlowStep annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        ApiFlowStepDoc apiFlowStepDoc = new ApiFlowStepDoc();
        apiFlowStepDoc.setApiOperationId(annotation.apiOperationId());
        apiFlowStepDoc.setApiOperationDoc(getApiOperationDoc(annotation, apiOperationDocsById));
        return apiFlowStepDoc;
    }

    private static ApiOperationDoc getApiOperationDoc(ApiFlowStep annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        ApiOperationDoc apiOperationDoc = apiOperationDocsById.get(annotation.apiOperationId());
        if (apiOperationDoc == null) {
            apiOperationDoc = new ApiOperationDoc();
            String missingIdMsg = String.format(ERROR_MISSING_METHOD_ID, annotation.apiOperationId());
            apiOperationDoc.setPaths(Sets.newHashSet(missingIdMsg));
            apiOperationDoc.addJsondocError(missingIdMsg);
            apiOperationDoc.addJsondocHint(HINT_MISSING_METHOD_ID);
        }
        return apiOperationDoc;
    }

    public String getApiOperationId() {
        return apiOperationId;
    }

    public void setApiOperationId(String apiOperationId) {
        this.apiOperationId = apiOperationId;
    }

    public ApiOperationDoc getApiOperationDoc() {
        return apiOperationDoc;
    }

    public void setApiOperationDoc(ApiOperationDoc apiOperationDoc) {
        this.apiOperationDoc = apiOperationDoc;
    }

}
